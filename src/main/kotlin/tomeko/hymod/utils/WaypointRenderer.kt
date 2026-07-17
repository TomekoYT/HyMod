package tomeko.hymod.utils

//? if = 1.8.9 {
/*import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.*
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.opengl.GL11
*///?} else {
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
//? if = 26.2 {
/*import net.minecraft.client.renderer.SubmitNodeCollector
*///?} else {
import net.minecraft.client.renderer.MultiBufferSource
//?}
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import org.joml.Matrix4f
//?}

import java.util.ArrayList
import kotlin.math.*

object WaypointRenderer {
    private const val BEACON_PNG =
    //? if = 1.8.9 {
            /*"textures/entity/beacon_beam.png"
            *///?} else {
        "textures/entity/beacon/beacon_beam.png"
    //?}

    private const val TEXT_SCALE_START_DISTANCE = 12.0
    private const val TEXT_SCALE_EXPONENT = 1.3

    //? if = 1.8.9 {
    /*private val BEAM_TEXTURE = ResourceLocation(BEACON_PNG)
    *///?} else {
    private val BEAM_TEXTURE = Identifier.parse(BEACON_PNG)
    //?}

    val waypoints: MutableList<Waypoint> = ArrayList()

    fun register() {
        //? if = 1.8.9 {
        /*MinecraftForge.EVENT_BUS.register(WaypointRenderer)
        *///?} else {
        LevelRenderEvents.AFTER_TRANSLUCENT_FEATURES.register(::onWorldRender)
        ClientTickEvents.END_CLIENT_TICK.register(::onTick)
        //?}
    }

    //? if = 1.8.9 {
    /*@SubscribeEvent
    *///?} else {
    @JvmStatic
    //?}
    fun onWorldRender(
        //? if = 1.8.9 {
        /*event: RenderWorldLastEvent
        *///?} else {
        context: LevelRenderContext
        //?}
    ) {
        for (waypoint in waypoints) {
            renderWaypoint(
                waypoint,
                //? if = 1.8.9 {
                /*event
                *///?} else {
                context
                //?}
            )
        }
    }

    //? if = 1.8.9 {
    /*@SubscribeEvent
    *///?} else {
    @JvmStatic
    //?}
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

        val iterator = waypoints.iterator()
        while (iterator.hasNext()) {
            val waypoint = iterator.next()
            waypoint.tickTime--

            if (waypoint.tickTime <= 0) {
                iterator.remove()
            }
        }
    }

    private fun renderWaypoint(
        waypoint: Waypoint?,
        //? if = 1.8.9 {
        /*event: RenderWorldLastEvent
        *///?} else {
        context: LevelRenderContext
        //?}
    ) {
        if (waypoint == null) return

        //? if >= 26.1 {
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().level == null) return
        //?}

        val viewerX =
        //? if = 1.8.9 {
                /*Minecraft.getMinecraft().renderViewEntity.let { it.lastTickPosX + (it.posX - it.lastTickPosX) * event.partialTicks }
                *///?} else if >= 26.2 {
                /*Minecraft.getInstance().gameRenderer.mainCamera().position().x
            *///?} else {
            Minecraft.getInstance().gameRenderer.mainCamera.position().x
//?}
        val viewerY =
        //? if = 1.8.9 {
                /*Minecraft.getMinecraft().renderViewEntity.let { it.lastTickPosY + (it.posY - it.lastTickPosY) * event.partialTicks }
                *///?} else if >= 26.2 {
                /*Minecraft.getInstance().gameRenderer.mainCamera().position().y
            *///?} else {
            Minecraft.getInstance().gameRenderer.mainCamera.position().y
//?}
        val viewerZ =
        //? if = 1.8.9 {
                /*Minecraft.getMinecraft().renderViewEntity.let { it.lastTickPosZ + (it.posZ - it.lastTickPosZ) * event.partialTicks }
                *///?} else if >= 26.2 {
                /*Minecraft.getInstance().gameRenderer.mainCamera().position().z
            *///?} else {
            Minecraft.getInstance().gameRenderer.mainCamera.position().z
