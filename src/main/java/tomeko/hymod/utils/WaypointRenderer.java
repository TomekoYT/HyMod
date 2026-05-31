package tomeko.hymod.utils;

import net.minecraft.client.Minecraft;
//? if = 1.8.9 {
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
//?} else {
/*import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockBox;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
*///?}

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WaypointRenderer {
    private static final String BEACON_PNG = "textures/entity/beacon_beam.png";
    //? if = 1.8.9 {
    private static final ResourceLocation BEAM_TEXTURE = new ResourceLocation(BEACON_PNG);
    //?} else {
    /*private static final Identifier BEAM_TEXTURE = Identifier.parse(BEACON_PNG);
    *///?}

    public static final List<Waypoint> waypoints = new ArrayList<>();

    public static void register() {
        //? if = 1.8.9 {
        MinecraftForge.EVENT_BUS.register(new WaypointRenderer());
         //?} else {
        /*WorldRenderEvents.AFTER_ENTITIES.register(WaypointRenderer::onWorldRender);
        ClientTickEvents.END_CLIENT_TICK.register(WaypointRenderer::onTick);
        *///?}
    }

    //? if = 1.8.9 {
    @SubscribeEvent
     //?} else {
    /*static
            *///?}
    public void onWorldRender(
            //? if = 1.8.9 {
            RenderWorldLastEvent event
             //?} else {
            /*WorldRenderContext context
            *///?}
    ) {
        for (Waypoint waypoint : waypoints) {
            renderWaypoint(
                    waypoint,
                    //? if = 1.8.9 {
                    event
                     //?} else {
                    /*context
                    *///?}
            );
        }
    }

    //? if = 1.8.9 {
    @SubscribeEvent
     //?} else {
    /*static
            *///?}
    public void onTick(
            //? if = 1.8.9 {
            TickEvent.ClientTickEvent event
             //?} else {
            /*Minecraft mc
            *///?}
    ) {
        //? if = 1.8.9 {
        if (event.phase != TickEvent.Phase.END) return;
         //?}

        Iterator<Waypoint> iterator = waypoints.iterator();
        while (iterator.hasNext()) {
            Waypoint waypoint = iterator.next();
            waypoint.tickTime--;

            if (waypoint.tickTime <= 0) {
                iterator.remove();
            }
        }
    }

    private static void renderWaypoint(
            Waypoint waypoint,
            //? if = 1.8.9 {
            RenderWorldLastEvent event
             //?} else {
            /*WorldRenderContext context
            *///?}
    ) {
        if (waypoint == null) return;

        //? if >= 1.21.9 {
        /*if (Minecraft.getInstance().player == null || Minecraft.getInstance().level == null) return;
        *///?}

        //? if = 1.8.9 {
        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
         //?}

        double viewerX =
                //? if = 1.8.9 {
                viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * event.partialTicks;
                 //?} else {
                /*Minecraft.getInstance().gameRenderer.getMainCamera().position().x;
        *///?}
        double viewerY =
                //? if = 1.8.9 {
                viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * event.partialTicks;
                 //?} else {
                /*Minecraft.getInstance().gameRenderer.getMainCamera().position().y;
        *///?}
        double viewerZ =
                //? if = 1.8.9 {
                viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * event.partialTicks;
                 //?} else {
                /*Minecraft.getInstance().gameRenderer.getMainCamera().position().z;
        *///?}

        double renderX = waypoint.pos.getX() - viewerX;
        double renderY = waypoint.pos.getY() - viewerY;
        double renderZ = waypoint.pos.getZ() - viewerZ;

        drawFilledBoundingBox(
                //? if = 1.8.9 {
                new AxisAlignedBB(renderX, renderY, renderZ, renderX + 1, renderY + 1, renderZ + 1),
                //?} else {
                /*context,
                new BlockBox(
                        waypoint.pos,
                        waypoint.pos.offset(1, 1, 1)
                ),
                *///?}
                waypoint.color,
                waypoint.boxOpacity
        );
        renderBeaconBeam(
                //? if >= 1.21.9 {
                /*context,
                *///?}
                renderX,
                renderY + 1,
                renderZ,
                waypoint.color.getRGB(),
                waypoint.beamOpacity
                //? if = 1.8.9 {
                , event.partialTicks
                 //?}
        );
        renderWaypointText(
                //? if >= 1.21.9 {
                /*context,
                *///?}
                waypoint.text,
                //? if = 1.8.9 {
                waypoint.pos.up(2),
                 //?} else {
                /*waypoint.pos,
                *///?}
                waypoint.renderText,
                waypoint.renderDistance
                //? if = 1.8.9 {
                , event.partialTicks
                 //?}
        );
    }

    private static void drawFilledBoundingBox(
            //? if = 1.8.9 {
            AxisAlignedBB aabb,
             //?} else {
            /*WorldRenderContext context,
            BlockBox box,
            *///?}
            Color c,
            float alphaMultiplier
    ) {
        //? if = 1.8.9 {
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.color(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f * alphaMultiplier);

        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        tessellator.draw();


        GlStateManager.color(c.getRed() / 255f * 0.8f, c.getGreen() / 255f * 0.8f, c.getBlue() / 255f * 0.8f, c.getAlpha() / 255f * alphaMultiplier);

        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        tessellator.draw();


        GlStateManager.color(c.getRed() / 255f * 0.9f, c.getGreen() / 255f * 0.9f, c.getBlue() / 255f * 0.9f, c.getAlpha() / 255f * alphaMultiplier);

        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        //?} else {
        /*Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

        double camX = camera.position().x;
        double camY = camera.position().y;
        double camZ = camera.position().z;

        context.matrices().pushPose();
        context.matrices().translate(-camX, -camY, -camZ);

        VertexConsumer buffer = context.consumers().getBuffer(RenderTypes.debugFilledBox());
        Matrix4f pose = context.matrices().last().pose();

        float r = c.getRed() / 255f;
        float g = c.getGreen() / 255f;
        float b = c.getBlue() / 255f;

        float minX = box.min().getX();
        float minY = box.min().getY();
        float minZ = box.min().getZ();

        float maxX = box.max().getX();
        float maxY = box.max().getY();
        float maxZ = box.max().getZ();

        addDoubleSidedQuad(
                buffer, pose,
                minX, minY, minZ,
                maxX, minY, minZ,
                maxX, minY, maxZ,
                minX, minY, maxZ,
                r, g, b, alphaMultiplier
        );

        addDoubleSidedQuad(
                buffer, pose,
                minX, maxY, maxZ,
                maxX, maxY, maxZ,
                maxX, maxY, minZ,
                minX, maxY, minZ,
                r, g, b, alphaMultiplier
        );

        addDoubleSidedQuad(
                buffer, pose,
                minX, minY, minZ,
                maxX, minY, minZ,
                maxX, maxY, minZ,
                minX, maxY, minZ,
                r, g, b, alphaMultiplier
        );

        addDoubleSidedQuad(
                buffer, pose,
                minX, minY, maxZ,
                maxX, minY, maxZ,
                maxX, maxY, maxZ,
                minX, maxY, maxZ,
                r, g, b, alphaMultiplier
        );

        addDoubleSidedQuad(
                buffer, pose,
                minX, minY, minZ,
                minX, minY, maxZ,
                minX, maxY, maxZ,
                minX, maxY, minZ,
                r, g, b, alphaMultiplier
        );

        addDoubleSidedQuad(
                buffer, pose,
                maxX, minY, minZ,
                maxX, minY, maxZ,
                maxX, maxY, maxZ,
                maxX, maxY, minZ,
                r, g, b, alphaMultiplier
        );

        context.matrices().popPose();
        *///?}
    }

    //? if >= 1.21.9 {
    /*private static void addDoubleSidedQuad(
            VertexConsumer buffer,
            Matrix4f pose,
            float x1, float y1, float z1,
            float x2, float y2, float z2,
            float x3, float y3, float z3,
            float x4, float y4, float z4,
            float r, float g, float b, float a
    ) {
        buffer.addVertex(pose, x1, y1, z1).setColor(r, g, b, a);
        buffer.addVertex(pose, x2, y2, z2).setColor(r, g, b, a);
        buffer.addVertex(pose, x3, y3, z3).setColor(r, g, b, a);
        buffer.addVertex(pose, x4, y4, z4).setColor(r, g, b, a);

        buffer.addVertex(pose, x4, y4, z4).setColor(r, g, b, a);
        buffer.addVertex(pose, x3, y3, z3).setColor(r, g, b, a);
        buffer.addVertex(pose, x2, y2, z2).setColor(r, g, b, a);
        buffer.addVertex(pose, x1, y1, z1).setColor(r, g, b, a);
    }
    *///?}

    private static void renderBeaconBeam(
            //? if >= 1.21.9 {
            /*WorldRenderContext context,
            *///?}
            double x,
            double y,
            double z,
            int rgb,
            float alphaMultiplier
            //? if = 1.8.9 {
            , float partialTicks
             //?}
    ) {
        //? if = 1.8.9 {
        int height = 300;
        int bottomOffset = 0;
        int topOffset = bottomOffset + height;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        Minecraft.getMinecraft().getTextureManager().bindTexture(BEAM_TEXTURE);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
        GlStateManager.disableLighting();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        double time = Minecraft.getMinecraft().theWorld.getTotalWorldTime() + (double) partialTicks;
        double d1 = MathHelper.func_181162_h(-time * 0.2D - (double) MathHelper.floor_double(-time * 0.1D));

        float r = ((rgb >> 16) & 0xFF) / 255f;
        float g = ((rgb >> 8) & 0xFF) / 255f;
        float b = (rgb & 0xFF) / 255f;
        double d2 = time * 0.025D * -1.5D;
        double d4 = 0.5D + Math.cos(d2 + 2.356194490192345D) * 0.2D;
        double d5 = 0.5D + Math.sin(d2 + 2.356194490192345D) * 0.2D;
        double d6 = 0.5D + Math.cos(d2 + (Math.PI / 4D)) * 0.2D;
        double d7 = 0.5D + Math.sin(d2 + (Math.PI / 4D)) * 0.2D;
        double d8 = 0.5D + Math.cos(d2 + 3.9269908169872414D) * 0.2D;
        double d9 = 0.5D + Math.sin(d2 + 3.9269908169872414D) * 0.2D;
        double d10 = 0.5D + Math.cos(d2 + 5.497787143782138D) * 0.2D;
        double d11 = 0.5D + Math.sin(d2 + 5.497787143782138D) * 0.2D;
        double d14 = -1.0D + d1;
        double d15 = (double) (height) * 2.5D + d14;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(x + d4, y + topOffset, z + d5).tex(1.0D, d15).color(r, g, b, alphaMultiplier).endVertex();
        worldrenderer.pos(x + d4, y + bottomOffset, z + d5).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d6, y + bottomOffset, z + d7).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d6, y + topOffset, z + d7).tex(0.0D, d15).color(r, g, b, alphaMultiplier).endVertex();
        worldrenderer.pos(x + d10, y + topOffset, z + d11).tex(1.0D, d15).color(r, g, b, alphaMultiplier).endVertex();
        worldrenderer.pos(x + d10, y + bottomOffset, z + d11).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d8, y + bottomOffset, z + d9).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d8, y + topOffset, z + d9).tex(0.0D, d15).color(r, g, b, alphaMultiplier).endVertex();
        worldrenderer.pos(x + d6, y + topOffset, z + d7).tex(1.0D, d15).color(r, g, b, alphaMultiplier).endVertex();
        worldrenderer.pos(x + d6, y + bottomOffset, z + d7).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d10, y + bottomOffset, z + d11).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d10, y + topOffset, z + d11).tex(0.0D, d15).color(r, g, b, alphaMultiplier).endVertex();
        worldrenderer.pos(x + d8, y + topOffset, z + d9).tex(1.0D, d15).color(r, g, b, alphaMultiplier).endVertex();
        worldrenderer.pos(x + d8, y + bottomOffset, z + d9).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d4, y + bottomOffset, z + d5).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d4, y + topOffset, z + d5).tex(0.0D, d15).color(r, g, b, alphaMultiplier).endVertex();
        tessellator.draw();

        GlStateManager.disableCull();
        double d12 = -1.0D + d1;
        double d13 = height + d12;

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.2D).tex(1.0D, d13).color(r, g, b, 0.25F * alphaMultiplier).endVertex();
        worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.2D).tex(1.0D, d12).color(r, g, b, 0.25F).endVertex();
        worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.2D).tex(0.0D, d12).color(r, g, b, 0.25F).endVertex();
        worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.2D).tex(0.0D, d13).color(r, g, b, 0.25F * alphaMultiplier).endVertex();
        worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.8D).tex(1.0D, d13).color(r, g, b, 0.25F * alphaMultiplier).endVertex();
        worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.8D).tex(1.0D, d12).color(r, g, b, 0.25F).endVertex();
        worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.8D).tex(0.0D, d12).color(r, g, b, 0.25F).endVertex();
        worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.8D).tex(0.0D, d13).color(r, g, b, 0.25F * alphaMultiplier).endVertex();
        worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.2D).tex(1.0D, d13).color(r, g, b, 0.25F * alphaMultiplier).endVertex();
        worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.2D).tex(1.0D, d12).color(r, g, b, 0.25F).endVertex();
        worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.8D).tex(0.0D, d12).color(r, g, b, 0.25F).endVertex();
        worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.8D).tex(0.0D, d13).color(r, g, b, 0.25F * alphaMultiplier).endVertex();
        worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.8D).tex(1.0D, d13).color(r, g, b, 0.25F * alphaMultiplier).endVertex();
        worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.8D).tex(1.0D, d12).color(r, g, b, 0.25F).endVertex();
        worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.2D).tex(0.0D, d12).color(r, g, b, 0.25F).endVertex();
        worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.2D).tex(0.0D, d13).color(r, g, b, 0.25F * alphaMultiplier).endVertex();
        tessellator.draw();
        //?} else {
        /*context.matrices().pushPose();
        context.matrices().translate(x, y, z);

        PoseStack.Pose pose = context.matrices().last();
        VertexConsumer buffer = context.consumers().getBuffer(RenderTypes.beaconBeam(BEAM_TEXTURE, true));

        long gameTime = Minecraft.getInstance().level.getGameTime();
        float tickDelta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks();
        double time = gameTime + (double) tickDelta;

        double t1 = -time * 0.2D;
        double d1 = t1 - Math.floor(t1);

        float r = ((rgb >> 16) & 0xFF) / 255f;
        float g = ((rgb >> 8) & 0xFF) / 255f;
        float b = (rgb & 0xFF) / 255f;

        double d2 = time * 0.025D * -1.5D;
        float d4 = (float) (0.5D + Math.cos(d2 + 2.356194490192345D) * 0.2D);
        float d5 = (float) (0.5D + Math.sin(d2 + 2.356194490192345D) * 0.2D);
        float d6 = (float) (0.5D + Math.cos(d2 + (Math.PI / 4D)) * 0.2D);
        float d7 = (float) (0.5D + Math.sin(d2 + (Math.PI / 4D)) * 0.2D);
        float d8 = (float) (0.5D + Math.cos(d2 + 3.9269908169872414D) * 0.2D);
        float d9 = (float) (0.5D + Math.sin(d2 + 3.9269908169872414D) * 0.2D);
        float d10 = (float) (0.5D + Math.cos(d2 + 5.497787143782138D) * 0.2D);
        float d11 = (float) (0.5D + Math.sin(d2 + 5.497787143782138D) * 0.2D);

        float d14 = (float) (-1.0D + d1);
        float d15 = (float) (300.0D * 2.5D + d14);

        float topOffset = 300.0f;
        float bottomOffset = 0.0f;

        renderBeamSide(pose, buffer, r, g, b, alphaMultiplier, bottomOffset, topOffset, d4, d5, d6, d7, 1.0f, 0.0f, d14, d15);
        renderBeamSide(pose, buffer, r, g, b, alphaMultiplier, bottomOffset, topOffset, d10, d11, d8, d9, 1.0f, 0.0f, d14, d15);
        renderBeamSide(pose, buffer, r, g, b, alphaMultiplier, bottomOffset, topOffset, d6, d7, d10, d11, 1.0f, 0.0f, d14, d15);
        renderBeamSide(pose, buffer, r, g, b, alphaMultiplier, bottomOffset, topOffset, d8, d9, d4, d5, 1.0f, 0.0f, d14, d15);

        float d12 = (float) (-1.0D + d1);
        float d13 = 300.0f + d12;
        float innerAlpha = 0.25f * alphaMultiplier;

        renderBeamSide(pose, buffer, r, g, b, innerAlpha, bottomOffset, topOffset, 0.2f, 0.2f, 0.8f, 0.2f, 1.0f, 0.0f, d12, d13);
        renderBeamSide(pose, buffer, r, g, b, innerAlpha, bottomOffset, topOffset, 0.8f, 0.8f, 0.2f, 0.8f, 1.0f, 0.0f, d12, d13);
        renderBeamSide(pose, buffer, r, g, b, innerAlpha, bottomOffset, topOffset, 0.8f, 0.2f, 0.8f, 0.8f, 1.0f, 0.0f, d12, d13);
        renderBeamSide(pose, buffer, r, g, b, innerAlpha, bottomOffset, topOffset, 0.2f, 0.8f, 0.2f, 0.2f, 1.0f, 0.0f, d12, d13);

        context.matrices().popPose();
        *///?}
    }

    //? if >= 1.21.9 {
    /*private static void renderBeamSide(
            PoseStack.Pose pose,
            VertexConsumer buffer,
            float r, float g, float b, float a,
            float yMin, float yMax,
            float x1, float z1,
            float x2, float z2,
            float u1, float u2,
            float v1, float v2
    ) {
        buffer.addVertex(pose.pose(), x1, yMax, z1).setColor(r, g, b, a).setUv(u1, v2).setUv2(15, 15).setNormal(pose, 0.0f, 1.0f, 0.0f);
        buffer.addVertex(pose.pose(), x1, yMin, z1).setColor(r, g, b, a).setUv(u1, v1).setUv2(15, 15).setNormal(pose, 0.0f, 1.0f, 0.0f);
        buffer.addVertex(pose.pose(), x2, yMin, z2).setColor(r, g, b, a).setUv(u2, v1).setUv2(15, 15).setNormal(pose, 0.0f, 1.0f, 0.0f);
        buffer.addVertex(pose.pose(), x2, yMax, z2).setColor(r, g, b, a).setUv(u2, v2).setUv2(15, 15).setNormal(pose, 0.0f, 1.0f, 0.0f);
    }
    *///?}

    private static void renderWaypointText(
            //? if >= 1.21.9 {
            /*WorldRenderContext context,
            *///?}
            String str,
            BlockPos loc,
            boolean renderText,
            boolean renderDistance
            //? if = 1.8.9 {
            , float partialTicks
             //?}
    ) {
        //? if = 1.8.9 {
        GlStateManager.alphaFunc(516, 0.1F);

        GlStateManager.pushMatrix();

        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        double x = loc.getX() + 0.5 - viewerX;
        double y = loc.getY() - viewerY - viewer.getEyeHeight();
        double z = loc.getZ() + 0.5 - viewerZ;

        double distSq = x * x + y * y + z * z;
        double dist = Math.sqrt(distSq);
        if (distSq > 144) {
            x *= 12 / dist;
            y *= 12 / dist;
            z *= 12 / dist;
        }
        GlStateManager.translate(x, y, z);
        GlStateManager.translate(0, viewer.getEyeHeight(), 0);

        drawNametag(str, renderText);

        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0, -0.25f, 0);
        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);

        drawNametag(EnumChatFormatting.YELLOW.toString() + Math.round(dist) + "m", renderDistance);

        GlStateManager.popMatrix();

        GlStateManager.disableLighting();
        //?} else {
        /*if (!renderText && !renderDistance) return;

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

        double viewerX = camera.position().x;
        double viewerY = camera.position().y;
        double viewerZ = camera.position().z;

        double renderX = loc.getX() + 0.5 - viewerX;
        double renderY = loc.getY() + 2.0 - viewerY;
        double renderZ = loc.getZ() + 0.5 - viewerZ;

        double dist = Math.sqrt(Minecraft.getInstance().player.distanceToSqr(loc.getX(), loc.getY(), loc.getZ()));

        context.matrices().pushPose();
        context.matrices().translate(renderX, renderY, renderZ);
        context.matrices().mulPose(Axis.YP.rotationDegrees(-camera.yRot()));
        context.matrices().mulPose(Axis.XP.rotationDegrees(camera.xRot()));

        float scale = 0.025f;
        context.matrices().scale(-scale, -scale, scale);

        Matrix4f matrix = context.matrices().last().pose();
        int background = (int) (Minecraft.getInstance().options.textBackgroundOpacity().get() * 255.0) << 24;
        int line = 0;

        if (renderText) {
            float width = -Minecraft.getInstance().font.width(str) / 2f;
            Minecraft.getInstance().font.drawInBatch(
                    Component.literal(str),
                    width,
                    line,
                    0xFFFFFFFF,
                    false,
                    matrix,
                    context.consumers(),
                    Font.DisplayMode.SEE_THROUGH,
                    background,
                    LightTexture.FULL_BRIGHT
            );
            line += 10;
        }
        if (renderDistance) {
            String distText = ((int) dist + "m");
            float width = -Minecraft.getInstance().font.width(distText) / 2f;
            Minecraft.getInstance().font.drawInBatch(
                    Component.literal(distText).withStyle(style -> style.withColor(ChatFormatting.YELLOW)),
                    width,
                    line,
                    0xFFFFFFFF,
                    false,
                    matrix,
                    context.consumers(),
                    Font.DisplayMode.SEE_THROUGH,
                    background,
                    LightTexture.FULL_BRIGHT
            );
        }
        context.matrices().popPose();
        *///?}
    }

    //? if = 1.8.9 {
    private static void drawNametag(String str, boolean render) {
        if (!render) return;

        FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
        float f = 1.6F;
        float f1 = 0.016666668F * f;
        GlStateManager.pushMatrix();
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-f1, -f1, f1);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 0;

        int j = fontrenderer.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(-j - 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(-j - 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(j + 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(j + 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
        GlStateManager.depthMask(true);

        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);

        GlStateManager.enableDepth();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
    //?}
}
