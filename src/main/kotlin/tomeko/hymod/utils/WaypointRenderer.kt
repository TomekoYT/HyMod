package tomeko.hymod.utils

import net.minecraft.client.Minecraft
//? if = 1.8.9 {
/*import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.*
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.opengl.GL11
import cc.polyfrost.oneconfig.config.core.OneColor
*///?} else {
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents
import net.minecraft.client.gui.Font
//? if = 26.2 {
/*import net.minecraft.client.renderer.SubmitNodeCollector
*///?} else {
import net.minecraft.client.renderer.MultiBufferSource
//?}
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.core.BlockBox
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import org.joml.Matrix4f
import org.polyfrost.compose.render.PolyColor
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
        LevelRenderEvents.AFTER_TRANSLUCENT_FEATURES.register(WaypointRenderer::onWorldRender)
        ClientTickEvents.END_CLIENT_TICK.register(WaypointRenderer::onTick)
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

        //? if = 1.8.9 {
        /*val viewer = Minecraft.getMinecraft().renderViewEntity
        *///?}

        val viewerX =
        //? if = 1.8.9 {
                /*viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * event.partialTicks
            *///?} else if >= 26.2 {
                /*Minecraft.getInstance().gameRenderer.mainCamera().position().x
            *///?} else {
            Minecraft.getInstance().gameRenderer.mainCamera.position().x
//?}
        val viewerY =
        //? if = 1.8.9 {
                /*viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * event.partialTicks
            *///?} else if >= 26.2 {
                /*Minecraft.getInstance().gameRenderer.mainCamera().position().y
            *///?} else {
            Minecraft.getInstance().gameRenderer.mainCamera.position().y
//?}
        val viewerZ =
        //? if = 1.8.9 {
                /*viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * event.partialTicks
            *///?} else if >= 26.2 {
                /*Minecraft.getInstance().gameRenderer.mainCamera().position().z
            *///?} else {
            Minecraft.getInstance().gameRenderer.mainCamera.position().z
