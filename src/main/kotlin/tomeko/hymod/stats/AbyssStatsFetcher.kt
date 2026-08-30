package tomeko.hymod.stats

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.minecraft.util.Util
import java.net.HttpURLConnection
import java.net.URI
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.sqrt

object AbyssStatsFetcher {
    private const val MOJANG_UUID_ENDPOINT = "https://api.mojang.com/users/profiles/minecraft/"
    private const val ABYSS_PLAYER_ENDPOINT = "http://api.abyssoverlay.com/player?uuid="
    private const val ABYSS_USER_AGENT = "node-ao/2.0.3"
    private const val CACHE_TTL_MS = 120_000L

    private val uuidCache = ConcurrentHashMap<String, String>()

    private data class CachedRaw(
        val fetchedAt: Long,
        val json: JsonObject
    )

    private val statsCache = ConcurrentHashMap<String, CachedRaw>()
    private val pendingRequests = ConcurrentHashMap<String, CompletableFuture<JsonObject?>>()

    data class DuelsDivisions(
        val overall: String,
        val uhc: String,
        val sw: String,
        val bridge: String,
        val sumo: String,
        val bow: String
    )

    fun getUuid(playerName: String): CompletableFuture<String?> {
        val key = playerName.lowercase()

        uuidCache[key]?.let {
            return CompletableFuture.completedFuture(it)
        }

        return CompletableFuture.supplyAsync({
            uuidCache[key]?.let {
                return@supplyAsync it
            }

            try {
                val connection =
                    URI.create(MOJANG_UUID_ENDPOINT + playerName).toURL().openConnection() as HttpURLConnection

                try {
                    connection.requestMethod = "GET"
                    connection.connectTimeout = 5000
                    connection.readTimeout = 5000

                    if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                        return@supplyAsync null
                    }

                    val body = connection.inputStream.bufferedReader().use { it.readText() }

                    val uuid = JsonParser.parseString(body).asJsonObject.get("id")?.asString

                    if (uuid != null) {
                        uuidCache[key] = uuid
                    }

                    uuid
                } finally {
                    connection.disconnect()
                }
            } catch (_: Exception) {
                null
            }
        }, Util.ioPool())
    }

    fun getRawPlayerData(uuid: String): CompletableFuture<JsonObject?> {
        val cached = statsCache[uuid]

        if (cached != null && System.currentTimeMillis() - cached.fetchedAt < CACHE_TTL_MS) {
            return CompletableFuture.completedFuture(cached.json)
        }


        return pendingRequests.computeIfAbsent(uuid) {
            CompletableFuture.supplyAsync({
                try {
                    val connection =
                        URI.create(ABYSS_PLAYER_ENDPOINT + uuid).toURL().openConnection() as HttpURLConnection

                    try {
                        connection.requestMethod = "GET"
                        connection.connectTimeout = 5000
                        connection.readTimeout = 5000
                        connection.setRequestProperty("User-Agent", ABYSS_USER_AGENT)
                        connection.setRequestProperty("Accept", "application/json")

                        if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                            return@supplyAsync null
                        }

                        val body = connection.inputStream.bufferedReader().use { it.readText() }
                        val root = JsonParser.parseString(body).asJsonObject
                        val playerObj = root.getAsJsonObject("player") ?: return@supplyAsync null

                        statsCache[uuid] = CachedRaw(fetchedAt = System.currentTimeMillis(), json = playerObj)

                        playerObj
                    } finally {
                        connection.disconnect()
                    }
                } catch (_: Exception) {
                    null
                }
            }, Util.ioPool()).whenComplete { _, _ ->
                pendingRequests.remove(uuid)
            }
        }
    }

    fun getHypixelLevel(uuid: String): CompletableFuture<Double?> {
        return getRawPlayerData(uuid).thenApply { player ->
            val exp = player?.get("networkExp")?.asDouble ?: return@thenApply null

            (sqrt(exp + 15312.5) - 88.38834764831844) / 35.35533905932738
        }
    }

    fun getBedwarsStars(uuid: String): CompletableFuture<Int?> {
        return getRawPlayerData(uuid).thenApply { player ->
            player?.getAsJsonObject("achievements")?.get("bedwars_level")?.asInt
        }
    }

    fun getSkywarsLevel(uuid: String): CompletableFuture<Int?> {
        return getRawPlayerData(uuid).thenApply { player ->
            val exp = player?.getAsJsonObject("stats")?.getAsJsonObject("SkyWars")?.get("skywars_experience")?.asLong
                ?: return@thenApply null

            skywarsExpToLevel(exp)
        }
    }

    fun getDuelsDivisions(uuid: String): CompletableFuture<DuelsDivisions?> {
        return getRawPlayerData(uuid).thenApply { player ->
            val duels = player?.getAsJsonObject("stats")?.getAsJsonObject("Duels")
                ?: return@thenApply DuelsDivisions(
                    overall = "Rookie",
                    uhc = "Rookie",
                    sw = "Rookie",
                    bridge = "Rookie",
                    sumo = "Rookie",
                    bow = "Rookie"
                )

            fun divisionFor(wins: Int): String = when {
                wins >= 2500 -> "Grandmaster"
                wins >= 1000 -> "Master"
                wins >= 500 -> "Diamond"
                wins >= 250 -> "Gold"
                wins >= 100 -> "Silver"
                wins >= 50 -> "Iron"
                else -> "Rookie"
            }

            fun winsFor(field: String): Int = duels.get(field)?.asInt ?: 0

            DuelsDivisions(
                overall = divisionFor(winsFor("wins")),
                uhc = divisionFor(winsFor("uhc_wins")),
                sw = divisionFor(winsFor("sw_wins")),
                bridge = divisionFor(winsFor("bridge_wins")),
                sumo = divisionFor(winsFor("sumo_wins")),
                bow = divisionFor(winsFor("bow_wins"))
            )
        }
    }

    private fun skywarsExpToLevel(exp: Long): Int {
        val xpTable = longArrayOf(
            0,
            20,
            70,
            150,
            250,
            500,
            1000,
            2000,
            3500,
            6000,
            10000
        )

        if (exp >= 15000) {
            return ((exp - 15000) / 10000 + 12).toInt()
        }

        for (i in xpTable.indices.reversed()) {
            if (exp >= xpTable[i]) {
                return i + 1
            }
        }

        return 1
    }

    fun clearCache() {
        uuidCache.clear()
        statsCache.clear()
        pendingRequests.clear()
    }
}