package tomeko.hymod.hud

import net.minecraft.client.Minecraft
//? if = 1.8.9 {
import cc.polyfrost.oneconfig.config.annotations.Dropdown
import cc.polyfrost.oneconfig.config.annotations.Exclude
import cc.polyfrost.oneconfig.config.annotations.Slider
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
/*import androidx.compose.runtime.Composable
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import org.polyfrost.oneconfig.api.config.v1.annotations.*
import org.polyfrost.oneconfig.api.hud.v1.Hud
*///?}

import tomeko.hymod.utils.ItemTracker
import tomeko.hymod.utils.HypixelPackets

object BedwarsResourceDisplay
//? if = 1.8.9 {
    : BasicHud(true)
//?} else {
/*: Hud(
    "bedwarsresourcedisplay",
    "Bedwars Resource Display",
    Category.COMBAT
)
*///?}
{
    @Slider(
        //? if = 1.8.9 {
        name
            //?} else {
            /*title
             *///?}
        = "Item Padding",
        min = 0f,
        max = 10f
    )
    var itemPadding = 5f

    @Slider(
        //? if = 1.8.9 {
        name
            //?} else {
            /*title
             *///?}
        = "Icon Padding",
        min = 0f,
        max = 10f
    )
    var iconPadding = 5f

    @Dropdown(
        //? if = 1.8.9 {
        name
            //?} else {
            /*title
             *///?}
        = "Text Type",
        options = ["No Shadow", "Shadow"]
    )
    var textType = 0

    //? if = 1.8.9 {
    @Exclude
    //?}
    private val mc =
        //? if = 1.8.9 {
        Minecraft.getMinecraft()
    //?} else {
    /*Minecraft.getInstance()
    *///?}

    //? if = 1.8.9 {
    @Exclude
    //?}
    private var actualWidth = 0f

    //? if = 1.8.9 {
    @Exclude
    //?}
    private var actualHeight = 0f

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

    //? if = 1.8.9 {
    @Exclude
    //?}
    val items = mutableListOf(
        IRON,
        GOLD,
        DIAMOND,
        EMERALD
    )

    //? if = 1.8.9 {
    override fun draw(
        matrices: UMatrixStack,
        x: Float,
        y: Float,
        scale: Float,
        example: Boolean
    ) {
        if (!example && !HypixelPackets.inBedwars) return

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

            TextRenderer.drawScaledString(
                getText(item),
                textX.toFloat(),
                itemY + mc.fontRendererObj.FONT_HEIGHT / 2f,
                OneColor(255, 255, 255).rgb,
                TextRenderer.TextType.toType(textType),
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
    /*override fun update(): Boolean {
        return false
    }

    @Composable
    override fun Content() {

    }
    *///?}

    private fun getText(item: Item): String {
        val inventoryAmount = ItemTracker.inventory[item]!!
        val enderChestAmount = ItemTracker.enderChest[item]!!

        return "$inventoryAmount + $enderChestAmount (${inventoryAmount + enderChestAmount})"
    }
}