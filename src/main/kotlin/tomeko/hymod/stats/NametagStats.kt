package tomeko.hymod.stats

import com.mojang.math.Axis
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
//? if >= 26.1 {
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents
//?} else {
/*import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext as LevelRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents as LevelRenderEvents
*///?}
import net.hypixel.modapi.HypixelModAPI
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component
import tomeko.hymod.config.HyModConfig
import tomeko.hymod.location.HypixelPackets
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.sqrt

object NametagStats {
    private var lastHypixelState = false

    fun register() {
        //? if = 1.8.9 {
        /*MinecraftForge.EVENT_BUS.register(this)
        *///?} else {
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick)
        //?}
        HypixelModAPI.getInstance().createHandler(ClientboundLocationPacket::class.java, this::onLocationPacket)
        HypixelModAPI.getInstance().subscribeToEventPacket(ClientboundLocationPacket::class.java)

        //? if >= 26.1 {
        LevelRenderEvents.COLLECT_SUBMITS.register(::render)
        //?} else {
        /*LevelRenderEvents.AFTER_ENTITIES.register(::render)
        *///?}
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


    private const val MAX_DISTANCE = 32.0
    private const val HEIGHT_OFFSET = 0.35
    private const val BASE_SCALE = 0.025f
    private const val MIN_DISTANCE = 1.0

    private val linesCache = ConcurrentHashMap<String, MutableList<Component>>()

    private val pendingLevel: ConcurrentHashMap.KeySetView<String?, Boolean?> = ConcurrentHashMap.newKeySet<String>()
    private val pendingBedwars: ConcurrentHashMap.KeySetView<String?, Boolean?> = ConcurrentHashMap.newKeySet<String>()
    private val pendingSkywars: ConcurrentHashMap.KeySetView<String?, Boolean?> = ConcurrentHashMap.newKeySet<String>()
    private val pendingDuels: ConcurrentHashMap.KeySetView<String?, Boolean?> = ConcurrentHashMap.newKeySet<String>()

    private fun render(context: LevelRenderContext) {
        val mc = Minecraft.getInstance()
        val level = mc.level ?: return

        val camera =
            //? if >= 26.1 {
            context.levelState().cameraRenderState
        //?} else {
        /*context.worldState().cameraRenderState
        *///?}

        for (player in level.players()) {
            if (!player.isAlive || player.isInvisible) {
                continue
            }

            val tickDelta = mc.deltaTracker.getGameTimeDeltaPartialTick(false)

            val x = player.xo + (player.x - player.xo) * tickDelta - camera.pos.x
            val relativeY = player.yo + (player.y - player.yo) * tickDelta - camera.pos.y
            val z = player.zo + (player.z - player.zo) * tickDelta - camera.pos.z

            val distanceSquared = x * x + relativeY * relativeY + z * z
            if (distanceSquared > MAX_DISTANCE * MAX_DISTANCE) continue

            val distance = sqrt(distanceSquared)

            val y = relativeY + player.bbHeight + HEIGHT_OFFSET

            val scale =
                (BASE_SCALE * (0.75 + 0.25 * (1.0 - 1.0.coerceAtMost(0.0.coerceAtLeast((distance - MIN_DISTANCE) / (MAX_DISTANCE - MIN_DISTANCE)))))).toFloat()

            val matrices =
                //? if >= 26.1 {
                context.poseStack()
            //?} else {
            /*context.matrices()
            *///?}

            matrices.pushPose()

            matrices.translate(x, y, z)
            matrices.mulPose(camera.orientation)
            matrices.mulPose(Axis.YP.rotationDegrees(180.0f))
            matrices.scale(-scale, -scale, scale)

            val uuid = player.stringUUID

            val shouldCheckHypixelLevel = (HypixelPackets.inBedwars && HyModConfig.showBedwarsStarsAboveNametag)
                    || (HypixelPackets.inSkywars && HyModConfig.showSkywarsStarsAboveNametag)
                    || (HypixelPackets.inDuels && HyModConfig.showDuelsDivisionAboveNametag)

            if (HypixelPackets.inBedwars && HyModConfig.showBedwarsStarsAboveNametag && pendingBedwars.add(uuid)) {
                AbyssStatsFetcher.getBedwarsStars(uuid)
                    .thenAccept { bedwars ->
                        if (bedwars != null) {
                            linesCache
                                .computeIfAbsent(uuid) {
                                    mutableListOf()
                                }
                                .apply {
                                    removeIf {
                                        it.toString().contains("§fBed§cWars§f:")
                                    }

                                    add(Component.literal("§fBed§cWars§f: ").append(bedwars))
                                }
                        }
                    }
                    .whenComplete { _, _ ->
                        pendingBedwars.remove(uuid)
                    }
            }

            if (HypixelPackets.inSkywars && HyModConfig.showSkywarsStarsAboveNametag && pendingSkywars.add(uuid)) {
                AbyssStatsFetcher.getSkywarsStars(uuid)
                    .thenAccept { skywars ->
                        if (skywars != null) {
                            linesCache
                                .computeIfAbsent(uuid) {
                                    mutableListOf()
                                }
                                .apply {
                                    removeIf {
                                        it.toString().contains("§bSky§aWars§f:")
                                    }

                                    add(Component.literal("§bSky§aWars§f: ").append(skywars))
                                }
                        }
                    }
                    .whenComplete { _, _ ->
                        pendingSkywars.remove(uuid)
                    }
            }

            if (HypixelPackets.inDuels && HyModConfig.showDuelsDivisionAboveNametag && pendingDuels.add(uuid)) {
                AbyssStatsFetcher.getDuelsDivision(uuid, HypixelPackets.duelsMode)
                    .thenAccept { division ->
                        if (division != null) {
                            linesCache
                                .computeIfAbsent(uuid) {
                                    mutableListOf()
                                }
                                .apply {
                                    removeIf {
                                        it.toString().contains("§3Duels§f:")
                                    }

                                    add(
                                        Component.literal(
                                            HypixelPackets.duelsMode.modeName + " §3Duels§f: " + division
                                        )
                                    )
                                }
                        }
                    }
                    .whenComplete { _, _ ->
                        pendingDuels.remove(uuid)
                    }
            }

            if (HypixelPackets.onHypixel && pendingLevel.add(uuid)) {
                AbyssStatsFetcher.getHypixelLevel(uuid)
                    .thenAccept { level ->
                        if (level != null) {
                            linesCache
                                .computeIfAbsent(uuid) {
                                    mutableListOf()
                                }
                                .apply {
                                    removeIf {
                                        it.toString().contains("§9Level§f:")
                                    }

                                    add(Component.literal("§9Level§f: §e$level"))
                                }
                        }
                    }
                    .whenComplete { _, _ ->
                        pendingLevel.remove(uuid)
                    }
            }

            val lines = linesCache[uuid]

            if (lines != null) {
                val submitNodeCollector =
                    //? if >= 26.1 {
                    context.submitNodeCollector()
                //?} else {
                /*context.commandQueue()
                *///?}

                var offset = 0

                for (text in lines) {
                    val width = mc.font.width(text)

                    submitNodeCollector.submitText(
                        matrices,
                        -width / 2.0f,
                        -15f + offset,
                        text.visualOrderText,
                        true,
                        Font.DisplayMode.SEE_THROUGH,
                        0xF000F0,
                        -0x1,
                        0x50000000,
                        0
                    )

                    offset -= 10
                }
            }

            matrices.popPose()
        }
    }

    private fun clearCache() {
        linesCache.clear()

        pendingLevel.clear()
        pendingBedwars.clear()
        pendingSkywars.clear()
        pendingDuels.clear()
    }
}