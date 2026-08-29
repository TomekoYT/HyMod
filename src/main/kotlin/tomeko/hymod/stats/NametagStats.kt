package tomeko.hymod.stats

import com.mojang.math.Axis
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
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.sqrt

object NametagStats {
    private const val MAX_DISTANCE = 32.0
    private const val HEIGHT_OFFSET = 0.35
    private const val BASE_SCALE = 0.025f
    private const val MIN_DISTANCE = 1.0

    private val linesCache =
        ConcurrentHashMap<String, List<Component>>()

    private val pending =
        ConcurrentHashMap.newKeySet<String>()

    fun register() {
        //? if >= 26.1 {
        LevelRenderEvents.COLLECT_SUBMITS.register(::render)
        //?} else {
        /*LevelRenderEvents.AFTER_ENTITIES.register(::render)
        *///?}
    }

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

            val tickDelta =
                mc.deltaTracker.getGameTimeDeltaPartialTick(false)

            val x =
                player.xo +
                        (player.x - player.xo) * tickDelta -
                        camera.pos.x

            val relativeY =
                player.yo +
                        (player.y - player.yo) * tickDelta -
                        camera.pos.y

            val z =
                player.zo +
                        (player.z - player.zo) * tickDelta -
                        camera.pos.z

            val distanceSquared =
                x * x +
                        relativeY * relativeY +
                        z * z

            if (distanceSquared > MAX_DISTANCE * MAX_DISTANCE) {
                continue
            }

            val distance = sqrt(distanceSquared)

            val y =
                relativeY +
                        player.bbHeight +
                        HEIGHT_OFFSET

            val distanceFactor =
                1.0 -
                        1.0.coerceAtMost(
                            0.0.coerceAtLeast(
                                (distance - MIN_DISTANCE) /
                                        (MAX_DISTANCE - MIN_DISTANCE)
                            )
                        )

            val scale =
                (
                        BASE_SCALE *
                                (0.75 + 0.25 * distanceFactor)
                        ).toFloat()

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

            requestStats(uuid)

            val lines = linesCache[uuid]
                ?: listOf(Component.literal("§7Loading..."))

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

            matrices.popPose()
        }
    }

    private fun requestStats(uuid: String) {
        if (linesCache.containsKey(uuid)) {
            return
        }

        if (!pending.add(uuid)) {
            return
        }

        val levelFuture =
            HypixelStatsFetcher.getHypixelLevel(uuid)

        val bedwarsFuture =
            HypixelStatsFetcher.getBedwarsStars(uuid)

        levelFuture
            .thenCombine(bedwarsFuture) { level, bedwars ->
                listOf(
                    Component.literal(
                        "§bLevel§f: §e${level ?: "N/A"}"
                    ),
                    Component.literal(
                        "§fBed§cWars§f: §4${bedwars ?: 0}"
                    )
                )
            }
            .thenAccept { lines ->
                linesCache[uuid] = lines
            }
            .whenComplete { _, _ ->
                pending.remove(uuid)
            }
    }

    fun clearCache() {
        linesCache.clear()
        pending.clear()
    }
}