//?}
        val renderX = waypoint.pos.x - viewerX
        val renderY = waypoint.pos.y - viewerY
        val renderZ = waypoint.pos.z - viewerZ

        drawFilledBoundingBox(
            //? if = 1.8.9 {
            /*AxisAlignedBB(renderX, renderY, renderZ, renderX + 1, renderY + 1, renderZ + 1),
            *///?} else {
            context.poseStack(),
            //? if >= 26.2 {
            /*context.submitNodeCollector(),
            *///?} else {
            context.bufferSource(),
            //?}
            BlockBox(
                waypoint.pos,
                waypoint.pos.offset(1, 1, 1)
            ),
            //?}
            waypoint.boxColor
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
            renderX,
            renderY + 1,
            renderZ,
            waypoint.beamColor
            //? if = 1.8.9 {
            /*, event.partialTicks
            *///?}
        )
        renderWaypointText(
            //? if >= 26.1 {
            context.poseStack(),
            //? if >= 26.2 {
            /*context.submitNodeCollector(),
            *///?} else {
            context.bufferSource(),
            //?}
            //?}
            waypoint.text,
            waypoint.owner,
            //? if = 1.8.9 {
            /*waypoint.pos.up(2),
            *///?} else {
            waypoint.pos,
            //?}
            waypoint.renderText,
            waypoint.renderOwner,
            //? if = 1.8.9 {
            /*waypoint.textColor,
            waypoint.ownerColor,
            *///?} else {
            waypoint.textColor,
            waypoint.ownerColor,
            //?}
            waypoint.renderDistance,
            waypoint.distanceTextColor
            //? if = 1.8.9 {
            /*, event.partialTicks
            *///?}
        )
    }

    private fun drawFilledBoundingBox(
        //? if = 1.8.9 {
        /*aabb: AxisAlignedBB,
        *///?} else {
        matrices: PoseStack,
        //? if >= 26.2 {
        /*collector: SubmitNodeCollector,
        *///?} else {
        consumers: MultiBufferSource,
        //?}
        box: BlockBox,
        //?}
        //? if = 1.8.9 {
        /*c: OneColor
        *///?} else {
        c: PolyColor
        //?}
    ) {
        //? if = 1.8.9 {
        /*GlStateManager.enableBlend()
        GlStateManager.disableLighting()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.disableTexture2D()

        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer

        GlStateManager.color(c.red / 255f, c.green / 255f, c.blue / 255f, c.alpha / 255f)

        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        tessellator.draw()
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        tessellator.draw()

        GlStateManager.color(
            c.red / 255f * 0.8f,
            c.green / 255f * 0.8f,
            c.blue / 255f * 0.8f,
            c.alpha / 255f
        )

        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        tessellator.draw()
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        tessellator.draw()

        GlStateManager.color(
            c.red / 255f * 0.9f,
            c.green / 255f * 0.9f,
            c.blue / 255f * 0.9f,
            c.alpha / 255f
        )

        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        tessellator.draw()
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        tessellator.draw()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        *///?} else {
        val camera =
        //? if >= 26.2 {
                /*Minecraft.getInstance().gameRenderer.mainCamera()
            *///?} else {
            Minecraft.getInstance().gameRenderer.mainCamera
        //?}

        val camX = camera.position().x
        val camY = camera.position().y
        val camZ = camera.position().z

        matrices.pushPose()
        matrices.translate(-camX, -camY, -camZ)

        //? if >= 26.2 {
        /*collector.submitCustomGeometry(
            matrices,
            RenderTypes.debugFilledBox()
        ) { pose, buffer ->
            *///?} else {
        val buffer = consumers.getBuffer(RenderTypes.debugFilledBox())
        val pose = matrices.last().pose()
        //?}

        val r = c.red / 255f
        val g = c.green / 255f
        val b = c.blue / 255f
        val a = c.alpha / 255f

        val minX = box.min().x.toFloat()
        val minY = box.min().y.toFloat()
        val minZ = box.min().z.toFloat()

        val maxX = box.max().x.toFloat()
        val maxY = box.max().y.toFloat()
        val maxZ = box.max().z.toFloat()

        addDoubleSidedQuad(
            buffer, pose,
            minX, minY, minZ,
            maxX, minY, minZ,
            maxX, minY, maxZ,
            minX, minY, maxZ,
            r, g, b, a
        )

        addDoubleSidedQuad(
            buffer, pose,
            minX, maxY, maxZ,
            maxX, maxY, maxZ,
            maxX, maxY, minZ,
            minX, maxY, minZ,
            r, g, b, a
        )

        addDoubleSidedQuad(
            buffer, pose,
            minX, minY, minZ,
            maxX, minY, minZ,
            maxX, maxY, minZ,
            minX, maxY, minZ,
            r, g, b, a
        )

        addDoubleSidedQuad(
            buffer, pose,
            minX, minY, maxZ,
            maxX, minY, maxZ,
            maxX, maxY, maxZ,
            minX, maxY, maxZ,
            r, g, b, a
        )

        addDoubleSidedQuad(
            buffer, pose,
            minX, minY, minZ,
            minX, minY, maxZ,
            minX, maxY, maxZ,
            minX, maxY, minZ,
            r, g, b, a
        )

        addDoubleSidedQuad(
            buffer, pose,
            maxX, minY, minZ,
            maxX, minY, maxZ,
            maxX, maxY, maxZ,
            maxX, maxY, minZ,
            r, g, b, a
        )
        //? if >= 26.2 {
        /*}
        *///?}

        matrices.popPose()
        //?}
    }

    //? if >= 26.1 {
    private fun addDoubleSidedQuad(
        buffer: VertexConsumer,
        //? if >= 26.2 {
        /*pose: PoseStack.Pose,
        *///?} else {
        pose: Matrix4f,
        //?}
        x1: Float, y1: Float, z1: Float,
        x2: Float, y2: Float, z2: Float,
        x3: Float, y3: Float, z3: Float,
        x4: Float, y4: Float, z4: Float,
        r: Float, g: Float, b: Float, a: Float
    ) {
        buffer.addVertex(pose, x1, y1, z1).setColor(r, g, b, a)
        buffer.addVertex(pose, x2, y2, z2).setColor(r, g, b, a)
        buffer.addVertex(pose, x3, y3, z3).setColor(r, g, b, a)
        buffer.addVertex(pose, x4, y4, z4).setColor(r, g, b, a)

        buffer.addVertex(pose, x4, y4, z4).setColor(r, g, b, a)
        buffer.addVertex(pose, x3, y3, z3).setColor(r, g, b, a)
        buffer.addVertex(pose, x2, y2, z2).setColor(r, g, b, a)
        buffer.addVertex(pose, x1, y1, z1).setColor(r, g, b, a)
    }
    //?}

    private fun renderBeaconBeam(
        //? if >= 26.1 {
        matrices: PoseStack,
        //? if >= 26.2 {
        /*collector: SubmitNodeCollector,
        *///?} else {
        consumers: MultiBufferSource,
        //?}
        //?}
        x: Double,
        y: Double,
        z: Double,
        //? if = 1.8.9 {
        /*c: OneColor
        *///?} else {
        c: PolyColor
        //?}
        //? if = 1.8.9 {
        /*, partialTicks: Float
        *///?}
    ) {
        //? if = 1.8.9 {
        /*val height = 300
        val bottomOffset = 0
        val topOffset = bottomOffset + height

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

        val time = Minecraft.getMinecraft().theWorld.totalWorldTime + partialTicks.toDouble()
        val d1 = MathHelper.func_181162_h(-time * 0.2 - MathHelper.floor_double(-time * 0.1).toDouble())

        val r = c.red / 255f
        val g = c.green / 255f
        val b = c.blue / 255f
        val alphaMultiplier = c.alpha / 255f

        val d2 = time * 0.025 * -1.5
        val d4 = 0.5 + cos(d2 + 2.356194490192345) * 0.2
        val d5 = 0.5 + sin(d2 + 2.356194490192345) * 0.2
        val d6 = 0.5 + cos(d2 + (Math.PI / 4)) * 0.2
        val d7 = 0.5 + sin(d2 + (Math.PI / 4)) * 0.2
        val d8 = 0.5 + cos(d2 + 3.9269908169872414) * 0.2
        val d9 = 0.5 + sin(d2 + 3.9269908169872414) * 0.2
        val d10 = 0.5 + cos(d2 + 5.497787143782138) * 0.2
        val d11 = 0.5 + sin(d2 + 5.497787143782138) * 0.2
        val d14 = -1.0 + d1
        val d15 = height.toDouble() * 2.5 + d14
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
        worldrenderer.pos(x + d4, y + topOffset, z + d5).tex(1.0, d15).color(r, g, b, alphaMultiplier).endVertex()
        worldrenderer.pos(x + d4, y + bottomOffset, z + d5).tex(1.0, d14).color(r, g, b, 1.0F).endVertex()
        worldrenderer.pos(x + d6, y + bottomOffset, z + d7).tex(0.0, d14).color(r, g, b, 1.0F).endVertex()
        worldrenderer.pos(x + d6, y + topOffset, z + d7).tex(0.0, d15).color(r, g, b, alphaMultiplier).endVertex()
        worldrenderer.pos(x + d10, y + topOffset, z + d11).tex(1.0, d15).color(r, g, b, alphaMultiplier).endVertex()
        worldrenderer.pos(x + d10, y + bottomOffset, z + d11).tex(1.0, d14).color(r, g, b, 1.0F).endVertex()
        worldrenderer.pos(x + d8, y + bottomOffset, z + d9).tex(0.0, d14).color(r, g, b, 1.0F).endVertex()
        worldrenderer.pos(x + d8, y + topOffset, z + d9).tex(0.0, d15).color(r, g, b, alphaMultiplier).endVertex()
        worldrenderer.pos(x + d6, y + topOffset, z + d7).tex(1.0, d15).color(r, g, b, alphaMultiplier).endVertex()
        worldrenderer.pos(x + d6, y + bottomOffset, z + d7).tex(1.0, d14).color(r, g, b, 1.0F).endVertex()
        worldrenderer.pos(x + d10, y + bottomOffset, z + d11).tex(0.0, d14).color(r, g, b, 1.0F).endVertex()
        worldrenderer.pos(x + d10, y + topOffset, z + d11).tex(0.0, d15).color(r, g, b, alphaMultiplier).endVertex()
        worldrenderer.pos(x + d8, y + topOffset, z + d9).tex(1.0, d15).color(r, g, b, alphaMultiplier).endVertex()
        worldrenderer.pos(x + d8, y + bottomOffset, z + d9).tex(1.0, d14).color(r, g, b, 1.0F).endVertex()
        worldrenderer.pos(x + d4, y + bottomOffset, z + d5).tex(0.0, d14).color(r, g, b, 1.0F).endVertex()
        worldrenderer.pos(x + d4, y + topOffset, z + d5).tex(0.0, d15).color(r, g, b, alphaMultiplier).endVertex()
        tessellator.draw()

        GlStateManager.disableCull()
        val d12 = -1.0 + d1
        val d13 = height.toDouble() + d12

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
        worldrenderer.pos(x + 0.2, y + topOffset, z + 0.2).tex(1.0, d13).color(r, g, b, 0.25F * alphaMultiplier)
            .endVertex()
        worldrenderer.pos(x + 0.2, y + bottomOffset, z + 0.2).tex(1.0, d12).color(r, g, b, 0.25F).endVertex()
        worldrenderer.pos(x + 0.8, y + bottomOffset, z + 0.2).tex(0.0, d12).color(r, g, b, 0.25F).endVertex()
        worldrenderer.pos(x + 0.8, y + topOffset, z + 0.2).tex(0.0, d13).color(r, g, b, 0.25F * alphaMultiplier)
            .endVertex()
        worldrenderer.pos(x + 0.8, y + topOffset, z + 0.8).tex(1.0, d13).color(r, g, b, 0.25F * alphaMultiplier)
            .endVertex()
        worldrenderer.pos(x + 0.8, y + bottomOffset, z + 0.8).tex(1.0, d12).color(r, g, b, 0.25F).endVertex()
        worldrenderer.pos(x + 0.2, y + bottomOffset, z + 0.8).tex(0.0, d12).color(r, g, b, 0.25F).endVertex()
        worldrenderer.pos(x + 0.2, y + topOffset, z + 0.8).tex(0.0, d13).color(r, g, b, 0.25F * alphaMultiplier)
            .endVertex()
        worldrenderer.pos(x + 0.8, y + topOffset, z + 0.2).tex(1.0, d13).color(r, g, b, 0.25F * alphaMultiplier)
            .endVertex()
        worldrenderer.pos(x + 0.8, y + bottomOffset, z + 0.2).tex(1.0, d12).color(r, g, b, 0.25F).endVertex()
        worldrenderer.pos(x + 0.8, y + bottomOffset, z + 0.8).tex(0.0, d12).color(r, g, b, 0.25F).endVertex()
        worldrenderer.pos(x + 0.8, y + topOffset, z + 0.8).tex(0.0, d13).color(r, g, b, 0.25F * alphaMultiplier)
            .endVertex()
        worldrenderer.pos(x + 0.2, y + topOffset, z + 0.8).tex(1.0, d13).color(r, g, b, 0.25F * alphaMultiplier)
            .endVertex()
        worldrenderer.pos(x + 0.2, y + bottomOffset, z + 0.8).tex(1.0, d12).color(r, g, b, 0.25F).endVertex()
        worldrenderer.pos(x + 0.2, y + bottomOffset, z + 0.2).tex(0.0, d12).color(r, g, b, 0.25F).endVertex()
        worldrenderer.pos(x + 0.2, y + topOffset, z + 0.2).tex(0.0, d13).color(r, g, b, 0.25F * alphaMultiplier)
            .endVertex()
        tessellator.draw()
        *///?} else {
        matrices.pushPose()
        matrices.translate(x, y, z)

        //? if >= 26.2 {
        /*collector.submitCustomGeometry(
            matrices,
            RenderTypes.beaconBeam(BEAM_TEXTURE, true)
        ) { pose, buffer ->
            *///?} else {
        val pose = matrices.last()
        val buffer = consumers.getBuffer(RenderTypes.beaconBeam(BEAM_TEXTURE, true))
        //?}

        val gameTime = Minecraft.getInstance().level!!.gameTime
        val tickDelta = Minecraft.getInstance().deltaTracker.gameTimeDeltaTicks
        val time = gameTime + tickDelta.toDouble()

        val t1 = -time * 0.2
        val d1 = t1 - floor(t1)

        val r = c.red / 255f
        val g = c.green / 255f
        val b = c.blue / 255f
        val alphaMultiplier = c.alpha / 255f

        val d2 = time * 0.025 * -1.5
        val d4 = (0.5 + cos(d2 + 2.356194490192345) * 0.2).toFloat()
        val d5 = (0.5 + sin(d2 + 2.356194490192345) * 0.2).toFloat()
        val d6 = (0.5 + cos(d2 + (Math.PI / 4)) * 0.2).toFloat()
        val d7 = (0.5 + sin(d2 + (Math.PI / 4)) * 0.2).toFloat()
        val d8 = (0.5 + cos(d2 + 3.9269908169872414) * 0.2).toFloat()
        val d9 = (0.5 + sin(d2 + 3.9269908169872414) * 0.2).toFloat()
        val d10 = (0.5 + cos(d2 + 5.497787143782138) * 0.2).toFloat()
        val d11 = (0.5 + sin(d2 + 5.497787143782138) * 0.2).toFloat()

        val d14 = (-1.0 + d1).toFloat()
        val d15 = (300.0 * 2.5 + d14.toDouble()).toFloat()

        val topOffset = 300.0f
        val bottomOffset = 0.0f

        renderBeamSide(
            pose,
            buffer,
            r,
            g,
            b,
            alphaMultiplier,
            bottomOffset,
            topOffset,
            d4,
            d5,
            d6,
            d7,
            1.0f,
            0.0f,
            d14,
            d15
        )
        renderBeamSide(
            pose,
            buffer,
            r,
            g,
            b,
            alphaMultiplier,
            bottomOffset,
            topOffset,
            d10,
            d11,
            d8,
            d9,
            1.0f,
            0.0f,
            d14,
            d15
        )
        renderBeamSide(
            pose,
            buffer,
            r,
            g,
            b,
            alphaMultiplier,
            bottomOffset,
            topOffset,
            d6,
            d7,
            d10,
            d11,
            1.0f,
            0.0f,
            d14,
            d15
        )
        renderBeamSide(
            pose,
            buffer,
            r,
            g,
            b,
            alphaMultiplier,
            bottomOffset,
            topOffset,
            d8,
            d9,
            d4,
            d5,
            1.0f,
            0.0f,
            d14,
            d15
        )

        val d12 = (-1.0 + d1).toFloat()
        val d13 = 300.0f + d12
        val innerAlpha = 0.25f * alphaMultiplier

        renderBeamSide(
            pose,
            buffer,
            r,
            g,
            b,
            innerAlpha,
            bottomOffset,
            topOffset,
            0.2f,
            0.2f,
            0.8f,
            0.2f,
            1.0f,
            0.0f,
            d12,
            d13
        )
        renderBeamSide(
            pose,
            buffer,
            r,
            g,
            b,
            innerAlpha,
            bottomOffset,
            topOffset,
            0.8f,
            0.8f,
            0.2f,
            0.8f,
            1.0f,
            0.0f,
            d12,
            d13
        )
        renderBeamSide(
            pose,
            buffer,
            r,
            g,
            b,
            innerAlpha,
            bottomOffset,
            topOffset,
            0.8f,
            0.2f,
            0.8f,
            0.8f,
            1.0f,
            0.0f,
            d12,
            d13
        )
        renderBeamSide(
            pose,
            buffer,
            r,
            g,
            b,
            innerAlpha,
            bottomOffset,
            topOffset,
            0.2f,
            0.8f,
            0.2f,
            0.2f,
            1.0f,
            0.0f,
            d12,
            d13
        )
        //? if >= 26.2 {
        /*}
        *///?}

        matrices.popPose()
        //?}
    }

    //? if >= 26.1 {
    private fun renderBeamSide(
        pose: PoseStack.Pose,
        buffer: VertexConsumer,
        r: Float, g: Float, b: Float, a: Float,
        yMin: Float, yMax: Float,
        x1: Float, z1: Float,
        x2: Float, z2: Float,
        u1: Float, u2: Float,
        v1: Float, v2: Float
    ) {
        buffer.addVertex(pose.pose(), x1, yMax, z1).setColor(r, g, b, a).setUv(u1, v2).setUv2(15, 15)
            .setNormal(pose, 0.0f, 1.0f, 0.0f)
        buffer.addVertex(pose.pose(), x1, yMin, z1).setColor(r, g, b, a).setUv(u1, v1).setUv2(15, 15)
            .setNormal(pose, 0.0f, 1.0f, 0.0f)
        buffer.addVertex(pose.pose(), x2, yMin, z2).setColor(r, g, b, a).setUv(u2, v1).setUv2(15, 15)
            .setNormal(pose, 0.0f, 1.0f, 0.0f)
        buffer.addVertex(pose.pose(), x2, yMax, z2).setColor(r, g, b, a).setUv(u2, v2).setUv2(15, 15)
            .setNormal(pose, 0.0f, 1.0f, 0.0f)
    }
    //?}

    private fun renderWaypointText(
        //? if >= 26.1 {
        matrices: PoseStack,
        //? if >= 26.2 {
        /*collector: SubmitNodeCollector,
        *///?} else {
        consumers: MultiBufferSource,
        //?}
        //?}
        str: String,
        owner: String,
        loc: BlockPos,
        renderText: Boolean,
        renderOwner: Boolean,
        //? if = 1.8.9 {
        /*textColor: OneColor,
        ownerColor: OneColor,
        *///?} else {
        textColor: PolyColor,
        ownerColor: PolyColor,
        //?}
        renderDistance: Boolean,
        //? if = 1.8.9 {
        /*distanceTextColor: OneColor
        *///?} else {
        distanceTextColor: PolyColor
        //?}
        //? if = 1.8.9 {
        /*, partialTicks: Float
        *///?}
    ) {
        //? if = 1.8.9 {
        /*GlStateManager.alphaFunc(516, 0.1F)

        GlStateManager.pushMatrix()

        val viewer = Minecraft.getMinecraft().renderViewEntity
        val viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks
        val viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks
        val viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks

        var x = loc.x + 0.5 - viewerX
        var y = loc.y - viewerY - viewer.eyeHeight
        var z = loc.z + 0.5 - viewerZ

        val distSq = x * x + y * y + z * z
        val dist = sqrt(distSq)
        if (distSq > 144) {
            x *= 12 / dist
            y *= 12 / dist
            z *= 12 / dist
        }
        GlStateManager.translate(x, y, z)
        GlStateManager.translate(0.0, viewer.eyeHeight.toDouble(), 0.0)

        if (renderOwner && !owner.isEmpty()) {
            drawNametag(owner, true, ownerColor)

            GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewY, 0.0F, 1.0F, 0.0F)
            GlStateManager.rotate(Minecraft.getMinecraft().renderManager.playerViewX, 1.0F, 0.0F, 0.0F)
            GlStateManager.translate(0.0f, -0.25f, 0.0f)
            GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewX, 1.0F, 0.0F, 0.0F)
            GlStateManager.rotate(Minecraft.getMinecraft().renderManager.playerViewY, 0.0F, 1.0F, 0.0F)
        }

        if (renderText && !str.isEmpty()) {
            drawNametag(str, true, textColor)

            GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewY, 0.0F, 1.0F, 0.0F)
            GlStateManager.rotate(Minecraft.getMinecraft().renderManager.playerViewX, 1.0F, 0.0F, 0.0F)
            GlStateManager.translate(0.0f, -0.25f, 0.0f)
            GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewX, 1.0F, 0.0F, 0.0F)
            GlStateManager.rotate(Minecraft.getMinecraft().renderManager.playerViewY, 0.0F, 1.0F, 0.0F)
        }

        if (renderDistance) {
            drawNametag("${dist.roundToInt()}m", true, distanceTextColor)
        }

        GlStateManager.popMatrix()

        GlStateManager.disableLighting()
        *///?} else {
        if (!renderText && !renderDistance && !renderOwner) return

        val camera =
        //? if >= 26.2 {
                /*Minecraft.getInstance().gameRenderer.mainCamera()
            *///?} else {
            Minecraft.getInstance().gameRenderer.mainCamera
        //?}

        val viewerX = camera.position().x
        val viewerY = camera.position().y
        val viewerZ = camera.position().z

        val renderX = loc.x + 0.5 - viewerX
        val renderY = loc.y + 2.0 - viewerY
        val renderZ = loc.z + 0.5 - viewerZ

        val dist = sqrt(
            Minecraft.getInstance().player!!.distanceToSqr(
                loc.x.toDouble(),
                loc.y.toDouble(),
                loc.z.toDouble()
            )
        )

        matrices.pushPose()
        matrices.translate(renderX, renderY, renderZ)
        matrices.mulPose(Axis.YP.rotationDegrees(-camera.yRot()))
        matrices.mulPose(Axis.XP.rotationDegrees(camera.xRot()))

        val scale = 0.025f
        matrices.scale(-scale, -scale, scale)

        val matrix = matrices.last().pose()
        val background = (Minecraft.getInstance().options.textBackgroundOpacity().get() * 255.0).toInt() shl 24
        var line = 0

        val textColorArgb = (textColor.alpha shl 24) or (textColor.red shl 16) or (textColor.green shl 8) or textColor.blue
        val ownerColorArgb = (ownerColor.alpha shl 24) or (ownerColor.red shl 16) or (ownerColor.green shl 8) or ownerColor.blue

        if (renderOwner && !owner.isEmpty()) {
            val width = -Minecraft.getInstance().font.width(owner) / 2f
            //? if >= 26.2 {
            /*collector.submitText(
                matrices,
                width,
                line.toFloat(),
                Component.literal(owner).visualOrderText,
                false,
                Font.DisplayMode.SEE_THROUGH,
                15728880,
                ownerColorArgb,
                background,
                0
            )
            *///?} else {
            Minecraft.getInstance().font.drawInBatch(
                Component.literal(owner),
                width,
                line.toFloat(),
                ownerColorArgb,
                false,
                matrix,
                consumers,
                Font.DisplayMode.SEE_THROUGH,
                background,
                15728880
            )
            //?}
            line += 10
        }

        if (renderText && !str.isEmpty()) {
            val width = -Minecraft.getInstance().font.width(str) / 2f
            //? if >= 26.2 {
            /*collector.submitText(
                matrices,
                width,
                line.toFloat(),
                Component.literal(str).visualOrderText,
                false,
                Font.DisplayMode.SEE_THROUGH,
                15728880,
                textColorArgb,
                background,
                0
            )
            *///?} else {
            Minecraft.getInstance().font.drawInBatch(
                Component.literal(str),
                width,
                line.toFloat(),
                textColorArgb,
                false,
                matrix,
                consumers,
                Font.DisplayMode.SEE_THROUGH,
                background,
                15728880
            )
            //?}
            line += 10
        }

        if (renderDistance) {
            val distText = "${dist.toInt()}m"
            val width = -Minecraft.getInstance().font.width(distText) / 2f
            val distanceColorArgb = (distanceTextColor.alpha shl 24) or (distanceTextColor.red shl 16) or (distanceTextColor.green shl 8) or distanceTextColor.blue
            //? if >= 26.2 {
            /*collector.submitText(
                matrices,
                width,
                line.toFloat(),
                Component.literal(distText).visualOrderText,
                false,
                Font.DisplayMode.SEE_THROUGH,
                15728880,
                distanceColorArgb,
                background,
                0
            )
            *///?} else {
            Minecraft.getInstance().font.drawInBatch(
                Component.literal(distText),
                width,
                line.toFloat(),
                distanceColorArgb,
                false,
                matrix,
                consumers,
                Font.DisplayMode.SEE_THROUGH,
                background,
                15728880
            )
            //?}
        }
        matrices.popPose()
        //?}
    }

    //? if = 1.8.9 {
    /*private fun drawNametag(str: String, render: Boolean, color: OneColor) {
        if (!render) return

        val fontrenderer = Minecraft.getMinecraft().fontRendererObj
        val f = 1.6F
        val f1 = 0.016666668F * f
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
        val i = 0

        val j = fontrenderer.getStringWidth(str) / 2
        GlStateManager.disableTexture2D()
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
        worldrenderer.pos(-j - 1.0, -1.0 + i, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex()
        worldrenderer.pos(-j - 1.0, 8.0 + i, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex()
        worldrenderer.pos(j + 1.0, 8.0 + i, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex()
        worldrenderer.pos(j + 1.0, -1.0 + i, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex()
        tessellator.draw()
        GlStateManager.enableTexture2D()
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127)
        GlStateManager.depthMask(true)

        val argb = (color.alpha shl 24) or (color.red shl 16) or (color.green shl 8) or color.blue
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, argb)

        GlStateManager.enableDepth()
        GlStateManager.enableBlend()
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F)
        GlStateManager.popMatrix()
    }
    *///?}
}