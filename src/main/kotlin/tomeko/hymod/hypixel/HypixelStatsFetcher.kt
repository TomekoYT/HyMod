package tomeko.hymod.hypixel

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

object HypixelStatsFetcher {
    private const val MOJANG_UUID_ENDPOINT = "https://api.mojang.com/users/profiles/minecraft/"
    private const val ABYSS_PLAYER_ENDPOINT = "http://api.abyssoverlay.com/player?uuid="
    private const val ABYSS_USER_AGENT = "node-ao/2.0.3"
    private const val CACHE_TTL_MS = 120_000L

    private val uuidCache = ConcurrentHashMap<String, String>()

    private data class CachedRaw(val fetchedAt: Long, val json: JsonObject)
    private val statsCache = ConcurrentHashMap<String, CachedRaw>()

    data class DuelsDivisions(
        val overall: String,
        val uhc: String,
        val sw: String,
        val bridge: String,
        val sumo: String,
        val bow: String
    )

    /** Resolves and caches a player's UUID from their username. */
    fun getUuid(playerName: String): String? {
        val key = playerName.lowercase()
        uuidCache[key]?.let { return it }

        return try {
            val conn = URL(MOJANG_UUID_ENDPOINT + playerName).openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 5000
            conn.readTimeout = 5000

            if (conn.responseCode != HttpURLConnection.HTTP_OK) return null

            val body = conn.inputStream.bufferedReader().use { it.readText() }
            val uuid = JsonParser.parseString(body).asJsonObject.get("id")?.asString
            if (uuid != null) uuidCache[key] = uuid
            uuid
        } catch (e: Exception) {
            null
        }
    }

    /** Fetches (and caches, TTL 120s) the raw Hypixel /player payload for a UUID. */
    private fun getRawPlayerData(uuid: String): JsonObject? {
        statsCache[uuid]?.let { cached ->
            if (System.currentTimeMillis() - cached.fetchedAt < CACHE_TTL_MS) {
                return cached.json
            }
        }

        return try {
            val conn = URL(ABYSS_PLAYER_ENDPOINT + uuid).openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 5000
            conn.readTimeout = 5000
            conn.setRequestProperty("User-Agent", ABYSS_USER_AGENT)
            conn.setRequestProperty("Accept", "application/json")

            if (conn.responseCode != HttpURLConnection.HTTP_OK) return null

            val body = conn.inputStream.bufferedReader().use { it.readText() }
            val root = JsonParser.parseString(body).asJsonObject
            val playerObj = root.getAsJsonObject("player") ?: return null

            statsCache[uuid] = CachedRaw(System.currentTimeMillis(), playerObj)
            playerObj
        } catch (e: Exception) {
            null
        }
    }

    /** Overall Hypixel network level (approximate formula Hypixel itself uses). */
    fun getHypixelLevel(uuid: String): Double? {
        val player = getRawPlayerData(uuid) ?: return null
        val exp = player.get("networkExp")?.asDouble ?: 0.0
        return (Math.sqrt(exp + 15312.5) - 88.38834764831844) / 35.35533905932738
    }

    fun getBedwarsStars(uuid: String): Int? {
        val player = getRawPlayerData(uuid) ?: return null
        return player.getAsJsonObject("achievements")
            ?.get("bedwars_level")?.asInt ?: 0
    }

    fun getSkywarsLevel(uuid: String): Int? {
        val player = getRawPlayerData(uuid) ?: return null
        val exp = player.getAsJsonObject("stats")
            ?.getAsJsonObject("SkyWars")
            ?.get("skywars_experience")?.asLong ?: 0L
        return skywarsExpToLevel(exp)
    }

    fun getDuelsDivisions(uuid: String): DuelsDivisions? {
        val player = getRawPlayerData(uuid) ?: return null
        val duels = player.getAsJsonObject("stats")?.getAsJsonObject("Duels")
            ?: return DuelsDivisions("Rookie", "Rookie", "Rookie", "Rookie", "Rookie", "Rookie")

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

        return DuelsDivisions(
            overall = divisionFor(winsFor("wins")),
            uhc = divisionFor(winsFor("uhc_wins")),
            sw = divisionFor(winsFor("sw_wins")),
            bridge = divisionFor(winsFor("bridge_wins")),
            sumo = divisionFor(winsFor("sumo_wins")),
            bow = divisionFor(winsFor("bow_wins"))
        )
    }

    private fun skywarsExpToLevel(exp: Long): Int {
        val xpTable = longArrayOf(0, 20, 70, 150, 250, 500, 1000, 2000, 3500, 6000, 10000)
        if (exp >= 15000) return ((exp - 15000) / 10000 + 12).toInt()
        for (i in xpTable.indices.reversed()) {
            if (exp >= xpTable[i]) return i + 1
        }
        return 1
    }

    fun clearCache() {
        uuidCache.clear()
        statsCache.clear()
    }
}