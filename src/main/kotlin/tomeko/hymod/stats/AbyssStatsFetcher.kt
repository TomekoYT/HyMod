package tomeko.hymod.stats

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.hypixel.modapi.HypixelModAPI
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket
import net.minecraft.ChatFormatting
import net.minecraft.ChatFormatting.*
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import tomeko.hymod.location.DuelsMode
import tomeko.hymod.location.DuelsModeType
import tomeko.hymod.location.HypixelPackets
import tomeko.hymod.utils.Constants
import tomeko.hymod.utils.toRoman
import java.net.HttpURLConnection
import java.net.URI
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
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

    fun getBedwarsStars(uuid: String): CompletableFuture<Component?> {
        return getRawPlayerData(uuid).thenApply { player ->
            val stars = player?.getAsJsonObject("achievements")?.get("bedwars_level")?.asInt ?: return@thenApply null

            fun format(text: String, vararg colors: ChatFormatting): Component {
                val result = Component.empty()
                text.forEachIndexed { i, char ->
                    val color = if (i < colors.size) colors[i] else colors.last()
                    result.append(Component.literal(char.toString()).withStyle(color))
                }
                return result
            }

            when {
                stars >= 10000 -> format("[$stars✥]", BLUE, AQUA, WHITE, WHITE, WHITE, WHITE, RED, DARK_RED)
                stars >= 9900 -> format("[$stars✥]", DARK_GRAY, GRAY, WHITE, WHITE, WHITE, YELLOW, WHITE)
                stars >= 9800 -> format("[$stars✥]", BLACK, DARK_GRAY, DARK_GRAY, DARK_GRAY, DARK_GRAY, DARK_GRAY, BLACK)
                stars >= 9700 -> format("[$stars✥]", LIGHT_PURPLE, LIGHT_PURPLE, YELLOW, YELLOW, AQUA, YELLOW)
                stars >= 9600 -> format("[$stars✥]", YELLOW, YELLOW, YELLOW, BLACK, BLACK, YELLOW, BLACK)
                stars >= 9500 -> format("[$stars✥]", BLACK, BLACK, DARK_GRAY, DARK_GRAY, GRAY, GRAY, WHITE)
                stars >= 9400 -> format("[$stars✥]", YELLOW, GOLD, DARK_RED, DARK_GRAY, DARK_GRAY, DARK_GRAY, DARK_GRAY)
                stars >= 9300 -> format("[$stars✥]", WHITE, DARK_GRAY, DARK_GRAY, DARK_GRAY, DARK_GRAY, WHITE, WHITE)
                stars >= 9200 -> format("[$stars✥]", DARK_GREEN, LIGHT_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, GREEN, DARK_GREEN)
                stars >= 9100 -> format("[$stars✥]", BLACK, RED, GOLD, GOLD, RED, RED, DARK_RED)

                stars >= 9000 -> format("[$stars✥]", LIGHT_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, DARK_PURPLE, DARK_GRAY)
                stars >= 8900 -> format("[$stars✥]", BLUE, AQUA, AQUA, AQUA, DARK_AQUA, DARK_AQUA, BLUE)
                stars >= 8800 -> format("[$stars✥]", DARK_RED, DARK_RED, DARK_RED, RED, RED, WHITE, WHITE)
                stars >= 8700 -> format("[$stars✥]", DARK_GRAY, GOLD, GOLD, GOLD, GOLD, GOLD, DARK_GRAY)
                stars >= 8600 -> format("[$stars✥]", LIGHT_PURPLE, WHITE, WHITE, WHITE, WHITE, YELLOW, LIGHT_PURPLE)
                stars >= 8500 -> format("[$stars✥]", DARK_AQUA, GOLD, GOLD, GOLD, GOLD, YELLOW, DARK_AQUA)
                stars >= 8400 -> format("[$stars✥]", WHITE, LIGHT_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, GREEN, GREEN, WHITE)
                stars >= 8300 -> format("[$stars✥]", DARK_GRAY, DARK_GRAY, DARK_RED, DARK_RED, RED, RED, DARK_GRAY)
                stars >= 8200 -> format("[$stars✥]", WHITE, WHITE, WHITE, WHITE, WHITE, GREEN, WHITE)
                stars >= 8100 -> format("[$stars✥]", DARK_GRAY, GRAY, WHITE, AQUA, DARK_AQUA, BLUE, DARK_BLUE)

                stars >= 8000 -> format("[$stars✥]", DARK_GREEN, GREEN, GREEN, GREEN, RED, DARK_RED, DARK_GREEN)
                stars >= 7900 -> format("[$stars✥]", GOLD, WHITE, DARK_GREEN, GOLD, DARK_GREEN, WHITE, GOLD)
                stars >= 7800 -> format("[$stars✥]", DARK_GRAY, GRAY, WHITE, WHITE, WHITE, YELLOW, DARK_GRAY)
                stars >= 7700 -> format("[$stars✥]", LIGHT_PURPLE, RED, RED, RED, RED, GOLD, LIGHT_PURPLE)
                stars >= 7600 -> format("[$stars✥]", WHITE, WHITE, WHITE, GRAY, GRAY, RED, DARK_GRAY)
                stars >= 7500 -> format("[$stars✥]", GOLD, GOLD, DARK_GREEN, DARK_GREEN, WHITE, WHITE, WHITE)
                stars >= 7400 -> format("[$stars✥]", DARK_GRAY, DARK_GRAY, DARK_GRAY, DARK_GRAY, DARK_GRAY, LIGHT_PURPLE, DARK_GRAY)
                stars >= 7300 -> format("[$stars✥]", DARK_GREEN, DARK_AQUA, DARK_AQUA, AQUA, AQUA, GREEN, DARK_GREEN)
                stars >= 7200 -> format("[$stars✥]", DARK_GREEN, GREEN, WHITE, DARK_GREEN, GREEN, WHITE, DARK_GRAY)
                stars >= 7100 -> format("[$stars✥]", DARK_RED, RED, GOLD, YELLOW, RED, GOLD, YELLOW)

                stars >= 7000 -> format("[$stars✥]", DARK_AQUA, AQUA, AQUA, AQUA, AQUA, WHITE, DARK_AQUA)
                stars >= 6900 -> format("[$stars✥]", GREEN, GREEN, GREEN, GREEN, DARK_GREEN, DARK_GREEN, DARK_GRAY)
                stars >= 6800 -> format("[$stars✥]", BLACK, GOLD, GOLD, YELLOW, YELLOW, WHITE, WHITE)
                stars >= 6700 -> format("[$stars✥]", DARK_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, WHITE, DARK_PURPLE)
                stars >= 6600 -> format("[$stars✥]", BLUE, LIGHT_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, AQUA, BLUE)
                stars >= 6500 -> format("[$stars✥]", DARK_AQUA, DARK_AQUA, GREEN, GREEN, WHITE, GREEN, DARK_AQUA)
                stars >= 6400 -> format("[$stars✥]", AQUA, AQUA, RED, RED, RED, GREEN, GREEN)
                stars >= 6300 -> format("[$stars✥]", GREEN, YELLOW, YELLOW, YELLOW, YELLOW, GREEN, DARK_GREEN)
                stars >= 6200 -> format("[$stars✥]", YELLOW, WHITE, YELLOW, GOLD, GOLD, WHITE, YELLOW)
                stars >= 6100 -> format("[$stars✥]", GOLD, YELLOW, WHITE, WHITE, WHITE, AQUA, DARK_AQUA)

                stars >= 6000 -> format("[$stars✥]", RED, WHITE, WHITE, WHITE, WHITE, RED, WHITE)
                stars >= 5900 -> format("[$stars✥]", GRAY, BLACK, DARK_GRAY, GRAY, WHITE, WHITE, GRAY)
                stars >= 5800 -> format("[$stars✥]", DARK_PURPLE, RED, GOLD, WHITE, AQUA, DARK_AQUA, BLUE)
                stars >= 5700 -> format("[$stars✥]", DARK_RED, GOLD, DARK_GREEN, DARK_AQUA, BLUE, DARK_PURPLE, DARK_GRAY)
                stars >= 5600 -> format("[$stars✥]", DARK_RED, RED, YELLOW, WHITE, YELLOW, RED, DARK_RED)
                stars >= 5500 -> format("[$stars✥]", DARK_GREEN, GREEN, YELLOW, WHITE, AQUA, LIGHT_PURPLE, DARK_PURPLE)
                stars >= 5400 -> format("[$stars✥]", DARK_AQUA, GREEN, DARK_GREEN, DARK_GRAY, DARK_GREEN, GREEN, DARK_AQUA)
                stars >= 5300 -> format("[$stars✥]", DARK_PURPLE, LIGHT_PURPLE, YELLOW, WHITE, YELLOW, LIGHT_PURPLE, DARK_PURPLE)
                stars >= 5200 -> format("[$stars✥]", DARK_BLUE, BLUE, DARK_AQUA, AQUA, WHITE, YELLOW, DARK_BLUE)
                stars >= 5100 -> format("[$stars✥]", DARK_RED, RED, RED, GOLD, YELLOW, WHITE, DARK_RED)

                stars >= 5000 -> format("[$stars✥]", DARK_RED, DARK_RED, DARK_PURPLE, BLUE, BLUE, DARK_BLUE, BLACK)
                stars >= 4900 -> format("[$stars✥]", DARK_GREEN, GREEN, WHITE, WHITE, WHITE, GREEN, DARK_GREEN)
                stars >= 4800 -> format("[$stars✥]", DARK_PURPLE, DARK_PURPLE, RED, GOLD, GOLD, AQUA, DARK_AQUA)
                stars >= 4700 -> format("[$stars✥]", WHITE, DARK_RED, RED, RED, BLUE, DARK_BLUE, BLUE)
                stars >= 4600 -> format("[$stars✥]", DARK_AQUA, AQUA, YELLOW, YELLOW, GOLD, LIGHT_PURPLE, DARK_PURPLE)
                stars >= 4500 -> format("[$stars✥]", WHITE, WHITE, AQUA, AQUA, DARK_AQUA, DARK_AQUA, DARK_AQUA)
                stars >= 4400 -> format("[$stars✥]", DARK_GREEN, DARK_GREEN, GREEN, YELLOW, GOLD, DARK_PURPLE, LIGHT_PURPLE)
                stars >= 4300 -> format("[$stars✥]", BLACK, DARK_PURPLE, DARK_GRAY, DARK_GRAY, DARK_PURPLE, DARK_PURPLE, BLACK)
                stars >= 4200 -> format("[$stars✥]", DARK_BLUE, BLUE, DARK_AQUA, AQUA, WHITE, GRAY, GRAY)
                stars >= 4100 -> format("[$stars✥]", YELLOW, YELLOW, GOLD, RED, LIGHT_PURPLE, LIGHT_PURPLE, DARK_PURPLE)

                stars >= 4000 -> format("[$stars✥]", DARK_PURPLE, DARK_PURPLE, RED, RED, GOLD, GOLD, YELLOW)
                stars >= 3900 -> format("[$stars✥]", RED, RED, GREEN, GREEN, DARK_AQUA, BLUE, BLUE)
                stars >= 3800 -> format("[$stars✥]", DARK_BLUE, DARK_BLUE, BLUE, DARK_PURPLE, DARK_PURPLE, LIGHT_PURPLE, DARK_BLUE)
                stars >= 3700 -> format("[$stars✥]", DARK_RED, DARK_RED, RED, RED, AQUA, DARK_AQUA, DARK_AQUA)
                stars >= 3600 -> format("[$stars✥]", GREEN, GREEN, GREEN, AQUA, BLUE, BLUE, DARK_BLUE)
                stars >= 3500 -> format("[$stars✥]", RED, RED, DARK_RED, DARK_RED, DARK_GREEN, GREEN, GREEN)
                stars >= 3400 -> format("[$stars✥]", DARK_GREEN, GREEN, LIGHT_PURPLE, LIGHT_PURPLE, DARK_PURPLE, DARK_PURPLE, DARK_GREEN)
                stars >= 3300 -> format("[$stars✥]", BLUE, BLUE, BLUE, LIGHT_PURPLE, RED, RED, DARK_RED)
                stars >= 3200 -> format("[$stars✥]", RED, DARK_RED, GRAY, GRAY, DARK_RED, RED, RED)
                stars >= 3100 -> format("[$stars✥]", BLUE, BLUE, DARK_AQUA, DARK_AQUA, GOLD, GOLD, YELLOW)

                stars >= 3000 -> format("[$stars⚝]", YELLOW, YELLOW, GOLD, GOLD, RED, RED, DARK_RED)
                stars >= 2900 -> format("[$stars⚝]", AQUA, AQUA, DARK_AQUA, DARK_AQUA, BLUE, BLUE, DARK_BLUE)
                stars >= 2800 -> format("[$stars⚝]", GREEN, GREEN, DARK_GREEN, DARK_GREEN, GOLD, GOLD, YELLOW)
                stars >= 2700 -> format("[$stars⚝]", YELLOW, YELLOW, WHITE, WHITE, DARK_GRAY, DARK_GRAY, DARK_GRAY)
                stars >= 2600 -> format("[$stars⚝]", DARK_RED, DARK_RED, RED, RED, LIGHT_PURPLE, LIGHT_PURPLE, DARK_PURPLE)
                stars >= 2500 -> format("[$stars⚝]", GRAY, GRAY, GREEN, GREEN, DARK_GREEN, DARK_GREEN, DARK_GREEN)
                stars >= 2400 -> format("[$stars⚝]", AQUA, AQUA, WHITE, WHITE, GRAY, GRAY, DARK_GRAY)
                stars >= 2300 -> format("[$stars⚝]", DARK_PURPLE, DARK_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, GOLD, YELLOW, YELLOW)
                stars >= 2200 -> format("[$stars⚝]", GOLD, GOLD, WHITE, WHITE, AQUA, DARK_AQUA, DARK_AQUA)
                stars >= 2100 -> format("[$stars⚝]", GRAY, GRAY, YELLOW, YELLOW, GOLD, GOLD, GOLD)

                stars >= 2000 -> format("[$stars✪]", DARK_GRAY, GRAY, WHITE, WHITE, GRAY, GRAY, DARK_GRAY)
                stars >= 1900 -> format("[$stars✪]", GRAY, DARK_PURPLE, DARK_PURPLE, DARK_PURPLE, DARK_PURPLE, DARK_GRAY, GRAY)
                stars >= 1800 -> format("[$stars✪]", GRAY, BLUE, BLUE, BLUE, BLUE, DARK_BLUE, GRAY)
                stars >= 1700 -> format("[$stars✪]", GRAY, LIGHT_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, LIGHT_PURPLE, DARK_PURPLE, GRAY)
                stars >= 1600 -> format("[$stars✪]", GRAY, RED, RED, RED, RED, DARK_RED, GRAY)
                stars >= 1500 -> format("[$stars✪]", GRAY, DARK_AQUA, DARK_AQUA, DARK_AQUA, DARK_AQUA, BLUE, GRAY)
                stars >= 1400 -> format("[$stars✪]", GRAY, GREEN, GREEN, GREEN, GREEN, DARK_GREEN, GRAY)
                stars >= 1300 -> format("[$stars✪]", GRAY, AQUA, AQUA, AQUA, AQUA, DARK_AQUA, GRAY)
                stars >= 1200 -> format("[$stars✪]", GRAY, YELLOW, YELLOW, YELLOW, YELLOW, GOLD, GRAY)
                stars >= 1100 -> format("[$stars✪]", GRAY, WHITE, WHITE, WHITE, WHITE, GRAY, GRAY)

                stars >= 1000 -> format("[$stars✫]", RED, GOLD, YELLOW, GREEN, AQUA, LIGHT_PURPLE, DARK_PURPLE)
                stars >= 900 -> format("[$stars✫]", DARK_PURPLE)
                stars >= 800 -> format("[$stars✫]", BLUE)
                stars >= 700 -> format("[$stars✫]", LIGHT_PURPLE)
                stars >= 600 -> format("[$stars✫]", DARK_RED)
                stars >= 500 -> format("[$stars✫]", DARK_AQUA)
                stars >= 400 -> format("[$stars✫]", DARK_GREEN)
                stars >= 300 -> format("[$stars✫]", AQUA)
                stars >= 200 -> format("[$stars✫]", GOLD)
                stars >= 100 -> format("[$stars✫]", WHITE)

                else -> format("[$stars✫]", GRAY)
            }
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