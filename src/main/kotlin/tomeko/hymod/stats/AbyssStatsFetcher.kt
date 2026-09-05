package tomeko.hymod.stats

import com.google.gson.JsonObject
import com.google.gson.JsonParser
//? if = 1.8.9-forge {
/*import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.minecraft.util.EnumChatFormatting as ChatFormatting
import net.minecraft.util.EnumChatFormatting.*
import net.minecraft.util.IChatComponent as Component
*///?} else {
import net.minecraft.ChatFormatting
import net.minecraft.ChatFormatting.*
import net.minecraft.network.chat.Component
//?}
import tomeko.hymod.config.HyModConfig
import tomeko.hymod.location.DuelsMode
import tomeko.hymod.location.DuelsModeType
import tomeko.hymod.location.HypixelPackets
import tomeko.hymod.utils.Constants
import tomeko.hymod.utils.Debug
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
    private data class CachedRaw(
        val fetchedAt: Long,
        val json: JsonObject?
    )

    private const val ABYSS_PLAYER_ENDPOINT = "http://api.abyssoverlay.com/player?uuid="
    private const val ABYSS_USER_AGENT = "node-ao/2.0.3"

    private const val CONNECT_TIMEOUT_MS = 3000
    private const val READ_TIMEOUT_MS = 3000

    private const val NETWORK_POOL_SIZE = 4
    private val networkThreadCounter = AtomicInteger(0)
    private val networkThreadFactory = ThreadFactory { runnable ->
        Thread(runnable, "${Constants.MOD_ID}-stats-fetch-${networkThreadCounter.incrementAndGet()}").apply {
            isDaemon = true
        }
    }
    private val networkExecutor = ThreadPoolExecutor(
        NETWORK_POOL_SIZE,
        NETWORK_POOL_SIZE,
        30L,
        TimeUnit.SECONDS,
        LinkedBlockingQueue(),
        networkThreadFactory
    ).apply { allowCoreThreadTimeOut(true) }

    private val statsCache = ConcurrentHashMap<String, CachedRaw>()
    private val pendingRequests = ConcurrentHashMap<String, CompletableFuture<JsonObject?>>()
    private val rateLimitedUntil = ConcurrentHashMap<String, Long>()

    val nickedPlayers = ConcurrentHashMap.newKeySet<String>()

    private const val CACHE_TTL_MS = 120_000L
    private const val FAILURE_TTL_MS = 15_000L

    private fun getRawPlayerData(uuid: String): CompletableFuture<JsonObject?> {
        val now = System.currentTimeMillis()

        val cached = statsCache[uuid]

        if (cached != null) {
            val ttl = if (cached.json != null) CACHE_TTL_MS else FAILURE_TTL_MS

            if (now - cached.fetchedAt < ttl) {
                return CompletableFuture.completedFuture(cached.json)
            }

            statsCache.remove(uuid, cached)
        }

        val limitedUntil = rateLimitedUntil[uuid]
        if (limitedUntil != null) {
            if (now < limitedUntil) {
                return CompletableFuture.completedFuture(null)
            }

            rateLimitedUntil.remove(uuid, limitedUntil)
        }

        return pendingRequests.computeIfAbsent(uuid) {
            CompletableFuture.supplyAsync({
                try {
                    Debug.log("Fetching Abyss player data for $uuid")

                    val connection =
                        URI.create(ABYSS_PLAYER_ENDPOINT + uuid).toURL().openConnection() as HttpURLConnection

                    connection.requestMethod = "GET"
                    connection.connectTimeout = CONNECT_TIMEOUT_MS
                    connection.readTimeout = READ_TIMEOUT_MS
                    connection.setRequestProperty("User-Agent", ABYSS_USER_AGENT)
                    connection.setRequestProperty("Accept", "application/json")

                    val responseCode = connection.responseCode

                    if (responseCode == 429) {
                        val retryAfter = connection.getHeaderField("Retry-After")
                        val retrySeconds = retryAfter?.toLongOrNull() ?: 60L
                        val retryUntil = System.currentTimeMillis() + retrySeconds * 1000L

                        rateLimitedUntil[uuid] = retryUntil

                        Debug.log("Abyss API rate limited request for $uuid (HTTP 429), retrying after ${retrySeconds}s")
                        null
                    } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                        nickedPlayers.add(uuid)

                        Debug.log("Abyss API request not found for $uuid")
                        null
                    } else if (responseCode != HttpURLConnection.HTTP_OK) {
                        Debug.log("Abyss API request failed for $uuid (HTTP $responseCode)")
                        null
                    } else {
                        val body = connection.inputStream.bufferedReader().use { it.readText() }
                        val root =
                        //? if = 1.8.9-forge {
                                /*JsonParser().parse(body).asJsonObject
                                *///?} else {
                            JsonParser.parseString(body).asJsonObject
                        //?}
                        val result = root.getAsJsonObject("player")

                        Debug.log("Abyss API request succeeded for $uuid")
                        result
                    }
                } catch (e: Exception) {
                    Debug.log("Abyss API request failed for $uuid: ${e::class.simpleName}: ${e.message}")
                    null
                }
            }, networkExecutor).whenComplete { result, _ ->
                statsCache[uuid] = CachedRaw(System.currentTimeMillis(), result)
                pendingRequests.remove(uuid)
            }
        }
    }

    data class CachedStats(
        val level: String? = null,
        val bedwars: Component? = null,
        val skywars: Component? = null,
        val duels: Component? = null,
        val duelsMode: DuelsMode? = null
    )

    private val displayCache = ConcurrentHashMap<String, CachedStats>()

    private val pendingLevel: ConcurrentHashMap.KeySetView<String, Boolean> = ConcurrentHashMap.newKeySet()
    private val pendingBedwars: ConcurrentHashMap.KeySetView<String, Boolean> = ConcurrentHashMap.newKeySet()
    private val pendingSkywars: ConcurrentHashMap.KeySetView<String, Boolean> = ConcurrentHashMap.newKeySet()
    private val pendingDuels = ConcurrentHashMap.newKeySet<String>()

    fun getCachedStats(uuid: String): CachedStats {
        val cached = displayCache[uuid] ?: return CachedStats()

        if (cached.duelsMode != null && HypixelPackets.inDuels && cached.duelsMode != HypixelPackets.duelsMode)
            return cached.copy(duels = null, duelsMode = null)

        return cached
    }

    fun requestStats(uuid: String) {
        if (!HypixelPackets.onHypixel) return

        val wantsBedwars = HypixelPackets.inBedwars &&
                (HyModConfig.showBedwarsStarsAboveNametag || HyModConfig.showBedwarsStarsInTablist)
        val wantsSkywars = HypixelPackets.inSkywars &&
                (HyModConfig.showSkywarsStarsAboveNametag || HyModConfig.showSkywarsStarsInTablist)
        val wantsDuels = HypixelPackets.inDuels &&
                (HyModConfig.showDuelsDivisionAboveNametag || HyModConfig.showDuelsDivisionInTablist)

        val shouldCheckNetworkLevel = wantsBedwars || wantsSkywars || wantsDuels
        val wantsLevel =
            HyModConfig.showNetworkLevelAboveNametag && (!shouldCheckNetworkLevel || HyModConfig.showNetworkLevelWithOtherNametagStats)

        if (wantsBedwars && pendingBedwars.add(uuid)) {
            getBedwarsStars(uuid)
                .thenAccept { component ->
                    if (component != null) {
                        displayCache.compute(uuid) { _, old -> (old ?: CachedStats()).copy(bedwars = component) }
                    }
                }
                .whenComplete { _, _ -> pendingBedwars.remove(uuid) }
        }

        if (wantsSkywars && pendingSkywars.add(uuid)) {
            getSkywarsStars(uuid)
                .thenAccept { component ->
                    if (component != null) {
                        displayCache.compute(uuid) { _, old -> (old ?: CachedStats()).copy(skywars = component) }
                    }
                }
                .whenComplete { _, _ -> pendingSkywars.remove(uuid) }
        }

        if (wantsDuels) {
            val duelsMode = HypixelPackets.duelsMode
            val pendingKey = "$uuid:$duelsMode"

            if (pendingDuels.add(pendingKey)) {
                getDuelsDivision(uuid, duelsMode)
                    .thenAccept { component ->
                        if (component != null && HypixelPackets.inDuels && HypixelPackets.duelsMode == duelsMode) {
                            displayCache.compute(uuid) { _, old ->
                                (old ?: CachedStats()).copy(duels = component, duelsMode = duelsMode)
                            }
                        }
                    }
                    .whenComplete { _, _ ->
                        pendingDuels.remove(pendingKey)
                    }
            }
        }

        if (wantsLevel && pendingLevel.add(uuid)) {
            getHypixelLevel(uuid)
                .thenAccept { string ->
                    if (string != null) {
                        displayCache.compute(uuid) { _, old -> (old ?: CachedStats()).copy(level = string) }
                    }
                }
                .whenComplete { _, _ -> pendingLevel.remove(uuid) }
        }
    }

    private fun getHypixelLevel(uuid: String): CompletableFuture<String?> {
        return getRawPlayerData(uuid).thenApply { player ->
            val exp = player?.get("networkExp")?.asDouble ?: return@thenApply null

            ((sqrt(exp + 15312.5) - 88.38834764831844) / 35.35533905932738).toInt().toString()
        }
    }

    private fun getBedwarsStars(uuid: String): CompletableFuture<Component?> {
        return getRawPlayerData(uuid).thenApply { player ->
            val stars = player?.getAsJsonObject("achievements")?.get("bedwars_level")?.asInt ?: return@thenApply null

            when {
                stars >= 10000 -> formatStars("[$stars✥]", BLUE, AQUA, WHITE, WHITE, WHITE, WHITE, RED, DARK_RED)
                stars >= 9900 -> formatStars("[$stars✥]", DARK_GRAY, GRAY, WHITE, WHITE, WHITE, YELLOW, WHITE)
                stars >= 9800 -> formatStars(
                    "[$stars✥]",
                    BLACK,
                    DARK_GRAY,
                    DARK_GRAY,
                    DARK_GRAY,
                    DARK_GRAY,
                    DARK_GRAY,
                    BLACK
                )

                stars >= 9700 -> formatStars("[$stars✥]", LIGHT_PURPLE, LIGHT_PURPLE, YELLOW, YELLOW, AQUA, YELLOW)
                stars >= 9600 -> formatStars("[$stars✥]", YELLOW, YELLOW, YELLOW, BLACK, BLACK, YELLOW, BLACK)
                stars >= 9500 -> formatStars("[$stars✥]", BLACK, BLACK, DARK_GRAY, DARK_GRAY, GRAY, GRAY, WHITE)
                stars >= 9400 -> formatStars(
                    "[$stars✥]",
                    YELLOW,
                    GOLD,
                    DARK_RED,
                    DARK_GRAY,
                    DARK_GRAY,
                    DARK_GRAY,
                    DARK_GRAY
                )

                stars >= 9300 -> formatStars(
                    "[$stars✥]",
                    WHITE,
                    DARK_GRAY,
                    DARK_GRAY,
                    DARK_GRAY,
                    DARK_GRAY,
                    WHITE,
                    WHITE
                )

                stars >= 9200 -> formatStars(
                    "[$stars✥]",
                    DARK_GREEN,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    GREEN,
                    DARK_GREEN
                )

                stars >= 9100 -> formatStars("[$stars✥]", BLACK, RED, GOLD, GOLD, RED, RED, DARK_RED)

                stars >= 9000 -> formatStars(
                    "[$stars✥]",
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    DARK_PURPLE,
                    DARK_GRAY
                )

                stars >= 8900 -> formatStars("[$stars✥]", BLUE, AQUA, AQUA, AQUA, DARK_AQUA, DARK_AQUA, BLUE)
                stars >= 8800 -> formatStars("[$stars✥]", DARK_RED, DARK_RED, DARK_RED, RED, RED, WHITE, WHITE)
                stars >= 8700 -> formatStars("[$stars✥]", DARK_GRAY, GOLD, GOLD, GOLD, GOLD, GOLD, DARK_GRAY)
                stars >= 8600 -> formatStars(
                    "[$stars✥]",
                    LIGHT_PURPLE,
                    WHITE,
                    WHITE,
                    WHITE,
                    WHITE,
                    YELLOW,
                    LIGHT_PURPLE
                )

                stars >= 8500 -> formatStars("[$stars✥]", DARK_AQUA, GOLD, GOLD, GOLD, GOLD, YELLOW, DARK_AQUA)
                stars >= 8400 -> formatStars(
                    "[$stars✥]",
                    WHITE,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    GREEN,
                    GREEN,
                    WHITE
                )

                stars >= 8300 -> formatStars("[$stars✥]", DARK_GRAY, DARK_GRAY, DARK_RED, DARK_RED, RED, RED, DARK_GRAY)
                stars >= 8200 -> formatStars("[$stars✥]", WHITE, WHITE, WHITE, WHITE, WHITE, GREEN, WHITE)
                stars >= 8100 -> formatStars("[$stars✥]", DARK_GRAY, GRAY, WHITE, AQUA, DARK_AQUA, BLUE, DARK_BLUE)

                stars >= 8000 -> formatStars("[$stars✥]", DARK_GREEN, GREEN, GREEN, GREEN, RED, DARK_RED, DARK_GREEN)
                stars >= 7900 -> formatStars("[$stars✥]", GOLD, WHITE, DARK_GREEN, GOLD, DARK_GREEN, WHITE, GOLD)
                stars >= 7800 -> formatStars("[$stars✥]", DARK_GRAY, GRAY, WHITE, WHITE, WHITE, YELLOW, DARK_GRAY)
                stars >= 7700 -> formatStars("[$stars✥]", LIGHT_PURPLE, RED, RED, RED, RED, GOLD, LIGHT_PURPLE)
                stars >= 7600 -> formatStars("[$stars✥]", WHITE, WHITE, WHITE, GRAY, GRAY, RED, DARK_GRAY)
                stars >= 7500 -> formatStars("[$stars✥]", GOLD, GOLD, DARK_GREEN, DARK_GREEN, WHITE, WHITE, WHITE)
                stars >= 7400 -> formatStars(
                    "[$stars✥]",
                    DARK_GRAY,
                    DARK_GRAY,
                    DARK_GRAY,
                    DARK_GRAY,
                    DARK_GRAY,
                    LIGHT_PURPLE,
                    DARK_GRAY
                )

                stars >= 7300 -> formatStars(
                    "[$stars✥]",
                    DARK_GREEN,
                    DARK_AQUA,
                    DARK_AQUA,
                    AQUA,
                    AQUA,
                    GREEN,
                    DARK_GREEN
                )

                stars >= 7200 -> formatStars("[$stars✥]", DARK_GREEN, GREEN, WHITE, DARK_GREEN, GREEN, WHITE, DARK_GRAY)
                stars >= 7100 -> formatStars("[$stars✥]", DARK_RED, RED, GOLD, YELLOW, RED, GOLD, YELLOW)

                stars >= 7000 -> formatStars("[$stars✥]", DARK_AQUA, AQUA, AQUA, AQUA, AQUA, WHITE, DARK_AQUA)
                stars >= 6900 -> formatStars("[$stars✥]", GREEN, GREEN, GREEN, GREEN, DARK_GREEN, DARK_GREEN, DARK_GRAY)
                stars >= 6800 -> formatStars("[$stars✥]", BLACK, GOLD, GOLD, YELLOW, YELLOW, WHITE, WHITE)
                stars >= 6700 -> formatStars(
                    "[$stars✥]",
                    DARK_PURPLE,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    WHITE,
                    DARK_PURPLE
                )

                stars >= 6600 -> formatStars(
                    "[$stars✥]",
                    BLUE,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    AQUA,
                    BLUE
                )

                stars >= 6500 -> formatStars("[$stars✥]", DARK_AQUA, DARK_AQUA, GREEN, GREEN, WHITE, GREEN, DARK_AQUA)
                stars >= 6400 -> formatStars("[$stars✥]", AQUA, AQUA, RED, RED, RED, GREEN, GREEN)
                stars >= 6300 -> formatStars("[$stars✥]", GREEN, YELLOW, YELLOW, YELLOW, YELLOW, GREEN, DARK_GREEN)
                stars >= 6200 -> formatStars("[$stars✥]", YELLOW, WHITE, YELLOW, GOLD, GOLD, WHITE, YELLOW)
                stars >= 6100 -> formatStars("[$stars✥]", GOLD, YELLOW, WHITE, WHITE, WHITE, AQUA, DARK_AQUA)

                stars >= 6000 -> formatStars("[$stars✥]", RED, WHITE, WHITE, WHITE, WHITE, RED, WHITE)
                stars >= 5900 -> formatStars("[$stars✥]", GRAY, BLACK, DARK_GRAY, GRAY, WHITE, WHITE, GRAY)
                stars >= 5800 -> formatStars("[$stars✥]", DARK_PURPLE, RED, GOLD, WHITE, AQUA, DARK_AQUA, BLUE)
                stars >= 5700 -> formatStars(
                    "[$stars✥]",
                    DARK_RED,
                    GOLD,
                    DARK_GREEN,
                    DARK_AQUA,
                    BLUE,
                    DARK_PURPLE,
                    DARK_GRAY
                )

                stars >= 5600 -> formatStars("[$stars✥]", DARK_RED, RED, YELLOW, WHITE, YELLOW, RED, DARK_RED)
                stars >= 5500 -> formatStars(
                    "[$stars✥]",
                    DARK_GREEN,
                    GREEN,
                    YELLOW,
                    WHITE,
                    AQUA,
                    LIGHT_PURPLE,
                    DARK_PURPLE
                )

                stars >= 5400 -> formatStars(
                    "[$stars✥]",
                    DARK_AQUA,
                    GREEN,
                    DARK_GREEN,
                    DARK_GRAY,
                    DARK_GREEN,
                    GREEN,
                    DARK_AQUA
                )

                stars >= 5300 -> formatStars(
                    "[$stars✥]",
                    DARK_PURPLE,
                    LIGHT_PURPLE,
                    YELLOW,
                    WHITE,
                    YELLOW,
                    LIGHT_PURPLE,
                    DARK_PURPLE
                )

                stars >= 5200 -> formatStars("[$stars✥]", DARK_BLUE, BLUE, DARK_AQUA, AQUA, WHITE, YELLOW, DARK_BLUE)
                stars >= 5100 -> formatStars("[$stars✥]", DARK_RED, RED, RED, GOLD, YELLOW, WHITE, DARK_RED)

                stars >= 5000 -> formatStars("[$stars✥]", DARK_RED, DARK_RED, DARK_PURPLE, BLUE, BLUE, DARK_BLUE, BLACK)
                stars >= 4900 -> formatStars("[$stars✥]", DARK_GREEN, GREEN, WHITE, WHITE, WHITE, GREEN, DARK_GREEN)
                stars >= 4800 -> formatStars("[$stars✥]", DARK_PURPLE, DARK_PURPLE, RED, GOLD, GOLD, AQUA, DARK_AQUA)
                stars >= 4700 -> formatStars("[$stars✥]", WHITE, DARK_RED, RED, RED, BLUE, DARK_BLUE, BLUE)
                stars >= 4600 -> formatStars(
                    "[$stars✥]",
                    DARK_AQUA,
                    AQUA,
                    YELLOW,
                    YELLOW,
                    GOLD,
                    LIGHT_PURPLE,
                    DARK_PURPLE
                )

                stars >= 4500 -> formatStars("[$stars✥]", WHITE, WHITE, AQUA, AQUA, DARK_AQUA, DARK_AQUA, DARK_AQUA)
                stars >= 4400 -> formatStars(
                    "[$stars✥]",
                    DARK_GREEN,
                    DARK_GREEN,
                    GREEN,
                    YELLOW,
                    GOLD,
                    DARK_PURPLE,
                    LIGHT_PURPLE
                )

                stars >= 4300 -> formatStars(
                    "[$stars✥]",
                    BLACK,
                    DARK_PURPLE,
                    DARK_GRAY,
                    DARK_GRAY,
                    DARK_PURPLE,
                    DARK_PURPLE,
                    BLACK
                )

                stars >= 4200 -> formatStars("[$stars✥]", DARK_BLUE, BLUE, DARK_AQUA, AQUA, WHITE, GRAY, GRAY)
                stars >= 4100 -> formatStars(
                    "[$stars✥]",
                    YELLOW,
                    YELLOW,
                    GOLD,
                    RED,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    DARK_PURPLE
                )

                stars >= 4000 -> formatStars("[$stars✥]", DARK_PURPLE, DARK_PURPLE, RED, RED, GOLD, GOLD, YELLOW)
                stars >= 3900 -> formatStars("[$stars✥]", RED, RED, GREEN, GREEN, DARK_AQUA, BLUE, BLUE)
                stars >= 3800 -> formatStars(
                    "[$stars✥]",
                    DARK_BLUE,
                    DARK_BLUE,
                    BLUE,
                    DARK_PURPLE,
                    DARK_PURPLE,
                    LIGHT_PURPLE,
                    DARK_BLUE
                )

                stars >= 3700 -> formatStars("[$stars✥]", DARK_RED, DARK_RED, RED, RED, AQUA, DARK_AQUA, DARK_AQUA)
                stars >= 3600 -> formatStars("[$stars✥]", GREEN, GREEN, GREEN, AQUA, BLUE, BLUE, DARK_BLUE)
                stars >= 3500 -> formatStars("[$stars✥]", RED, RED, DARK_RED, DARK_RED, DARK_GREEN, GREEN, GREEN)
                stars >= 3400 -> formatStars(
                    "[$stars✥]",
                    DARK_GREEN,
                    GREEN,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    DARK_PURPLE,
                    DARK_PURPLE,
                    DARK_GREEN
                )

                stars >= 3300 -> formatStars("[$stars✥]", BLUE, BLUE, BLUE, LIGHT_PURPLE, RED, RED, DARK_RED)
                stars >= 3200 -> formatStars("[$stars✥]", RED, DARK_RED, GRAY, GRAY, DARK_RED, RED, RED)
                stars >= 3100 -> formatStars("[$stars✥]", BLUE, BLUE, DARK_AQUA, DARK_AQUA, GOLD, GOLD, YELLOW)

                stars >= 3000 -> formatStars("[$stars⚝]", YELLOW, YELLOW, GOLD, GOLD, RED, RED, DARK_RED)
                stars >= 2900 -> formatStars("[$stars⚝]", AQUA, AQUA, DARK_AQUA, DARK_AQUA, BLUE, BLUE, DARK_BLUE)
                stars >= 2800 -> formatStars("[$stars⚝]", GREEN, GREEN, DARK_GREEN, DARK_GREEN, GOLD, GOLD, YELLOW)
                stars >= 2700 -> formatStars("[$stars⚝]", YELLOW, YELLOW, WHITE, WHITE, DARK_GRAY, DARK_GRAY, DARK_GRAY)
                stars >= 2600 -> formatStars(
                    "[$stars⚝]",
                    DARK_RED,
                    DARK_RED,
                    RED,
                    RED,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    DARK_PURPLE
                )

                stars >= 2500 -> formatStars("[$stars⚝]", GRAY, GRAY, GREEN, GREEN, DARK_GREEN, DARK_GREEN, DARK_GREEN)
                stars >= 2400 -> formatStars("[$stars⚝]", AQUA, AQUA, WHITE, WHITE, GRAY, GRAY, DARK_GRAY)
                stars >= 2300 -> formatStars(
                    "[$stars⚝]",
                    DARK_PURPLE,
                    DARK_PURPLE,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    GOLD,
                    YELLOW,
                    YELLOW
                )

                stars >= 2200 -> formatStars("[$stars⚝]", GOLD, GOLD, WHITE, WHITE, AQUA, DARK_AQUA, DARK_AQUA)
                stars >= 2100 -> formatStars("[$stars⚝]", GRAY, GRAY, YELLOW, YELLOW, GOLD, GOLD, GOLD)

                stars >= 2000 -> formatStars("[$stars✪]", DARK_GRAY, GRAY, WHITE, WHITE, GRAY, GRAY, DARK_GRAY)
                stars >= 1900 -> formatStars(
                    "[$stars✪]",
                    GRAY,
                    DARK_PURPLE,
                    DARK_PURPLE,
                    DARK_PURPLE,
                    DARK_PURPLE,
                    DARK_GRAY,
                    GRAY
                )

                stars >= 1800 -> formatStars("[$stars✪]", GRAY, BLUE, BLUE, BLUE, BLUE, DARK_BLUE, GRAY)
                stars >= 1700 -> formatStars(
                    "[$stars✪]",
                    GRAY,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    DARK_PURPLE,
                    GRAY
                )

                stars >= 1600 -> formatStars("[$stars✪]", GRAY, RED, RED, RED, RED, DARK_RED, GRAY)
                stars >= 1500 -> formatStars("[$stars✪]", GRAY, DARK_AQUA, DARK_AQUA, DARK_AQUA, DARK_AQUA, BLUE, GRAY)
                stars >= 1400 -> formatStars("[$stars✪]", GRAY, GREEN, GREEN, GREEN, GREEN, DARK_GREEN, GRAY)
                stars >= 1300 -> formatStars("[$stars✪]", GRAY, AQUA, AQUA, AQUA, AQUA, DARK_AQUA, GRAY)
                stars >= 1200 -> formatStars("[$stars✪]", GRAY, YELLOW, YELLOW, YELLOW, YELLOW, GOLD, GRAY)
                stars >= 1100 -> formatStars("[$stars✪]", GRAY, WHITE, WHITE, WHITE, WHITE, GRAY, GRAY)

                stars >= 1000 -> formatStars("[$stars✫]", RED, GOLD, YELLOW, GREEN, AQUA, LIGHT_PURPLE, DARK_PURPLE)
                stars >= 900 -> formatStars("[$stars✫]", DARK_PURPLE)
                stars >= 800 -> formatStars("[$stars✫]", BLUE)
                stars >= 700 -> formatStars("[$stars✫]", LIGHT_PURPLE)
                stars >= 600 -> formatStars("[$stars✫]", DARK_RED)
                stars >= 500 -> formatStars("[$stars✫]", DARK_AQUA)
                stars >= 400 -> formatStars("[$stars✫]", DARK_GREEN)
                stars >= 300 -> formatStars("[$stars✫]", AQUA)
                stars >= 200 -> formatStars("[$stars✫]", GOLD)
                stars >= 100 -> formatStars("[$stars✫]", WHITE)

                else -> formatStars("[$stars✫]", GRAY)
            }
        }
    }

    private fun getSkywarsStars(uuid: String): CompletableFuture<Component?> {
        return getRawPlayerData(uuid).thenApply { player ->
            val exp = player?.getAsJsonObject("stats")?.getAsJsonObject("SkyWars")?.get("skywars_experience")?.asLong
                ?: return@thenApply null

            val stars = when {
                exp >= 31800 -> 19 + (exp - 26800) / 5000
                exp >= 26800 -> 19
                exp >= 22300 -> 18
                exp >= 18300 -> 17
                exp >= 14800 -> 16
                exp >= 11800 -> 15
                exp >= 9300 -> 14
                exp >= 7300 -> 13
                exp >= 5550 -> 12
                exp >= 4000 -> 11
                exp >= 2750 -> 10
                exp >= 1750 -> 9
                exp >= 1000 -> 8
                exp >= 500 -> 7
                exp >= 250 -> 6
                exp >= 125 -> 5
                exp >= 75 -> 4
                exp >= 35 -> 3
                exp >= 10 -> 2
                else -> 1
            }

            when {
                stars >= 500 -> formatStarsObfuscated("[$stars✯]", AQUA, LIGHT_PURPLE, RED, GOLD, YELLOW, GREEN)
                stars >= 490 -> formatStars("[$stars✯]", BLUE, AQUA, WHITE, WHITE, RED, DARK_RED)
                stars >= 480 -> formatStars("[$stars✯]", DARK_BLUE, DARK_BLUE, BLUE, DARK_AQUA, AQUA, WHITE)
                stars >= 470 -> formatStars("[$stars✯]", BLACK, DARK_GRAY, GRAY, WHITE, GRAY, DARK_GRAY)
                stars >= 460 -> formatStars("[$stars✯]", BLUE, AQUA, DARK_AQUA, LIGHT_PURPLE, DARK_RED, DARK_RED)
                stars >= 450 -> formatStars("[$stars✯]", DARK_RED, DARK_RED, RED, GOLD, YELLOW, WHITE)
                stars >= 440 -> formatStars("[$stars✯]", GREEN, DARK_GREEN, GREEN, YELLOW, WHITE, WHITE)
                stars >= 430 -> formatStars("[$stars✯]", GOLD, GOLD, WHITE, WHITE, AQUA, DARK_AQUA)
                stars >= 420 -> formatStars("[$stars✯]", BLACK, DARK_PURPLE, GRAY, GRAY, DARK_PURPLE, BLACK)
                stars >= 410 -> formatStars("[$stars✯]", DARK_AQUA, AQUA, AQUA, AQUA, AQUA, DARK_AQUA)
                stars >= 400 -> formatStarsObfuscated("[$stars✯]", GREEN, AQUA, LIGHT_PURPLE, RED, GOLD, YELLOW)
                stars >= 390 -> formatStars("[$stars✯]", DARK_GREEN, GREEN, GREEN, GREEN, GREEN, DARK_GREEN)
                stars >= 380 -> formatStars("[$stars✯]", RED, WHITE, RED, RED, WHITE, RED)
                stars >= 370 -> formatStars("[$stars✯]", YELLOW, YELLOW, WHITE, WHITE, DARK_GRAY, DARK_GRAY)
                stars >= 360 -> formatStars("[$stars✯]", DARK_BLUE, DARK_AQUA, AQUA, WHITE, YELLOW, YELLOW)
                stars >= 350 -> formatStars("[$stars✯]", WHITE, WHITE, YELLOW, YELLOW, GOLD, GOLD)
                stars >= 340 -> formatStars("[$stars✯]", AQUA, GREEN, AQUA, LIGHT_PURPLE, GREEN, GREEN)
                stars >= 330 -> formatStars("[$stars✯]", YELLOW, RED, RED, RED, RED, YELLOW)
                stars >= 320 -> formatStars("[$stars✯]", LIGHT_PURPLE, GREEN, GREEN, GREEN, GREEN, LIGHT_PURPLE)
                stars >= 310 -> formatStars("[$stars✯]", DARK_GRAY, GRAY, GRAY, GRAY, GRAY, DARK_GRAY)
                stars >= 300 -> formatStarsObfuscated("[$stars✯]", YELLOW, GREEN, AQUA, LIGHT_PURPLE, RED, GOLD)
                stars >= 290 -> formatStars("[$stars✯]", BLUE, AQUA, AQUA, AQUA, AQUA, BLUE)
                stars >= 280 -> formatStars("[$stars✯]", GREEN, DARK_GREEN, GREEN, YELLOW, GREEN, DARK_GREEN)
                stars >= 270 -> formatStars(
                    "[$stars✯]",
                    DARK_BLUE,
                    DARK_AQUA,
                    DARK_AQUA,
                    DARK_AQUA,
                    DARK_AQUA,
                    DARK_BLUE
                )

                stars >= 260 -> formatStars("[$stars✯]", BLACK, YELLOW, GOLD, GOLD, YELLOW, BLACK)
                stars >= 250 -> formatStars("[$stars✯]", RED, GOLD, YELLOW, YELLOW, GOLD, RED)
                stars >= 240 -> formatStars("[$stars✯]", BLACK)
                stars >= 230 -> formatStars("[$stars✯]", LIGHT_PURPLE, AQUA, AQUA, AQUA, AQUA, LIGHT_PURPLE)
                stars >= 220 -> formatStars("[$stars✯]", DARK_GRAY)
                stars >= 210 -> formatStars(
                    "[$stars✯]",
                    DARK_PURPLE,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    LIGHT_PURPLE,
                    DARK_PURPLE
                )

                stars >= 200 -> formatStarsObfuscated("[$stars✯]", GOLD, YELLOW, GREEN, LIGHT_PURPLE, AQUA, RED)
                stars >= 190 -> formatStars("[$stars✯]", DARK_RED, YELLOW, YELLOW, YELLOW, YELLOW, DARK_RED)
                stars >= 180 -> formatStars("[$stars✯]", DARK_AQUA)
                stars >= 170 -> formatStars("[$stars✯]", DARK_BLUE, BLUE, BLUE, BLUE, BLUE, DARK_BLUE)
                stars >= 160 -> formatStars("[$stars✯]", DARK_GREEN)
                stars >= 150 -> formatStars("[$stars✯]", GOLD, YELLOW, YELLOW, YELLOW, YELLOW, GOLD)
                stars >= 140 -> formatStars("[$stars✯]", DARK_RED)
                stars >= 130 -> formatStars("[$stars✯]", RED, WHITE, WHITE, WHITE, WHITE, RED)
                stars >= 120 -> formatStars("[$stars✯]", DARK_BLUE)
                stars >= 110 -> formatStars("[$stars✯]", DARK_RED, RED, RED, RED, RED, DARK_RED)
                stars >= 100 -> formatStarsObfuscated("[$stars✯]", RED, GOLD, YELLOW, GREEN, AQUA, LIGHT_PURPLE)
                stars >= 90 -> formatStars("[$stars✯]", GREEN)
                stars >= 80 -> formatStars("[$stars✯]", YELLOW)
                stars >= 70 -> formatStars("[$stars✯]", DARK_BLUE)
                stars >= 60 -> formatStars("[$stars✯]", DARK_PURPLE)
                stars >= 50 -> formatStars("[$stars✯]", LIGHT_PURPLE)
                stars >= 40 -> formatStars("[$stars✯]", RED)
                stars >= 30 -> formatStars("[$stars✯]", AQUA)
                stars >= 20 -> formatStars("[$stars✯]", GOLD)
                stars >= 10 -> formatStars("[$stars✯]", WHITE)
                else -> formatStars("[$stars✯]", GRAY)
            }
        }
    }

    private fun getDuelsDivision(uuid: String, duelsMode: DuelsMode): CompletableFuture<Component?> {
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

            val divisionText = division(
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

            //? if = 1.8.9-forge {
            /*ChatComponentText(
                *///?} else {
            Component.literal(
                //?}
                divisionText
            )
        }
    }

    private fun formatStars(text: String, vararg colors: ChatFormatting): Component {
        val result =
        //? if = 1.8.9-forge {
                /*ChatComponentText("")
            *///?} else {
            Component.empty()
        //?}
        text.forEachIndexed { i, char ->
            val color = if (i < colors.size) colors[i] else colors.last()
            //? if = 1.8.9-forge {
            /*result.appendSibling(ChatComponentText(char.toString()).setChatStyle(ChatStyle().setColor(color)))
            *///?} else {
            result.append(Component.literal(char.toString()).withStyle(color))
            //?}
        }
        return result
    }

    private fun formatStarsObfuscated(text: String, vararg colors: ChatFormatting): Component {
        val result =
        //? if = 1.8.9-forge {
                /*ChatComponentText("")
            *///?} else {
            Component.empty()
        //?}
        text.forEachIndexed { i, char ->
            val color = if (i < colors.size) colors[i] else colors.last()

            if (i == 0 || i == text.length - 1)
            //? if = 1.8.9-forge {
            /*result.appendSibling(
                ChatComponentText(char.toString()).setChatStyle(
                    ChatStyle().setColor(color).setObfuscated(true)
                )
            )
        *///?} else {
                result.append(Component.literal(char.toString()).withStyle(OBFUSCATED, color))
            //?}
            else
            //? if = 1.8.9-forge {
            /*result.appendSibling(ChatComponentText(char.toString()).setChatStyle(ChatStyle().setColor(color)))
        *///?} else {
                result.append(Component.literal(char.toString()).withStyle(color))
            //?}
        }
        return result
    }
}