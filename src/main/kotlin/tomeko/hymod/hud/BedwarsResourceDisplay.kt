package tomeko.hymod.hud

import net.minecraft.client.Minecraft
//? if = 1.8.9 {
/*import cc.polyfrost.oneconfig.config.annotations.*
import cc.polyfrost.oneconfig.config.core.OneColor
import cc.polyfrost.oneconfig.hud.BasicHud
import cc.polyfrost.oneconfig.libs.universal.UGraphics
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import cc.polyfrost.oneconfig.renderer.TextRenderer
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
*///?} else {
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.polyfrost.oneconfig.api.config.v1.annotations.*
import org.polyfrost.oneconfig.api.hud.v1.HudManager
import org.polyfrost.oneconfig.api.hud.v1.LegacyHud
import tomeko.hymod.utils.Constants
//?}
import tomeko.hymod.config.HyModConfig
import tomeko.hymod.utils.HypixelPackets
import tomeko.hymod.utils.ItemTracker

class BedwarsResourceDisplay
//? if = 1.8.9 {
/*: BasicHud(true)
*///?} else {
    : LegacyHud("bedwars-resource-display", "Bedwars Resource Display", Category.COMBAT)
//?}
{
    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    companion object {
        //? if >= 26.1 {
        fun register() {
            HudManager.register(BedwarsResourceDisplay(), Constants.MOD_ID, Constants.MOD_ICON)
        }
        //?}

        //? if = 1.8.9 {
        /*@Exclude
        *///?}
        private val IRON: Item =
        //? if = 1.8.9 {
                /*Items.iron_ingot
            *///?} else {
            Items.IRON_INGOT
        //?}

        //? if = 1.8.9 {
        /*@Exclude
        *///?}
        private val GOLD: Item =
        //? if = 1.8.9 {
                /*Items.gold_ingot
            *///?} else {
            Items.GOLD_INGOT
        //?}

        //? if = 1.8.9 {
        /*@Exclude
        *///?}
        private val DIAMOND: Item =
        //? if = 1.8.9 {
                /*Items.diamond
            *///?} else {
            Items.DIAMOND
        //?}

        //? if = 1.8.9 {
        /*@Exclude
        *///?}
        private val EMERALD: Item =
        //? if = 1.8.9 {
                /*Items.emerald
            *///?} else {
            Items.EMERALD
        //?}

        //? if = 1.8.9 {
        /*@Exclude
        *///?}
        val items = mutableListOf(
            IRON,
            GOLD,
            DIAMOND,
            EMERALD
        )
    }

    @Slider(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Item Padding",
        min = 0f,
        max = 10f
        //? if >= 26.1 {
        , step = 0.1f
        //?}
    )
    var itemPadding = 5f

    @Slider(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Icon Padding",
        min = 0f,
        max = 10f
        //? if >= 26.1 {
        , step = 0.1f
        //?}
    )
    var iconPadding = 5f

    //? if = 1.8.9 {
    /*@Dropdown(
        name = "Text Type",
        options = ["No Shadow", "Shadow", "Full Shadow"]
    )
    var textType = 0

    @Color(name = "Text Color")
    var textColor: OneColor = OneColor(255, 255, 255)
    *///?}

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    private var actualWidth = 0f

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    private var actualHeight = 0f

    //? if = 1.8.9 {
    /*override fun draw(
        matrices: UMatrixStack,
        x: Float,
        y: Float,
        scale: Float,
        example: Boolean
    )
    *///?} else {
    override fun render(mcCtx: GuiGraphicsExtractor)
    //?}
    {
        //? if >= 26.1 {
        if (!HyModConfig.bedwarsResourceDisplayEnabled) return
        //?}

        if (
        //? if = 1.8.9 {
        /*!example &&
        *///?}
            !HypixelPackets.inBedwars
            && !HypixelPackets.onRBW
        ) return

        val mc =
        //? if = 1.8.9 {
                /*Minecraft.getMinecraft()
            *///?} else {
            Minecraft.getInstance()
        //?}

        val iconSize = 16f
        val offset = iconSize + itemPadding

        var longestWidth = 0
        for (item in items) {
            if (!showItem(item)) continue

            longestWidth = maxOf(
                longestWidth,
                //? if = 1.8.9 {
                /*mc.fontRendererObj.getStringWidth(
                    *///?} else {
                mc.font.width(
                    //?}
                    getText(item)
                )
            )
        }

        var size = 0
        for (item in items) {
            if (showItem(item)) size++
        }

        //? if = 1.8.9 {
        /*UGraphics.GL.pushMatrix()
        UGraphics.GL.scale(scale, scale, 1f)
        UGraphics.GL.translate(x / scale, y / scale, 0f)
        *///?} else {
        mcCtx.pose().pushMatrix()
        //?}

        //? if >= 26.1 {
        if (showBackground) {
            mcCtx.fill(
                -bgRadius.toInt(),
                -bgRadius.toInt(),
                (longestWidth + iconPadding + iconSize).toInt() + bgRadius.toInt(),
                (size * offset - itemPadding).toInt() + bgRadius.toInt(),
                bgColor
            )
        }
        //?}

        var i = 0
        for (item in items) {
            if (!showItem(item)) continue

            val stack = ItemStack(item)
            val itemY = (i * offset).toInt()
            val iconX = 0
            val textX = (iconSize + iconPadding).toInt()

            //? if = 1.8.9 {
            /*RenderHelper.enableGUIStandardItemLighting()
            mc.renderItem.zLevel = 200f
            *///?}

            //? if = 1.8.9 {
            /*mc.renderItem.renderItemAndEffectIntoGUI(stack, iconX, itemY)
            mc.renderItem.renderItemOverlayIntoGUI(
                mc.fontRendererObj,
                stack,
                0,
                0,
                ""
            )
            *///?} else {
            mcCtx.item(
                stack,
                iconX,
                itemY
            )
            //?}

            //? if = 1.8.9 {
            /*RenderHelper.disableStandardItemLighting()
            *///?}

            //? if = 1.8.9 {
            /*TextRenderer.drawScaledString(
                getText(item),
                textX.toFloat(),
                itemY + mc.fontRendererObj.FONT_HEIGHT / 2f,
                textColor.rgb,
                TextRenderer.TextType.toType(textType),
                1f
            )
            *///?} else {
            val textY = itemY + (16 - mc.font.lineHeight) / 2

            if (showShadow) {
                mcCtx.text(
                    mc.font,
                    getText(item),
                    textX + 1,
                    textY + 1,
                    shadowColor,
                    false
                )
            }

            mcCtx.text(
                mc.font,
                getText(item),
                textX,
                textY,
                textColor,
                false
            )
            //?}

            i++
        }

        //? if = 1.8.9 {
        /*UGraphics.GL.popMatrix()
        *///?} else {
        mcCtx.pose().popMatrix()
        //?}

        actualWidth = longestWidth + iconPadding + iconSize
        actualHeight = size * offset - itemPadding
    }

    //? if = 1.8.9 {
    /*override fun getWidth(scale: Float, example: Boolean): Float =
        actualWidth * scale

    override fun getHeight(scale: Float, example: Boolean): Float =
        actualHeight * scale

    override fun shouldShow(): Boolean =
        super.shouldShow() && (HypixelPackets.inBedwars || HypixelPackets.onRBW)
    *///?} else {
    override val width: Float = actualWidth
    override val height: Float = actualHeight
    override fun minimumSize(): Pair<Float, Float> = actualWidth to actualHeight
    override fun update(): Boolean = true
    override fun multipleInstancesAllowed(): Boolean = false
    override fun deletable(): Boolean = false
    //?}

    private fun getText(item: Item): String {
        val inventoryAmount = ItemTracker.inventory[item]!!
        val enderChestAmount = ItemTracker.enderChest[item]!!

        var text = ""
        if (HyModConfig.bedwarsResourceDisplayShowInventory) text += inventoryAmount.toString()

        if (HyModConfig.bedwarsResourceDisplayShowEnderChest) {
            if (HyModConfig.bedwarsResourceDisplayShowInventory) text += " + "
            text += enderChestAmount.toString()
        }

        if (HyModConfig.bedwarsResourceDisplayShowTotal) {
            if (HyModConfig.bedwarsResourceDisplayShowInventory || HyModConfig.bedwarsResourceDisplayShowEnderChest) text += " "
            text += "(${inventoryAmount + enderChestAmount})"
        }

        return text
    }

    private fun showItem(item: Item): Boolean = when (item) {
        IRON -> HyModConfig.bedwarsResourceDisplayShowIron
        GOLD -> HyModConfig.bedwarsResourceDisplayShowGold
        DIAMOND -> HyModConfig.bedwarsResourceDisplayShowDiamond
        EMERALD -> HyModConfig.bedwarsResourceDisplayShowEmerald
        else -> false
    }
}