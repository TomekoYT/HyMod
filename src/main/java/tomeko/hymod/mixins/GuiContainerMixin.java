package tomeko.hymod.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import tomeko.hymod.config.HyModConfig;
import tomeko.hymod.utils.HypixelPackets;

import java.util.List;

@Mixin(GuiContainer.class)
public class GuiContainerMixin {
    private static final Minecraft mc = Minecraft.getMinecraft();

    @WrapOperation(
            method = "mouseClicked",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/inventory/GuiContainer;handleMouseClick(Lnet/minecraft/inventory/Slot;III)V"
            )
    )
    private void mouseClicked(GuiContainer instance, Slot slotIn, int slotId, int clickedButton, int clickType, Operation<Void> original) {
        if (shouldCallOriginal(instance, slotIn, clickedButton, clickType)) {
            original.call(instance, slotIn, slotId, clickedButton, clickType);
            return;
        }

        original.call(instance, slotIn, slotId, 2, 3);
    }

    private static boolean shouldCallOriginal(GuiContainer instance, Slot slotIn, int clickedButton, int clickType) {
        if (
                clickedButton != 0
                        || clickType != 0
                        || !HyModConfig.middleClickGUIItemsEnabled
                        || !(instance instanceof GuiChest)
                        || !HypixelPackets.onHypixel
                        || HypixelPackets.inSkyblock
                        || slotIn == null
                        || !slotIn.getHasStack()
                        || !(instance.inventorySlots instanceof ContainerChest)
                        || ((ContainerChest) instance.inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText().contains("Chest")
        ) return true;

        List<String> tooltip = slotIn.getStack().getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
        for (String line : tooltip) {
            if (moreThanOneButton(line)) return true;
        }

        return false;
    }

    private static boolean moreThanOneButton(String text) {
        text = text.toLowerCase();

        return text.contains("right-click")
                || text.contains("right click")
                || text.contains("left-click")
                || text.contains("left click");
    }
}
