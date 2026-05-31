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
/*import dev.isxander.yacl3.config.v2.api.SerialEntry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import tomeko.hymod.config.HyModConfig;
import tomeko.hymod.utils.Constants;
*///?}
import tomeko.hymod.utils.ItemTracker;
import tomeko.hymod.utils.HypixelPackets;

import java.util.ArrayList;
import java.util.List;

public class BedwarsResourceDisplay
        //? if = 1.8.9 {
        extends BasicHud
        //?}
{
    //? if = 1.8.9 {
    public BedwarsResourceDisplay() {
        super(true);
    }
    //?} else {
    /*public static void register() {
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "bedwars_resource_display"), HyModConfig.bedwarsResourceDisplay::render);
    }
    *///?}

    //? if >= 1.21.9 {
    /*@SerialEntry
    public int widthPercentage = 0;

    @SerialEntry
    public int heightPercentage = 0;

    @SerialEntry
    public int hudScale = 100;
    *///?}

    //? if = 1.8.9 {
    @Exclude
            //?}
    public final String itemPaddingName = "Item Padding";

    //? if = 1.8.9 {
    @Exclude
            //?}
    public final float itemPaddingMin = 0f;

    //? if = 1.8.9 {
    @Exclude
            //?}
    public final float itemPaddingMax = 10f;

    //? if = 1.8.9{
    @Slider(name = itemPaddingName, min = itemPaddingMin, max = itemPaddingMax)
            //?} else {
    /*@SerialEntry
     *///?}
    public float itemPadding = 5;

    //? if = 1.8.9 {
    @Exclude
            //?}
    public final String iconPaddingName = "Icon Padding";

    //? if = 1.8.9 {
    @Exclude
            //?}
    public final float iconPaddingMin = 0f;

    //? if = 1.8.9 {
    @Exclude
            //?}
    public final float iconPaddingMax = 10f;

    //? if = 1.8.9 {
    @Slider(name = iconPaddingName, min = iconPaddingMin, max = iconPaddingMax)
            //?} else {
    /*@SerialEntry
     *///?}
    public float iconPadding = 5;

    //? if = 1.8.9 {
    @Dropdown(name = "Text Type", options = {"No Shadow", "Shadow", "Full Shadow"})
    public int textType = 0;
    //?} else {
    /*@SerialEntry
    public boolean textType = false;
    *///?}


    //? if = 1.8.9 {
    @Exclude
    private static final Minecraft mc = Minecraft.getMinecraft();
    //?} else {
    /*private static final Minecraft mc = Minecraft.getInstance();
     *///?}

    //? if = 1.8.9 {
    @Exclude
    private static float actualWidth = 0.0f;
    @Exclude
    private static float actualHeight = 0.0f;
    //?}

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
    /*public void render(GuiGraphics context, DeltaTracker tickDelta) {
        if (!HypixelPackets.inBedwars) return;

        int x = widthPercentage * mc.getWindow().getGuiScaledWidth() / 100;
        int y = heightPercentage * mc.getWindow().getGuiScaledHeight() / 100;
        float scale = (float) hudScale / 100;

        float iconSize = 16f;
        float offset = iconSize + itemPadding;

        context.pose().pushMatrix();
        context.pose().translate(x, y);
        context.pose().scale(scale, scale);

        int index = 0;
        for (Item item : items) {
            ItemStack stack = new ItemStack(item);

            int itemY = (int) (index * offset);
            int iconX = 0;
            int textX = (int) (iconSize + iconPadding);

            context.renderItem(stack, iconX, itemY);

            int textY = itemY + (16 - mc.font.lineHeight) / 2;

            String text = getText(item);
            int color = 0xFFFFFFFF;

            context.drawString(mc.font, text, textX, textY, color, textType);

            index++;
        }
        context.pose().popMatrix();
    }
    *///?}

    private String getText(Item item) {
        int inventoryAmount = ItemTracker.inventory.get(item);
        int enderChestAmount = ItemTracker.enderChest.get(item);
        return inventoryAmount + " + " + enderChestAmount + " (" + (inventoryAmount + enderChestAmount) + ")";
    }
}
