package tomeko.hymod.hud;

import net.minecraft.client.Minecraft;
//? if = 1.8.9 {
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Exclude;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.hud.BasicHud;
import cc.polyfrost.oneconfig.libs.universal.UGraphics;
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack;
import cc.polyfrost.oneconfig.renderer.TextRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
//?} else {
/*import androidx.compose.runtime.Composer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;
import org.polyfrost.oneconfig.api.config.v1.annotations.*;
import org.polyfrost.oneconfig.api.hud.v1.Hud;
*///?}
import tomeko.hymod.utils.ItemTracker;
import tomeko.hymod.utils.HypixelPackets;

import java.util.ArrayList;
import java.util.List;

public class BedwarsResourceDisplay
        //? if = 1.8.9 {
        extends BasicHud
         //?} else {
        /*extends Hud
        *///?}
{
    public BedwarsResourceDisplay() {
        super(
                //? if = 1.8.9 {
                true
                 //?} else {
                /*"bedwarsresourcedisplay",
                "Bedwars Resource Display",
                Category.Companion.getCOMBAT()
                *///?}
        );
    }


    @Slider(//? if = 1.8.9 {
            name
            //?} else {
            /*title
                    *///?}
                    = "Item Padding", min = 0f, max = 10f)
    public float itemPadding = 5;

    @Slider(//? if = 1.8.9 {
            name
            //?} else {
            /*title
                    *///?}
                    = "Icon Padding", min = 0f, max = 10f)
    public float iconPadding = 5;

    @Dropdown(//? if = 1.8.9 {
            name
            //?} else {
            /*title
                    *///?}
                    = "Text Type", options = {"No Shadow", "Shadow"})
    public int textType = 0;


    //? if = 1.8.9 {
    @Exclude
    private static final Minecraft mc = Minecraft.getMinecraft();
    //?} else {
    /*private static final Minecraft mc = Minecraft.getInstance();
    *///?}

    //? if = 1.8.9 {
    @Exclude
     //?}
    private static float actualWidth = 0.0f;
    //? if = 1.8.9 {
    @Exclude
     //?}
    private static float actualHeight = 0.0f;

    //? if = 1.8.9 {
    @Exclude
     //?}
    private static final Item IRON =
            //? if = 1.8.9 {
            Items.iron_ingot;
             //?} else {
            /*Items.IRON_INGOT;
    *///?}

    //? if = 1.8.9 {
    @Exclude
     //?}
    private static final Item GOLD =
            //? if = 1.8.9 {
            Items.gold_ingot;
             //?} else {
            /*Items.GOLD_INGOT;
    *///?}

    //? if = 1.8.9 {
    @Exclude
     //?}
    private static final Item DIAMOND =
            //? if = 1.8.9 {
            Items.diamond;
             //?} else {
            /*Items.DIAMOND;
    *///?}

    //? if = 1.8.9 {
    @Exclude
     //?}
    private static final Item EMERALD =
            //? if = 1.8.9 {
            Items.emerald;
             //?} else {
            /*Items.EMERALD;
    *///?}

    //? if = 1.8.9 {
    @Exclude
     //?}
    public static List<Item> items = new ArrayList<>();

    static {
        items.add(IRON);
        items.add(GOLD);
        items.add(DIAMOND);
        items.add(EMERALD);
    }

    //? if = 1.8.9 {
    @Override
    public void draw(UMatrixStack matrices, float x, float y, float scale, boolean example) {
        if (!example && !HypixelPackets.inBedwars) return;

        float iconSize = 16f;
        float offset = iconSize + itemPadding;

        int longestWidth = 0;
        for (Item item : items) {
            longestWidth = Math.max(longestWidth, mc.fontRendererObj.getStringWidth(getText(item)));
        }

        int size = 0;

        UGraphics.GL.pushMatrix();
        UGraphics.GL.scale(scale, scale, 1f);
        UGraphics.GL.translate(x / scale, y / scale, 0f);

        for (Item item : items) {
            ItemStack stack = new ItemStack(item);

            int itemY = (int) (size * offset);
            int iconX = 0;

            int textX = (int) (iconSize + iconPadding);

            RenderHelper.enableGUIStandardItemLighting();
            mc.getRenderItem().zLevel = 200f;

            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, iconX, itemY);

            mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, 0, 0, "");

            RenderHelper.disableStandardItemLighting();

            TextRenderer.drawScaledString(
                    getText(item),
                    (float) textX, itemY + mc.fontRendererObj.FONT_HEIGHT / 2f,
                    new OneColor(255, 255, 255).getRGB(),
                    TextRenderer.TextType.toType(textType),
                    1f
            );

            size++;
        }

        UGraphics.GL.popMatrix();

        actualWidth = longestWidth + iconPadding + iconSize;
        actualHeight = size * offset - itemPadding;
    }

    @Override
    public float getWidth(float scale, boolean example) {
        return actualWidth * scale;
    }

    @Override
    public float getHeight(float scale, boolean example) {
        return actualHeight * scale;
    }

    @Override
    public boolean shouldShow() {
        return super.shouldShow() && HypixelPackets.inBedwars;
    }
    //?} else {
    /*@Override
    public boolean update() {
        return false;
    }
    *///?}

    @Override
    public void Content(@Nullable Composer composer, int i) {

    }

    private String getText(Item item) {
        int inventoryAmount = ItemTracker.inventory.get(item);
        int enderChestAmount = ItemTracker.enderChest.get(item);
        return inventoryAmount + " + " + enderChestAmount + " (" + (inventoryAmount + enderChestAmount) + ")";
    }
}
