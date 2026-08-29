package tomeko.hymod.bedwars

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
//? if >= 26.1 {
import net.minecraft.client.gui.GuiGraphicsExtractor
//?} else {
/*import net.minecraft.client.gui.GuiGraphics as GuiGraphicsExtractor
*///?}
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.polyfrost.compose.render.PolyColor
import org.polyfrost.oneconfig.api.config.v1.annotations.*
import org.polyfrost.oneconfig.api.hud.v1.HudManager
import org.polyfrost.oneconfig.api.hud.v1.LegacyHud
//?}
import tomeko.hymod.config.HyModConfig
import tomeko.hymod.utils.Constants
import tomeko.hymod.utils.HypixelPackets
import tomeko.hymod.utils.ItemTracker

class BedwarsResourceDisplay
//? if = 1.8.9 {
/*: BasicHud(true)
*///?} else {
    : LegacyHud("${Constants.MOD_ID}_bedwars_resource_display.json", "Bedwars Resource Display", Category.COMBAT)
//?}
{
    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    companion object {
        //? if >= 1.21.11 {
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

        //? if >= 1.21.11 {
        private const val CATEGORY_GENERAL = "General"
        private const val SUBCATEGORY_GENERAL = "General"
        private const val SUBCATEGORY_RESOURCES = "Resources"
        private const val CATEGORY_BACKGROUND = "Background"
        //?}
    }

    //? if = 1.8.9 {
    /*@Dropdown(
        name = "Text Type",
        options = ["No Shadow", "Shadow", "Full Shadow"]
    )
    var textType = 0

    @Color(name = "Text Color")
    var textColor: OneColor = OneColor(255, 255, 255)
    *///?} else {
    @Color(
        title = "Text Color",
        category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_GENERAL
    )
    var txtColor = PolyColor(0xFFFFFFFF.toInt())

    @Switch(
        title = "Text Shadow",
        category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_GENERAL
    )
    var textShadow = true

    @Color(
        title = "Text Shadow Color",
        category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_GENERAL
    )
    var textShadowColor = PolyColor(0xFF000000.toInt())
    //?}

    @Slider(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Item Padding",
        min = 0f,
        max = 10f
        //? if >= 1.21.11 {
        , step = 0.1f,
        category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_GENERAL
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
        //? if >= 1.21.11 {
        , step = 0.1f,
        category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_GENERAL
        //?}
    )
    var iconPadding = 5f

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Show Iron"
        //? if >= 1.21.11 {
        , category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_RESOURCES
        //?}
    )
    var showIron = true

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Show Gold"
        //? if >= 1.21.11 {
        , category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_RESOURCES
        //?}
    )
    var showGold = true

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Show Diamond"
        //? if >= 1.21.11 {
        , category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_RESOURCES
        //?}
    )
    var showDiamond = true

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Show Emerald"
        //? if >= 1.21.11 {
        , category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_RESOURCES
        //?}
    )
    var showEmerald = true

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Show Inventory"
        //? if >= 1.21.11 {
        , category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_RESOURCES
        //?}
    )
    var showInventory = true

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Show Ender Chest"
        //? if >= 1.21.11 {
        , category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_RESOURCES
        //?}
    )
    var showEnderChest = true

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Show Total"
        //? if >= 1.21.11 {
        , category = CATEGORY_GENERAL,
        subcategory = SUBCATEGORY_RESOURCES
        //?}
    )
    var showTotal = true

    //? if >= 1.21.11 {
    @Switch(
        title = "Show Background",
        category = CATEGORY_BACKGROUND
    )
    var background = false

    @Color(
        title = "Background Color",
        category = CATEGORY_BACKGROUND
    )
    var backgroundColor = PolyColor(0x80000000.toInt())

    @Slider(
        title = "Background Radius",
        category = CATEGORY_BACKGROUND,
        min = 0f,
        max = 10f,
        step = 0.1f
    )
    var backgroundRadius = 5f
    //?}

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    private var actualWidth = 1f

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    private var actualHeight = 1f

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
        if (
        //? if = 1.8.9 {
        /*!example
        *///?} else {
            !HudManager.isEditing
            //?}
            && !HyModConfig.debugModeEnabled
            && !HypixelPackets.inBedwars
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

        //? if >= 1.21.11 {
        if (background) {
            mcCtx.fill(
                -backgroundRadius.toInt(),
                -backgroundRadius.toInt(),
                (longestWidth + iconPadding + iconSize).toInt() + backgroundRadius.toInt(),
                (size * offset - itemPadding).toInt() + backgroundRadius.toInt(),
                backgroundColor.argb
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
            //? if >= 26.1 {
            mcCtx.item(
            //?} else {
            /*mcCtx.renderItem(
                *///?}
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

            if (textShadow) {
                //? if >= 26.1 {
                mcCtx.text(
                //?} else {
                /*mcCtx.drawString(
                    *///?}
                    mc.font,
                    getText(item),
                    textX + 1,
                    textY + 1,
                    textShadowColor.argb,
                    false
                )
            }

            //? if >= 26.1 {
            mcCtx.text(
            //?} else {
            /*mcCtx.drawString(
                *///?}
                mc.font,
                getText(item),
                textX,
                textY,
                txtColor.argb,
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
        super.shouldShow() && (HyModConfig.debugModeEnabled || HypixelPackets.inBedwars || HypixelPackets.onRBW)
    *///?} else {
    override val width: Float = actualWidth
    override val height: Float = actualHeight
    override fun minimumSize(): Pair<Float, Float> = actualWidth to actualHeight
    override fun defaultPosition(): Pair<Float, Float> = 0f to 0f
    override fun update(): Boolean = true
    override fun multipleInstancesAllowed(): Boolean = true
    override fun deletable(): Boolean = true
    //?}

    private fun getText(item: Item): String {
        val inventoryAmount = ItemTracker.inventory[item] ?: 0
        val enderChestAmount = ItemTracker.enderChest[item] ?: 0

        var text = ""
        if (showInventory) text += inventoryAmount.toString()

        if (showEnderChest) {
            if (showInventory) text += " + "
            text += enderChestAmount.toString()
        }

        if (showTotal) {
            if (showInventory || showEnderChest) text += " "
            text += "(${inventoryAmount + enderChestAmount})"
        }

        return text
    }

    private fun showItem(item: Item): Boolean = when (item) {
        IRON -> showIron
        GOLD -> showGold
        DIAMOND -> showDiamond
        EMERALD -> showEmerald
        else -> false
    }
}