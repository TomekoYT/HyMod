package tomeko.hymod.hud;

import cc.polyfrost.oneconfig.config.annotations.Exclude;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.hud.BasicHud;
import cc.polyfrost.oneconfig.libs.universal.UGraphics;
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack;
import cc.polyfrost.oneconfig.renderer.TextRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import tomeko.hymod.gui.ItemTracker;

import java.util.ArrayList;
import java.util.List;

public class BedwarsResourceDisplay extends BasicHud {
    public BedwarsResourceDisplay() {
        super(true);
    }

    @Exclude
    private static final Minecraft mc = Minecraft.getMinecraft();

    @Exclude
    private static float actualWidth = 0.0f;
    @Exclude
    private static float actualHeight = 0.0f;

    @Exclude
    private static final int padding = 5;
    @Exclude
    private static final int iconPadding = 5;

    @Exclude
    private static final int textType = 2;

    @Exclude
    public static List<ItemStack> items = new ArrayList<>();

    static {
        items.add(new ItemStack(Items.iron_ingot));
        items.add(new ItemStack(Items.gold_ingot));
        items.add(new ItemStack(Items.diamond));
        items.add(new ItemStack(Items.emerald));
    }

    @Override
    public void draw(UMatrixStack matrices, float x, float y, float scale, boolean example) {
        float iconSize = 16f;
        float offset = iconSize + padding;

        int longestWidth = 0;
        for (Integer amount : ItemTracker.inventory.values()) {
            longestWidth = Math.max(longestWidth, mc.fontRendererObj.getStringWidth(amount.toString()));
        }

        int lastWidth = 0;
        int size = 0;

        UGraphics.GL.pushMatrix();
        UGraphics.GL.scale(scale, scale, 1f);
        UGraphics.GL.translate(x / scale, y / scale, 0f);

        for (ItemStack item : items) {
            int amount = ItemTracker.inventory.get(item);

            String text = String.valueOf(amount);
            int textWidth = mc.fontRendererObj.getStringWidth(text);

            int itemY = (int) (size * offset);
            int iconX = lastWidth;

            int textX = (int) (iconSize + iconPadding);

            RenderHelper.enableGUIStandardItemLighting();
            mc.getRenderItem().zLevel = 200f;

            mc.getRenderItem().renderItemAndEffectIntoGUI(item, iconX, itemY);

            mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, item, 0, 0, "");

            RenderHelper.disableStandardItemLighting();

            TextRenderer.drawScaledString(
                    text,
                    (float) textX, itemY + mc.fontRendererObj.FONT_HEIGHT / 2f,
                    new OneColor(255, 255, 255).getRGB(),
                    TextRenderer.TextType.toType(textType),
                    1f
            );

            size++;
        }

        UGraphics.GL.popMatrix();

        actualWidth = longestWidth + iconPadding + iconSize;
        actualHeight = size * offset - padding;
    }

    @Override
    public float getWidth(float scale, boolean example) {
        return actualWidth * scale;
    }

    @Override
    public float getHeight(float scale, boolean example) {
        return actualHeight * scale;
    }
}
