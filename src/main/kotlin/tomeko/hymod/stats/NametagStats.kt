package tomeko.hymod.stats

//? if = 1.8.9-forge {
/*import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent as Component
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.common.MinecraftForge
import org.lwjgl.opengl.GL11
*///?} else {
import com.mojang.math.Axis
//? if >= 26.1-fabric {
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents
//?} else {
/*import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext as LevelRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents as LevelRenderEvents
*///?}
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component
//?}
import tomeko.hymod.config.HyModConfig
import tomeko.hymod.location.HypixelPackets
import kotlin.math.sqrt

object NametagStats {
    fun register() {
        //? if = 1.8.9-forge {
        /*MinecraftForge.EVENT_BUS.register(this)
        *///?} elif >= 26.1-fabric {
        LevelRenderEvents.COLLECT_SUBMITS.register(::render)
        //?} else {
        //LevelRenderEvents.AFTER_ENTITIES.register(::render)
        //?}
    }

    private const val MAX_DISTANCE = 32.0
    private const val HEIGHT_OFFSET = 0.35
    private const val BASE_SCALE = 0.025f
    private const val MIN_DISTANCE = 1.0
    const val NAMETAG_OFFSET = -15f

    //? if = 1.8.9-forge {
    /*@SubscribeEvent
    *///?}
    fun render(
        //? if = 1.8.9-forge {
        /*event: RenderWorldLastEvent,
        *///?} else {
        context: LevelRenderContext
        //?}
    ) {
        val mc =
        //? if = 1.8.9-forge {
                /*Minecraft.getMinecraft()
            *///?} else {
            Minecraft.getInstance()
        //?}
        val level =
        //? if = 1.8.9-forge {
                /*mc.theWorld
                *///?} else {
            mc.level
            //?}
                ?: return

        val players =
        //? if = 1.8.9-forge {
                /*level.playerEntities
            *///?} else {
            level.players()
        //?}

        for (player in players) {
            if (//? if = 1.8.9-forge {
            /*!player.isEntityAlive
            *///?} else {
                !player.isAlive
                //?}
                || player.isInvisible
            ) continue

            val uuid =
            //? if = 1.8.9-forge {
                    /*player.uniqueID
                *///?} else {
                player.uuid
            //?}

            if (uuid.version() != 4 && uuid.version() != 1) continue

            if (uuid.version() == 4) HypixelStatsFetcher.requestStats(uuid.toString())

            val playerX =
            //? if = 1.8.9-forge {
                    /*player.posX
                *///?} else {
                player.x
            //?}

            val playerY =
            //? if = 1.8.9-forge {
                    /*player.posY
                *///?} else {
                player.y
            //?}

            val playerZ =
            //? if = 1.8.9-forge {
                    /*player.posZ
                *///?} else {
                player.z
            //?}

            val playerLastX =
            //? if = 1.8.9-forge {
                    /*player.lastTickPosX
                *///?} else {
                player.xo
            //?}

            val playerLastY =
            //? if = 1.8.9-forge {
                    /*player.lastTickPosY
                *///?} else {
                player.yo
            //?}

            val playerLastZ =
            //? if = 1.8.9-forge {
                    /*player.lastTickPosZ
                *///?} else {
                player.zo
            //?}

            val camera =
            //? if = 1.8.9-forge {
                    /*mc.renderManager
                *///?} elif >= 26.1-fabric {
                context.levelState().cameraRenderState
            //?} else {
            //context.worldState().cameraRenderState
            //?}

            val cameraX =
            //? if = 1.8.9-forge {
                    /*camera.viewerPosX
                *///?} else {
                camera.pos.x
            //?}

            val cameraY =
            //? if = 1.8.9-forge {
                    /*camera.viewerPosY
                *///?} else {
                camera.pos.y
            //?}

            val cameraZ =
            //? if = 1.8.9-forge {
                    /*camera.viewerPosZ
                *///?} else {
                camera.pos.z
            //?}

            val tickDelta =
            //? if = 1.8.9-forge {
                    /*event.partialTicks
                *///?} else {
                mc.deltaTracker.getGameTimeDeltaPartialTick(false)
            //?}

            val x = playerLastX + (playerX - playerLastX) * tickDelta - cameraX
            val relativeY = playerLastY + (playerY - playerLastY) * tickDelta - cameraY
            val z = playerLastZ + (playerZ - playerLastZ) * tickDelta - cameraZ

            val distanceSquared = x * x + relativeY * relativeY + z * z
            if (distanceSquared > MAX_DISTANCE * MAX_DISTANCE) continue

            val distance = sqrt(distanceSquared)

            val playerHeight =
            //? if = 1.8.9-forge {
                    /*player.height
                *///?} else {
                player.bbHeight
            //?}
            val y = relativeY + playerHeight + HEIGHT_OFFSET

            val scale =
                (BASE_SCALE * (0.75 + 0.25 * (1.0 - 1.0.coerceAtMost(0.0.coerceAtLeast((distance - MIN_DISTANCE) / (MAX_DISTANCE - MIN_DISTANCE)))))).toFloat()

            val cached = HypixelStatsFetcher.getCachedStats(uuid.toString())
            val lines = mutableListOf<Component>()

            if (uuid.version() == 1) {
                if (HyModConfig.showNickedIndicatorAboveNametag) {
                    lines.add(
                        //? if = 1.8.9-forge {
                        //ChatComponentText(
                        //?} else {
                        Component.literal(
                            //?}
                            HyModConfig.nickedIndicatorText
                        )
                    )
                }
            } else {
                if (HypixelPackets.inDuels && HyModConfig.showDuelsDivisionAboveNametag) {
                    cached.duels?.let { division ->
                        lines.add(
                            //? if = 1.8.9-forge {
                            /*ChatComponentText(HypixelPackets.duelsMode.modeName + HyModConfig.duelsTextAboveNametag).appendSibling(
                            division
                        )
                        *///?} else {
                            Component.literal(HypixelPackets.duelsMode.modeName + HyModConfig.duelsTextAboveNametag)
                                .append(division)
                            //?}
                        )
                    }
                }

                if (HypixelPackets.inBedwars && HyModConfig.showBedwarsStarsAboveNametag) {
                    cached.bedwars?.let { bedwars ->
                        lines.add(
                            //? if = 1.8.9-forge {
                            /*ChatComponentText(HyModConfig.bedwarsTextAboveNametag).appendSibling(bedwars)
                        *///?} else {
                            Component.literal(HyModConfig.bedwarsTextAboveNametag).append(bedwars)
                            //?}
                        )
                    }
                }

                if (HypixelPackets.inSkywars && HyModConfig.showSkywarsStarsAboveNametag) {
                    cached.skywars?.let { skywars ->
                        lines.add(
                            //? if = 1.8.9-forge {
                            /*ChatComponentText(HyModConfig.skywarsTextAboveNametag).appendSibling(skywars)
                        *///?} else {
                            Component.literal(HyModConfig.skywarsTextAboveNametag).append(skywars)
                            //?}
                        )
                    }
                }

                val shouldCheckNetworkLevelWithOtherNametagStats =
                    (HypixelPackets.inBedwars && HyModConfig.showBedwarsStarsAboveNametag)
                            || (HypixelPackets.inSkywars && HyModConfig.showSkywarsStarsAboveNametag)
                            || (HypixelPackets.inDuels && HyModConfig.showDuelsDivisionAboveNametag)

                if (HypixelPackets.onHypixel
                    && HyModConfig.showNetworkLevelAboveNametag
                    && (!shouldCheckNetworkLevelWithOtherNametagStats || HyModConfig.showNetworkLevelWithOtherNametagStats)
                ) {
                    cached.level?.let { networkLevel ->
                        lines.add(
                            //? if = 1.8.9-forge {
                            /*ChatComponentText(HyModConfig.networkLevelTextAboveNametag + networkLevel)
                        *///?} else {
                            Component.literal(HyModConfig.networkLevelTextAboveNametag + networkLevel)
                            //?}
                        )
                    }
                }
            }

            //? if = 1.8.9-forge {
            /*GlStateManager.pushMatrix()
            GlStateManager.translate(x, y, z)

            GlStateManager.rotate(-camera.playerViewY, 0.0f, 1.0f, 0.0f)
            GlStateManager.rotate(camera.playerViewX, 1.0f, 0.0f, 0.0f)
            GlStateManager.scale(-scale, -scale, scale)

            GlStateManager.disableDepth()
            GlStateManager.depthMask(false)
            GlStateManager.enableBlend()
            GlStateManager.tryBlendFuncSeparate(
                GL11.GL_SRC_ALPHA,
                GL11.GL_ONE_MINUS_SRC_ALPHA,
                GL11.GL_ONE,
                GL11.GL_ZERO
            )
            *///?} else {
            val matrices =
                //? if >= 26.1-fabric {
                context.poseStack()
            //?} else {
            /*context.matrices()
        *///?}

            matrices.pushPose()

            matrices.translate(x, y, z)
            matrices.mulPose(camera.orientation)
            matrices.mulPose(Axis.YP.rotationDegrees(180.0f))
            matrices.scale(-scale, -scale, scale)
            val submitNodeCollector =
                //? if >= 26.1-fabric {
                context.submitNodeCollector()
            //?} else {
            /*context.commandQueue()
        *///?}
            //?}

            var offset = 0

            for (text in lines) {
                val width =
                //? if = 1.8.9-forge {
                        /*mc.fontRendererObj.getStringWidth(text.formattedText)
                    *///?} else {
                    mc.font.width(text)
                //?}

                //? if = 1.8.9-forge {
                /*val paddingX = 2
                val paddingY = 1
                val textHeight = 9

                Gui.drawRect(
                    -width / 2 - paddingX,
                    (NAMETAG_OFFSET + offset - paddingY).toInt(),
                    width / 2 + paddingX,
                    (NAMETAG_OFFSET + offset + textHeight + paddingY).toInt(),
                    0x50000000
                )

                mc.fontRendererObj.drawString(
                    text.formattedText,
                    -width / 2.0f,
                    NAMETAG_OFFSET + offset,
                    0xFFFFFFFF.toInt(),
                    true
                )
                *///?} else {
                submitNodeCollector.submitText(
                    matrices,
                    -width / 2.0f,
                    NAMETAG_OFFSET + offset,
                    text.visualOrderText,
                    true,
                    Font.DisplayMode.SEE_THROUGH,
                    0xF000F0,
                    -0x1,
                    0x50000000,
                    0
                )
                //?}

                offset -= 10
            }

            //? if = 1.8.9-forge {
            /*GlStateManager.depthMask(true)
            GlStateManager.enableDepth()
            GlStateManager.disableBlend()

            GlStateManager.popMatrix()
            *///?} else {
            matrices.popPose()
            //?}
        }
    }
}