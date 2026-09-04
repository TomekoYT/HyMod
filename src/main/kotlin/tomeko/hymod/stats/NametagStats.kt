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
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component
import tomeko.hymod.config.HyModConfig
import tomeko.hymod.location.HypixelPackets
import kotlin.math.sqrt

object NametagStats {
    fun register() {
        //? if >= 26.1 {
        LevelRenderEvents.COLLECT_SUBMITS.register(::render)
        //?} else {
        /*LevelRenderEvents.AFTER_ENTITIES.register(::render)
        *///?}
    }

    private const val MAX_DISTANCE = 32.0
    private const val HEIGHT_OFFSET = 0.35
    private const val BASE_SCALE = 0.025f
    private const val MIN_DISTANCE = 1.0

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
            val uuid = player.stringUUID

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

            val cached = AbyssStatsFetcher.getCachedStats(uuid)
            val lines = mutableListOf<Component>()

            if (HypixelPackets.inBedwars && HyModConfig.showBedwarsStarsAboveNametag) {
                cached.bedwars?.let { bedwars ->
                    lines.add(Component.literal(HyModConfig.bedwarsTextAboveNametag).append(bedwars))
                }
            }

            if (HypixelPackets.inSkywars && HyModConfig.showSkywarsStarsAboveNametag) {
                cached.skywars?.let { skywars ->
                    lines.add(Component.literal(HyModConfig.skywarsTextAboveNametag).append(skywars))
                }
            }

            if (HypixelPackets.inDuels && HyModConfig.showDuelsDivisionAboveNametag) {
                cached.duels?.let { division ->
                    lines.add(
                        Component.literal(HypixelPackets.duelsMode.modeName + HyModConfig.duelsTextAboveNametag)
                            .append(division)
                    )
                }
            }

            val shouldCheckNetworkLevel = lines.isNotEmpty()

            if (HypixelPackets.onHypixel
                && HyModConfig.showNetworkLevelAboveNametag
                && (!shouldCheckNetworkLevel || HyModConfig.showNetworkLevelWithOtherNametagStats)
            ) {
                cached.level?.let { networkLevel ->
                    lines.add(Component.literal(HyModConfig.networkLevelTextAboveNametag + networkLevel))
                }
            }

            val submitNodeCollector =
                //? if >= 26.1 {
                context.submitNodeCollector()
            //?} else {
            /*context.commandQueue()
            *///?}

            var offset = 0

            for (text in lines) {
                submitNodeCollector.submitText(
                    matrices,
                    -mc.font.width(text) / 2.0f,
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

            matrices.popPose()
        }
    }
}