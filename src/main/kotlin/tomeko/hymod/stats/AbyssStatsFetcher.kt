package tomeko.hymod.stats

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.hypixel.modapi.HypixelModAPI
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket
import net.minecraft.client.Minecraft
import tomeko.hymod.location.DuelsMode
import tomeko.hymod.location.DuelsModeType
import tomeko.hymod.location.HypixelPackets
import tomeko.hymod.utils.Constants
import tomeko.hymod.utils.toRoman
import java.net.HttpURLConnection
import java.net.URI
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.String
import kotlin.math.sqrt

object AbyssStatsFetcher {
    private var lastHypixelState = false

    fun register() {
        //? if = 1.8.9 {
        /*MinecraftForge.EVENT_BUS.register(this)
        *///?} else {
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick)
        //?}
        HypixelModAPI.getInstance().createHandler(ClientboundLocationPacket::class.java, this::onLocationPacket)
        HypixelModAPI.getInstance().subscribeToEventPacket(ClientboundLocationPacket::class.java)
    }

    //? if = 1.8.9 {
    /*@SubscribeEvent
    *///?}
    fun onTick(
        //? if = 1.8.9 {
        /*event: TickEvent.ClientTickEvent
        *///?} else {
        mc: Minecraft
        //?}
    ) {
        //? if = 1.8.9 {
        /*if (event.phase != TickEvent.Phase.END) return
        *///?}

        if (lastHypixelState != HypixelPackets.onHypixel) {
            lastHypixelState = HypixelPackets.onHypixel
            clearCache()
        }
    }

    private fun onLocationPacket(packet: ClientboundLocationPacket) {
        clearCache()
    }


    private const val MOJANG_UUID_ENDPOINT = "https://api.mojang.com/users/profiles/minecraft/"
    private const val ABYSS_PLAYER_ENDPOINT = "http://api.abyssoverlay.com/player?uuid="
    private const val ABYSS_USER_AGENT = "node-ao/2.0.3"
    private const val CACHE_TTL_MS = 120_000L

    private const val NETWORK_POOL_SIZE = 6
    private val networkThreadCounter = AtomicInteger(0)
    private val networkThreadFactory = ThreadFactory { runnable ->
        Thread(runnable, "${Constants.MOD_ID}-stats-fetch-${networkThreadCounter.incrementAndGet()}").apply {
            isDaemon = true
        }
    }
    private val networkExecutor = ThreadPoolExecutor(
        NETWORK_POOL_SIZE,
        NETWORK_POOL_SIZE,
        30L, TimeUnit.SECONDS,
        LinkedBlockingQueue(),
        networkThreadFactory
    ).apply { allowCoreThreadTimeOut(true) }

    private const val CONNECT_TIMEOUT_MS = 3000
    private const val READ_TIMEOUT_MS = 3000

    private data class CachedRaw(
        val fetchedAt: Long,
        val json: JsonObject
    )

    private val uuidCache = ConcurrentHashMap<String, String>()
    private val statsCache = ConcurrentHashMap<String, CachedRaw>()
    private val pendingRequests = ConcurrentHashMap<String, CompletableFuture<JsonObject?>>()

    val pendingLevel: ConcurrentHashMap.KeySetView<String?, Boolean?> = ConcurrentHashMap.newKeySet<String>()
    val pendingBedwars: ConcurrentHashMap.KeySetView<String?, Boolean?> = ConcurrentHashMap.newKeySet<String>()
    val pendingSkywars: ConcurrentHashMap.KeySetView<String?, Boolean?> = ConcurrentHashMap.newKeySet<String>()
    val pendingDuels: ConcurrentHashMap.KeySetView<String?, Boolean?> = ConcurrentHashMap.newKeySet<String>()

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
                    connection.connectTimeout = CONNECT_TIMEOUT_MS
                    connection.readTimeout = READ_TIMEOUT_MS

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
        }, networkExecutor)
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
                        connection.connectTimeout = CONNECT_TIMEOUT_MS
                        connection.readTimeout = READ_TIMEOUT_MS
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
            }, networkExecutor).whenComplete { _, _ ->
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

            fun skywarsExpToLevel(exp: Long): Int {
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

            skywarsExpToLevel(exp)
        }
    }

    fun getDuelsDivision(uuid: String, duelsMode: DuelsMode): CompletableFuture<String?> {
        return getRawPlayerData(uuid).thenApply { player ->
            val duels = player?.getAsJsonObject("stats")?.getAsJsonObject("Duels")
                ?: return@thenApply null

            fun wins(field: String): Int = duels.get(field)?.asInt ?: 0

            fun division(duelsMode: DuelsMode, wins: Int): String {
                return when (duelsMode.modeType) {
                    DuelsModeType.SHORT -> when {
                        wins >= 100_000 -> "§cAscended " + ((wins - 100_000) / 10_000 + 1).toRoman()
                        wins >= 50_000 -> "§dDivine " + ((wins - 50_000) / 10_000 + 1).toRoman()
                        wins >= 25_000 -> "§9Celestial " + ((wins - 25_000) / 5_000 + 1).toRoman()
                        wins >= 10_000 -> "§5Godlike " + ((wins - 10_000) / 3_000 + 1).toRoman()
                        wins >= 5_000 -> "§cGrandmaster " + ((wins - 5_000) / 1_000 + 1).toRoman()
                        wins >= 2_000 -> "§4Legend " + ((wins - 2_000) / 600 + 1).toRoman()
                        wins >= 1_000 -> "§2Master " + ((wins - 1_000) / 200 + 1).toRoman()
                        wins >= 500 -> "§bDiamond " + ((wins - 500) / 100 + 1).toRoman()
                        wins >= 250 -> "§6Gold " + ((wins - 250) / 50 + 1).toRoman()
                        wins >= 100 -> "§fIron " + ((wins - 100) / 30 + 1).toRoman()
                        wins >= 50 -> "§7Rookie " + ((wins - 50) / 10 + 1).toRoman()
                        else -> "§8Unranked"
                    }

                    DuelsModeType.LONG -> when {
                        wins >= 50_000 -> "§cAscended " + ((wins - 50_000) / 5_000 + 1).toRoman()
                        wins >= 25_000 -> "§dDivine " + ((wins - 25_000) / 5_000 + 1).toRoman()
                        wins >= 12_500 -> "§9Celestial " + ((wins - 12_500) / 2_500 + 1).toRoman()
                        wins >= 5_000 -> "§5Godlike " + ((wins - 5_000) / 1_500 + 1).toRoman()
                        wins >= 2_500 -> "§cGrandmaster " + ((wins - 2_500) / 500 + 1).toRoman()
                        wins >= 1_000 -> "§4Legend " + ((wins - 1_000) / 300 + 1).toRoman()
                        wins >= 500 -> "§2Master " + ((wins - 500) / 100 + 1).toRoman()
                        wins >= 250 -> "§bDiamond " + ((wins - 250) / 50 + 1).toRoman()
                        wins >= 125 -> "§6Gold " + ((wins - 125) / 25 + 1).toRoman()
                        wins >= 50 -> "§fIron " + ((wins - 50) / 15 + 1).toRoman()
                        wins >= 25 -> "§7Rookie " + ((wins - 25) / 5 + 1).toRoman()
                        else -> "§8Unranked"
                    }

                    else -> when {
                        wins >= 200_000 -> "§cAscended " + ((wins - 200_000) / 20_000 + 1).toRoman()
                        wins >= 100_000 -> "§dDivine " + ((wins - 100_000) / 20_000 + 1).toRoman()
                        wins >= 50_000 -> "§9Celestial " + ((wins - 50_000) / 10_000 + 1).toRoman()
                        wins >= 20_000 -> "§5Godlike " + ((wins - 20_000) / 6_000 + 1).toRoman()
                        wins >= 10_000 -> "§cGrandmaster " + ((wins - 10_000) / 2_000 + 1).toRoman()
                        wins >= 4_000 -> "§4Legend " + ((wins - 4_000) / 1_200 + 1).toRoman()
                        wins >= 2_000 -> "§2Master " + ((wins - 2_000) / 400 + 1).toRoman()
                        wins >= 1_000 -> "§bDiamond " + ((wins - 1_000) / 200 + 1).toRoman()
                        wins >= 500 -> "§6Gold " + ((wins - 500) / 100 + 1).toRoman()
                        wins >= 200 -> "§fIron " + ((wins - 200) / 60 + 1).toRoman()
                        wins >= 100 -> "§7Rookie " + ((wins - 100) / 20 + 1).toRoman()
                        else -> "§8Unranked"
                    }
                }
            }

            division(
                duelsMode, when (duelsMode) {
                    DuelsMode.SKYWARS -> wins("sw_duel_wins") + wins("sw_doubles_wins")
                    DuelsMode.THE_BRIDGE -> wins("bridgeMapWins")
                    DuelsMode.BEDWARS -> wins("bedwars_two_one_duels_wins") + wins("bedwars_two_one_duels_rush_wins")
                    DuelsMode.CLASSIC -> wins("classic_duel_wins") + wins("classic_doubles_wins")
                    DuelsMode.UHC -> wins("uhc_duel_wins") + wins("uhc_doubles_wins") + wins("uhc_threes_wins") + wins("uhc_four_wins")
                    DuelsMode.SUMO -> wins("sumo_duel_wins")
                    DuelsMode.BOW -> wins("bow_duel_wins")
                    DuelsMode.MEGA_WALLS -> wins("mw_duel_wins")
                    DuelsMode.PARKOUR -> wins("parkour_eight_wins")
                    DuelsMode.QUAKECRAFT -> wins("quake_duel_wins")
                    DuelsMode.SPLEEF -> wins("spleef_duel_wins") + wins("bowspleef_duel_wins")
                    DuelsMode.OP -> wins("op_duel_wins") + wins("op_doubles_wins")
                    DuelsMode.BLITZ -> wins("blitz_duel_wins")
                    DuelsMode.COMBO -> wins("combo_duel_wins")
                    DuelsMode.BOXING -> wins("boxing_duel_wins")
                    DuelsMode.NO_DEBUFF -> wins("potion_duel_wins")
                    else -> wins("wins")
                }
            )
        }
    }

    private fun clearCache() {
        uuidCache.clear()
        statsCache.clear()
        pendingRequests.clear()

        pendingLevel.clear()
        pendingBedwars.clear()
        pendingSkywars.clear()
        pendingDuels.clear()
    }
}