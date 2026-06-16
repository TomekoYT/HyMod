package tomeko.hymod.hud

import net.minecraft.client.Minecraft
//? if = 1.8.9 {
import cc.polyfrost.oneconfig.config.annotations.Exclude
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.core.OneColor
import cc.polyfrost.oneconfig.hud.BasicHud
import cc.polyfrost.oneconfig.libs.universal.UGraphics
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import cc.polyfrost.oneconfig.renderer.TextRenderer
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
//?} else {
/*import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.client.DeltaTracker
//? if >= 26.1 {
/*import net.minecraft.client.gui.GuiGraphicsExtractor
*///?} else {
import net.minecraft.client.gui.GuiGraphics
//?}
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import tomeko.hymod.config.HyModConfig
import tomeko.hymod.utils.Constants
*///?}
import tomeko.hymod.utils.HypixelPackets
import tomeko.hymod.utils.ItemTracker

//? if = 1.8.9 {
class
//?} else {
/*object
*///?}
BedwarsResourceDisplay
//? if = 1.8.9 {
: BasicHud(true)
//?}
{
    //? if >= 1.21.11 {
    /*fun register() {
        HudElementRegistry.attachElementBefore(
            VanillaHudElements.CHAT,
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "bedwars_resource_display"),
            BedwarsResourceDisplay::render
        )
    }
    *///?}

    //? if = 1.8.9 {
    companion object {
    //?}
    //? if = 1.8.9 {
    @Exclude
    //?}
    private val IRON: Item =
    //? if = 1.8.9 {
            Items.iron_ingot
        //?} else {
        /*Items.IRON_INGOT
    *///?}

    //? if = 1.8.9 {
    @Exclude
    //?}
    private val GOLD: Item =
    //? if = 1.8.9 {
            Items.gold_ingot
        //?} else {
        /*Items.GOLD_INGOT
    *///?}

    //? if = 1.8.9 {
    @Exclude
    //?}
    private val DIAMOND: Item =
    //? if = 1.8.9 {
            Items.diamond
        //?} else {
        /*Items.DIAMOND
    *///?}

    //? if = 1.8.9 {
    @Exclude
    //?}
    private val EMERALD: Item =
    //? if = 1.8.9 {
            Items.emerald
        //?} else {
        /*Items.EMERALD
    *///?}

    val items = mutableListOf(
        IRON,
        GOLD,
        DIAMOND,
        EMERALD
    )
    //? if = 1.8.9 {
    }
    //?}

    //? if = 1.8.9 {
    @Slider(
        name = "Item Padding",
        min = 0f,
        max = 10f
    )
    //?}
    var itemPadding =
    //? if = 1.8.9 {
    5f
        //?} else {
        /*HyModConfig.bedwarsResourceDisplayItemPadding
    *///?}

    //? if = 1.8.9 {
    @Slider(
        name = "Icon Padding",
        min = 0f,
        max = 10f
    )
    //?}
    var iconPadding =
    //? if = 1.8.9 {
    5f
        //?} else {
        /*HyModConfig.bedwarsResourceDisplayIconPadding
    *///?}

    //? if = 1.8.9 {
    @Switch(
        name = "Text Shadow"
    )
    //?}
    var textType =
    //? if = 1.8.9 {
    false
        //?} else {
        /*HyModConfig.bedwarsResourceDisplayTextType
    *///?}

    //? if = 1.8.9 {
    @Exclude
    private var actualWidth = 0f
    //?}

    //? if = 1.8.9 {
    @Exclude
    private var actualHeight = 0f
    //?}

    //? if = 1.8.9 {
    override fun draw(
        matrices: UMatrixStack,
        x: Float,
        y: Float,
        scale: Float,
        example: Boolean
    ) {
        if (!example && !HypixelPackets.inBedwars) return

        val mc = Minecraft.getMinecraft()

        val iconSize = 16f
        val offset = iconSize + itemPadding

        var longestWidth = 0
        for (item in items) {
            longestWidth =
                maxOf(longestWidth, mc.fontRendererObj.getStringWidth(getText(item)))
        }

        var size = 0

        UGraphics.GL.pushMatrix()
        UGraphics.GL.scale(scale, scale, 1f)
        UGraphics.GL.translate(x / scale, y / scale, 0f)

        for (item in items) {
            val stack = ItemStack(item)

            val itemY = (size * offset).toInt()
            val iconX = 0
            val textX = (iconSize + iconPadding).toInt()

            RenderHelper.enableGUIStandardItemLighting()
            mc.renderItem.zLevel = 200f

            mc.renderItem.renderItemAndEffectIntoGUI(stack, iconX, itemY)
            mc.renderItem.renderItemOverlayIntoGUI(
                mc.fontRendererObj,
                stack,
                0,
                0,
                ""
            )

            RenderHelper.disableStandardItemLighting()

            val type = if(textType) 1 else 0

            TextRenderer.drawScaledString(
                getText(item),
                textX.toFloat(),
                itemY + mc.fontRendererObj.FONT_HEIGHT / 2f,
                OneColor(255, 255, 255).rgb,
                TextRenderer.TextType.toType(type),
                1f
            )

            size++
        }

        UGraphics.GL.popMatrix()

        actualWidth = longestWidth + iconPadding + iconSize
        actualHeight = size * offset - itemPadding
    }

    override fun getWidth(scale: Float, example: Boolean): Float =
        actualWidth * scale

    override fun getHeight(scale: Float, example: Boolean): Float =
        actualHeight * scale

    override fun shouldShow(): Boolean =
        super.shouldShow() && HypixelPackets.inBedwars
    //?} else {
    /*fun render(
        //? if >= 26.1 {
        /*context: GuiGraphicsExtractor,
    *///?} else {
        context: GuiGraphics,
        //?}
        tickDelta: DeltaTracker
    ) {
        if (!HypixelPackets.inBedwars) return

        val mc = Minecraft.getInstance()

        val x = HyModConfig.bedwarsResourceDisplayWidthPercentage * mc.window.guiScaledWidth / 100
        val y = HyModConfig.bedwarsResourceDisplayHeightPercentage * mc.window.guiScaledHeight / 100
        val scale = HyModConfig.bedwarsResourceDisplayScalePercentage.toFloat() / 100f

        val iconSize = 16f
        val offset = iconSize + itemPadding

        context.pose().pushMatrix()
        context.pose().translate(x.toFloat(), y.toFloat())
        context.pose().scale(scale, scale)

        items.forEachIndexed { index, item ->
            val stack = ItemStack(item)

            val itemY = (index * offset).toInt()
            val iconX = 0
            val textX = (iconSize + iconPadding).toInt()

            //? if >= 26.1 {
            /*context.item(
            *///?} else {
            context.renderItem(
                //?}
                stack,
                iconX,
                itemY
            )

            val textY = itemY + (16 - mc.font.lineHeight) / 2

            val text = getText(item)
            val color = 0xFFFFFFFF.toInt()

            //? if >= 26.1 {
            /*context.text(
            *///?} else {
            context.drawString(
                //?}
                mc.font,
                text,
                textX,
                textY,
                color,
                textType
            )
        }

        context.pose().popMatrix()
    }
    *///?}

    private fun getText(item: Item): String {
        val inventoryAmount = ItemTracker.inventory[item]!!
        val enderChestAmount = ItemTracker.enderChest[item]!!

        return "$inventoryAmount + $enderChestAmount (${inventoryAmount + enderChestAmount})"
    }
}