//?}
        val renderX = waypoint.pos.x - viewerX
        val renderY = waypoint.pos.y - viewerY
        val renderZ = waypoint.pos.z - viewerZ

        drawBox(
            //? if >= 26.1 {
            context.poseStack(),
            //? if >= 26.2 {
            /*context.submitNodeCollector(),
            *///?} else {
            context.bufferSource(),
            //?}
            //?}
            renderX, renderY, renderZ,
            waypoint.boxColor.red / 255f,
            waypoint.boxColor.green / 255f,
            waypoint.boxColor.blue / 255f,
            waypoint.boxColor.alpha / 255f
        )

        renderBeaconBeam(
            //? if >= 26.1 {
            context.poseStack(),
            //? if >= 26.2 {
            /*context.submitNodeCollector(),
            *///?} else {
            context.bufferSource(),
            //?}
            //?}
            renderX, renderY + 1, renderZ,
            waypoint.beamColor.red / 255f,
            waypoint.beamColor.green / 255f,
            waypoint.beamColor.blue / 255f,
            waypoint.beamColor.alpha / 255f
            //? if = 1.8.9 {
            /*, event.partialTicks
            *///?}
        )

        val textArgb =
            (waypoint.textColor.alpha shl 24) or (waypoint.textColor.red shl 16) or (waypoint.textColor.green shl 8) or waypoint.textColor.blue
        val ownerArgb =
            (waypoint.ownerColor.alpha shl 24) or (waypoint.ownerColor.red shl 16) or (waypoint.ownerColor.green shl 8) or waypoint.ownerColor.blue
        val distArgb =
            (waypoint.distanceTextColor.alpha shl 24) or (waypoint.distanceTextColor.red shl 16) or (waypoint.distanceTextColor.green shl 8) or waypoint.distanceTextColor.blue

        renderWaypointText(
            //? if >= 26.1 {
            context.poseStack(),
            //? if >= 26.2 {
            /*context.submitNodeCollector(),
            *///?} else {
            context.bufferSource(),
            //?}
            //?}
            waypoint.text, waypoint.owner, waypoint.pos,
            waypoint.renderText, waypoint.renderOwner, waypoint.renderDistance,
            textArgb, ownerArgb, distArgb,
            viewerX, viewerY, viewerZ
        )
    }

    private fun drawBox(
        //? if >= 26.1 {
        matrices: PoseStack,
        //? if >= 26.2 {
        /*collector: SubmitNodeCollector,
        *///?} else {
        consumers: MultiBufferSource,
        //?}
        //?}
        x: Double, y: Double, z: Double,
        r: Float, g: Float, b: Float, a: Float
    ) {
        //? if = 1.8.9 {
        /*GlStateManager.pushMatrix()
        GlStateManager.translate(x, y, z)

        GlStateManager.enableBlend()
        GlStateManager.disableLighting()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.disableTexture2D()
        GlStateManager.color(r, g, b, a)

        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        *///?} else {
        matrices.pushPose()
        matrices.translate(x, y, z)

        //? if >= 26.2 {
        /*collector.submitCustomGeometry(matrices, RenderTypes.debugFilledBox()) { pose, buffer ->
            *///?} else {
        val buffer = consumers.getBuffer(RenderTypes.debugFilledBox())
        val pose = matrices.last().pose()
        //?}
        //?}

        addDoubleSidedQuad(
            //? if >= 26.1 {
            buffer, pose,
            //?}
            0f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 1f, 0f, 0f, 1f, r, g, b, a
        )
        addDoubleSidedQuad(
            //? if >= 26.1 {
            buffer, pose,
            //?}
            0f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 0f, 0f, 1f, 0f, r, g, b, a
        )
        addDoubleSidedQuad(
            //? if >= 26.1 {
            buffer, pose,
            //?}
            0f, 0f, 0f, 1f, 0f, 0f, 1f, 1f, 0f, 0f, 1f, 0f, r, g, b, a
        )
        addDoubleSidedQuad(
            //? if >= 26.1 {
            buffer, pose,
            //?}
            0f, 0f, 1f, 1f, 0f, 1f, 1f, 1f, 1f, 0f, 1f, 1f, r, g, b, a
        )
        addDoubleSidedQuad(
            //? if >= 26.1 {
            buffer, pose,
            //?}
            0f, 0f, 0f, 0f, 0f, 1f, 0f, 1f, 1f, 0f, 1f, 0f, r, g, b, a
        )
        addDoubleSidedQuad(
            //? if >= 26.1 {
            buffer, pose,
            //?}
            1f, 0f, 0f, 1f, 0f, 1f, 1f, 1f, 1f, 1f, 1f, 0f, r, g, b, a
        )

        //? if = 1.8.9 {
        /*tessellator.draw()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
        *///?} else {
        //? if >= 26.2 {
        //}
        //?}
        matrices.popPose()
        //?}
    }

    private fun addDoubleSidedQuad(
        //? if >= 26.1 {
        buffer: VertexConsumer,
        //? if >= 26.2 {
        /*pose: PoseStack.Pose,
        *///?} else {
        pose: Matrix4f,
        //?}
        //?}
        x1: Float, y1: Float, z1: Float,
        x2: Float, y2: Float, z2: Float,
        x3: Float, y3: Float, z3: Float,
        x4: Float, y4: Float, z4: Float,
        r: Float, g: Float, b: Float, a: Float
    ) {
        //? if = 1.8.9 {
        /*val wr = Tessellator.getInstance().worldRenderer
        wr.pos(x1.toDouble(), y1.toDouble(), z1.toDouble()).endVertex()
        wr.pos(x2.toDouble(), y2.toDouble(), z2.toDouble()).endVertex()
        wr.pos(x3.toDouble(), y3.toDouble(), z3.toDouble()).endVertex()
        wr.pos(x4.toDouble(), y4.toDouble(), z4.toDouble()).endVertex()

        wr.pos(x4.toDouble(), y4.toDouble(), z4.toDouble()).endVertex()
        wr.pos(x3.toDouble(), y3.toDouble(), z3.toDouble()).endVertex()
        wr.pos(x2.toDouble(), y2.toDouble(), z2.toDouble()).endVertex()
        wr.pos(x1.toDouble(), y1.toDouble(), z1.toDouble()).endVertex()
        *///?} else {
        //? if >= 26.2 {
        /*val p = pose.pose()
        *///?} else {
        val p = pose
        //?}

        buffer.addVertex(p, x1, y1, z1).setColor(r, g, b, a)
        buffer.addVertex(p, x2, y2, z2).setColor(r, g, b, a)
        buffer.addVertex(p, x3, y3, z3).setColor(r, g, b, a)
        buffer.addVertex(p, x4, y4, z4).setColor(r, g, b, a)

        buffer.addVertex(p, x4, y4, z4).setColor(r, g, b, a)
        buffer.addVertex(p, x3, y3, z3).setColor(r, g, b, a)
        buffer.addVertex(p, x2, y2, z2).setColor(r, g, b, a)
        buffer.addVertex(p, x1, y1, z1).setColor(r, g, b, a)
        //?}
    }

    private fun renderBeaconBeam(
        //? if >= 26.1 {
        matrices: PoseStack,
        //? if >= 26.2 {
        /*collector: SubmitNodeCollector,
        *///?} else {
        consumers: MultiBufferSource,
        //?}
        //?}
        x: Double, y: Double, z: Double,
        r: Float, g: Float, b: Float, a: Float
        //? if = 1.8.9 {
        /*, partialTicks: Float
        *///?}
    ) {
        //? if = 1.8.9 {
        /*val time = Minecraft.getMinecraft().theWorld.totalWorldTime + partialTicks.toDouble()
        *///?} else {
        val time =
            Minecraft.getInstance().level!!.gameTime + Minecraft.getInstance().deltaTracker.gameTimeDeltaTicks.toDouble()
        //?}

        val t1 = -time * 0.2
        val d1 = t1 - floor(t1)
        val d14 = (-1.0 + d1).toFloat()
        val d15 = (300.0 * 2.5 + d14).toFloat()
        val d12 = (-1.0 + d1).toFloat()
        val d13 = 300.0f + d12

        val d2 = time * 0.025 * -1.5
        val d4 = (0.5 + cos(d2 + 2.356194490192345) * 0.2).toFloat()
        val d5 = (0.5 + sin(d2 + 2.356194490192345) * 0.2).toFloat()
        val d6 = (0.5 + cos(d2 + (Math.PI / 4)) * 0.2).toFloat()
        val d7 = (0.5 + sin(d2 + (Math.PI / 4)) * 0.2).toFloat()
        val d8 = (0.5 + cos(d2 + 3.9269908169872414) * 0.2).toFloat()
        val d9 = (0.5 + sin(d2 + 3.9269908169872414) * 0.2).toFloat()
        val d10 = (0.5 + cos(d2 + 5.497787143782138) * 0.2).toFloat()
        val d11 = (0.5 + sin(d2 + 5.497787143782138) * 0.2).toFloat()

        //? if = 1.8.9 {
        /*GlStateManager.pushMatrix()
        GlStateManager.translate(x, y, z)

        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        Minecraft.getMinecraft().textureManager.bindTexture(BEAM_TEXTURE)
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F)
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F)
        GlStateManager.disableLighting()
        GlStateManager.enableCull()
        GlStateManager.enableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0)
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
        *///?} else {
        matrices.pushPose()
        matrices.translate(x, y, z)
        //? if >= 26.2 {
        /*collector.submitCustomGeometry(matrices, RenderTypes.beaconBeam(BEAM_TEXTURE, true)) { pose, buffer ->
            *///?} else {
        val pose = matrices.last()
        val buffer = consumers.getBuffer(RenderTypes.beaconBeam(BEAM_TEXTURE, true))
        //?}
        //?}

        val yMin = 0.0f
        val yMax = 300.0f

        renderBeamSide(//? if >= 26.1 {
            pose, buffer, //?}
            r, g, b, a, 1.0f, yMin, yMax, d4, d5, d6, d7, 1.0f, 0.0f, d14, d15
        )
        renderBeamSide(//? if >= 26.1 {
            pose, buffer, //?}
            r, g, b, a, 1.0f, yMin, yMax, d10, d11, d8, d9, 1.0f, 0.0f, d14, d15
        )
        renderBeamSide(//? if >= 26.1 {
            pose, buffer, //?}
            r, g, b, a, 1.0f, yMin, yMax, d6, d7, d10, d11, 1.0f, 0.0f, d14, d15
        )
        renderBeamSide(//? if >= 26.1 {
            pose, buffer, //?}
            r, g, b, a, 1.0f, yMin, yMax, d8, d9, d4, d5, 1.0f, 0.0f, d14, d15
        )

        //? if = 1.8.9 {
        /*tessellator.draw()
        GlStateManager.disableCull()
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
        *///?}

        val innerTopA = 0.25f * a
        val innerBotA = 0.25f

        renderBeamSide(//? if >= 26.1 {
            pose, buffer, //?}
            r, g, b, innerTopA, innerBotA, yMin, yMax, 0.2f, 0.2f, 0.8f, 0.2f, 1.0f, 0.0f, d12, d13
        )
        renderBeamSide(//? if >= 26.1 {
            pose, buffer, //?}
            r, g, b, innerTopA, innerBotA, yMin, yMax, 0.8f, 0.8f, 0.2f, 0.8f, 1.0f, 0.0f, d12, d13
        )
        renderBeamSide(//? if >= 26.1 {
            pose, buffer, //?}
            r, g, b, innerTopA, innerBotA, yMin, yMax, 0.8f, 0.2f, 0.8f, 0.8f, 1.0f, 0.0f, d12, d13
        )
        renderBeamSide(//? if >= 26.1 {
            pose, buffer, //?}
            r, g, b, innerTopA, innerBotA, yMin, yMax, 0.2f, 0.8f, 0.2f, 0.2f, 1.0f, 0.0f, d12, d13
        )

        //? if = 1.8.9 {
        /*tessellator.draw()
        GlStateManager.popMatrix()
        *///?} else {
        //? if >= 26.2 {
        //}
        //?}
        matrices.popPose()
        //?}
    }

    private fun renderBeamSide(
        //? if >= 26.1 {
        pose: PoseStack.Pose,
        buffer: VertexConsumer,
        //?}
        r: Float, g: Float, b: Float, topA: Float, botA: Float,
        yMin: Float, yMax: Float,
        x1: Float, z1: Float,
        x2: Float, z2: Float,
        u1: Float, u2: Float,
        v1: Float, v2: Float
    ) {
        //? if = 1.8.9 {
        /*val wr = Tessellator.getInstance().worldRenderer
        wr.pos(x1.toDouble(), yMax.toDouble(), z1.toDouble()).tex(u1.toDouble(), v2.toDouble()).color(r, g, b, topA).endVertex()
        wr.pos(x1.toDouble(), yMin.toDouble(), z1.toDouble()).tex(u1.toDouble(), v1.toDouble()).color(r, g, b, botA).endVertex()
        wr.pos(x2.toDouble(), yMin.toDouble(), z2.toDouble()).tex(u2.toDouble(), v1.toDouble()).color(r, g, b, botA).endVertex()
        wr.pos(x2.toDouble(), yMax.toDouble(), z2.toDouble()).tex(u2.toDouble(), v2.toDouble()).color(r, g, b, topA).endVertex()
        *///?} else {
        //? if >= 26.2 {
        /*val p = pose.pose()
        *///?} else {
        val p = pose
        //?}
        buffer.addVertex(p, x1, yMax, z1).setColor(r, g, b, topA).setUv(u1, v2).setUv2(15, 15)
            //? if >= 26.2 {
            /*.setNormal(pose, 0.0f, 1.0f, 0.0f)*///?} else {
            .setNormal(0.0f, 1.0f, 0.0f)//?}
        buffer.addVertex(p, x1, yMin, z1).setColor(r, g, b, botA).setUv(u1, v1).setUv2(15, 15)
            //? if >= 26.2 {
            /*.setNormal(pose, 0.0f, 1.0f, 0.0f)*///?} else {
            .setNormal(0.0f, 1.0f, 0.0f)//?}
        buffer.addVertex(p, x2, yMin, z2).setColor(r, g, b, botA).setUv(u2, v1).setUv2(15, 15)
            //? if >= 26.2 {
            /*.setNormal(pose, 0.0f, 1.0f, 0.0f)*///?} else {
            .setNormal(0.0f, 1.0f, 0.0f)//?}
        buffer.addVertex(p, x2, yMax, z2).setColor(r, g, b, topA).setUv(u2, v2).setUv2(15, 15)
            //? if >= 26.2 {
            /*.setNormal(pose, 0.0f, 1.0f, 0.0f)*///?} else {
            .setNormal(0.0f, 1.0f, 0.0f)//?}
        //?}
    }

    private fun renderWaypointText(
        //? if >= 26.1 {
        matrices: PoseStack,
        //? if >= 26.2 {
        /*collector: SubmitNodeCollector,
        *///?} else {
        consumers: MultiBufferSource,
        //?}
        //?}
        str: String, owner: String, loc: BlockPos,
        renderText: Boolean, renderOwner: Boolean, renderDistance: Boolean,
        textArgb: Int, ownerArgb: Int, distArgb: Int,
        viewerX: Double, viewerY: Double, viewerZ: Double
    ) {
        if (!renderText && !renderDistance && !renderOwner) return

        val dx = loc.x + 0.5 - viewerX
        val dy = loc.y + 2.0 - viewerY
        val dz = loc.z + 0.5 - viewerZ

        val distSq = dx * dx + dy * dy + dz * dz
        val dist = sqrt(distSq)
        val distText = "${dist.roundToInt()}m"

        val scaleMultiplier =
            if (dist > TEXT_SCALE_START_DISTANCE)
                (dist / TEXT_SCALE_START_DISTANCE).pow(TEXT_SCALE_EXPONENT).toFloat()
            else
                1f

        //? if = 1.8.9 {
        /*val viewer = Minecraft.getMinecraft().renderViewEntity
        val x = dx
        val y = dy - viewer.eyeHeight
        val z = dz

        GlStateManager.alphaFunc(516, 0.1F)
        GlStateManager.pushMatrix()
        GlStateManager.translate(x, y, z)
        GlStateManager.translate(0.0, viewer.eyeHeight.toDouble(), 0.0)
        *///?} else {
        val camera =
        //? if >= 26.2 {
                /*Minecraft.getInstance().gameRenderer.mainCamera()
            *///?} else {
            Minecraft.getInstance().gameRenderer.mainCamera
        //?}

        matrices.pushPose()
        matrices.translate(dx, dy, dz)
        matrices.mulPose(Axis.YP.rotationDegrees(-camera.yRot()))
        matrices.mulPose(Axis.XP.rotationDegrees(camera.xRot()))

        val scale = 0.025f * scaleMultiplier
        matrices.scale(-scale, -scale, scale)
        //?}

        var lineOffset = 0

        if (renderOwner && owner.isNotEmpty()) {
            drawNametag(
                owner, ownerArgb, lineOffset, scaleMultiplier,
                //? if >= 26.1 {
                matrices,
                //? if >= 26.2 {
                /*collector
                *///?} else {
                consumers
                //?}
                //?}
            )

            //? if = 1.8.9 {
            /*GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewY, 0.0F, 1.0F, 0.0F)
            GlStateManager.rotate(Minecraft.getMinecraft().renderManager.playerViewX, 1.0F, 0.0F, 0.0F)
            GlStateManager.translate(0.0f, -0.25f * scaleMultiplier, 0.0f)
            GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewX, 1.0F, 0.0F, 0.0F)
            GlStateManager.rotate(Minecraft.getMinecraft().renderManager.playerViewY, 0.0F, 1.0F, 0.0F)
            *///?} else {
            lineOffset += 10
            //?}
        }

        if (renderText && str.isNotEmpty()) {
            drawNametag(
                str, textArgb, lineOffset, scaleMultiplier,
                //? if >= 26.1 {
                matrices,
                //? if >= 26.2 {
                /*collector
                *///?} else {
                consumers
                //?}
                //?}
            )

            //? if = 1.8.9 {
            /*GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewY, 0.0F, 1.0F, 0.0F)
            GlStateManager.rotate(Minecraft.getMinecraft().renderManager.playerViewX, 1.0F, 0.0F, 0.0F)
            GlStateManager.translate(0.0f, -0.25f * scaleMultiplier, 0.0f)
            GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewX, 1.0F, 0.0F, 0.0F)
            GlStateManager.rotate(Minecraft.getMinecraft().renderManager.playerViewY, 0.0F, 1.0F, 0.0F)
            *///?} else {
            lineOffset += 10
            //?}
        }

        if (renderDistance) {
            drawNametag(
                distText, distArgb, lineOffset, scaleMultiplier,
                //? if >= 26.1 {
                matrices,
                //? if >= 26.2 {
                /*collector
                *///?} else {
                consumers
                //?}
                //?}
            )
        }

        //? if = 1.8.9 {
        /*GlStateManager.popMatrix()
        GlStateManager.disableLighting()
        *///?} else {
        matrices.popPose()
        //?}
    }

    private fun drawNametag(
        str: String, colorArgb: Int, line: Int, scaleMultiplier: Float,
        //? if >= 26.1 {
        matrices: PoseStack,
        //? if >= 26.2 {
        /*collector: SubmitNodeCollector
        *///?} else {
        consumers: MultiBufferSource
        //?}
        //?}
    ) {
        //? if = 1.8.9 {
        /*val fontrenderer = Minecraft.getMinecraft().fontRendererObj
        val f1 = 0.016666668F * 1.6F * scaleMultiplier
        GlStateManager.pushMatrix()
        GL11.glNormal3f(0.0F, 1.0F, 0.0F)
        GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewY, 0.0F, 1.0F, 0.0F)
        GlStateManager.rotate(Minecraft.getMinecraft().renderManager.playerViewX, 1.0F, 0.0F, 0.0F)
        GlStateManager.scale(-f1, -f1, f1)
        GlStateManager.disableLighting()
        GlStateManager.depthMask(false)
        GlStateManager.disableDepth()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)

        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        val j = fontrenderer.getStringWidth(str) / 2

        GlStateManager.disableTexture2D()
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
        worldrenderer.pos(-j - 1.0, -1.0, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex()
        worldrenderer.pos(-j - 1.0, 8.0, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex()
        worldrenderer.pos(j + 1.0, 8.0, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex()
        worldrenderer.pos(j + 1.0, -1.0, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex()
        tessellator.draw()
        GlStateManager.enableTexture2D()

        fontrenderer.drawString(str, -j, 0, 553648127)
        GlStateManager.depthMask(true)
        fontrenderer.drawString(str, -j, 0, colorArgb)

        GlStateManager.enableDepth()
        GlStateManager.enableBlend()
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F)
        GlStateManager.popMatrix()
        *///?} else {
        val width = -Minecraft.getInstance().font.width(str) / 2f
        val background = (Minecraft.getInstance().options.textBackgroundOpacity().get() * 255.0).toInt() shl 24

        //? if >= 26.2 {
        /*collector.submitText(
            matrices,
            width,
            line.toFloat(),
            Component.literal(str).visualOrderText,
            false,
            Font.DisplayMode.SEE_THROUGH,
            15728880,
            colorArgb,
            background,
            0
        )
        *///?} else {
        Minecraft.getInstance().font.drawInBatch(Component.literal(str), width, line.toFloat(), colorArgb, false, matrices.last().pose(), consumers, Font.DisplayMode.SEE_THROUGH, background, 15728880)
        //?}
        //?}
    }
}