package tomeko.hymod.hud;

import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Exclude;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.hud.BasicHud;
import cc.polyfrost.oneconfig.libs.universal.UGraphics;
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack;
import cc.polyfrost.oneconfig.renderer.TextRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tomeko.hymod.gui.ItemTracker;

import java.util.ArrayList;
import java.util.List;

public class BedwarsResourceDisplay extends BasicHud {
    public BedwarsResourceDisplay() {
        super(true);
    }

    @Slider(name = "Item Padding", min = 0f, max = 10f)
    public int itemPadding = 5;

    @Slider(name = "Icon Padding",  min = 0f, max = 10f)
    public int iconPadding = 5;

    @Dropdown(name = "Text Type", options = {"No Shadow", "Shadow", "Full Shadow"})
    public int textType = 0;

    @Exclude
    private static final Minecraft mc = Minecraft.getMinecraft();

    @Exclude
    private static float actualWidth = 0.0f;
    @Exclude
    private static float actualHeight = 0.0f;

    @Exclude
    public static List<Item> items = new ArrayList<>();

    static {
        items.add(Items.iron_ingot);
        items.add(Items.gold_ingot);
        items.add(Items.diamond);
        items.add(Items.emerald);
    }

    @Override
    public void draw(UMatrixStack matrices, float x, float y, float scale, boolean example) {
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

    private String getText(Item item) {
        int inventoryAmount = ItemTracker.inventory.get(item);
        int enderChestAmount = ItemTracker.enderChest.get(item);
        return inventoryAmount + " + " + enderChestAmount + " (" + (inventoryAmount + enderChestAmount) + ")";
    }
}
