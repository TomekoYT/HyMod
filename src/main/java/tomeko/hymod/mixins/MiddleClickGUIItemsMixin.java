package tomeko.hymod.mixins;

import net.minecraft.client.Minecraft;
//? if = 1.8.9 {
/*import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Redirect;
*///?} else {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
//?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import tomeko.hymod.config.HyModConfig;
import tomeko.hymod.utils.HypixelPackets;

import java.util.List;

//? if = 1.8.9 {
/*@Mixin(GuiContainer.class)
 *///?} else {
@Mixin(AbstractContainerScreen.class)
//?}
public abstract class MiddleClickGUIItemsMixin {
    //? if = 1.8.9 {
    /*@Shadow
    protected abstract void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType);
    *///?}

    //? if = 1.8.9 {
    /*@Redirect(
            method = "mouseClicked",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/inventory/GuiContainer;handleMouseClick(Lnet/minecraft/inventory/Slot;III)V"
            )
    )
            *///?} else {
    @WrapOperation(
            method = "mouseClicked",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V"
            )
    )
    //?}
    private void useMiddleClick(
            //? if = 1.8.9 {
            /*GuiContainer instance,
             *///?} else {
            AbstractContainerScreen instance,
            //?}
            Slot slotIn,
            int slotId,
            int clickedButton,
            //? if = 1.8.9 {
            /*int clickType
             *///?} else {
            ContainerInput clickType,
            //?}
            //? if >= 26.1 {
            Operation<Void> original
            //?}
    ) {
        if (shouldCallOriginal(instance, slotIn, clickedButton, clickType)) {
            //? if = 1.8.9 {
            /*handleMouseClick(slotIn, slotId, clickedButton, clickType);
             *///?} else {
            original.call(instance, slotIn, slotId, clickedButton, clickType);
            //?}
            return;
        }

        //? if = 1.8.9 {
        /*handleMouseClick(slotIn, slotId, 2, 3);
         *///?} else {
        original.call(
                instance,
                slotIn,
                slotId,
                2,
                ContainerInput.CLONE
        );
        //?}
    }

    private static boolean shouldCallOriginal(
            //? if = 1.8.9 {
            /*GuiContainer instance,
             *///?} else {
            AbstractContainerScreen instance,
            //?}
            Slot slotIn,
            int clickedButton,
            //? if = 1.8.9 {
            /*int clickType
             *///?} else {
            ContainerInput clickType
            //?}
    ) {
        if (
                clickedButton != 0
                        //? if = 1.8.9 {
                        /*|| clickType != 0
                         *///?} else {
                        || clickType != ContainerInput.PICKUP
                        //?}
                        || !HyModConfig.middleClickGUIItemsEnabled
                        //? if = 1.8.9 {
                        /*|| !(instance instanceof GuiChest)
                         *///?} else {
                        || !(instance.getMenu() instanceof ChestMenu)
                        //?}
                        || !HypixelPackets.onHypixel
                        || HypixelPackets.inSkyblock
                        || slotIn == null
                        //? if = 1.8.9 {
                        /*|| !slotIn.getHasStack()
                        || !(instance.inventorySlots instanceof ContainerChest)
                        *///?}
                        //? if = 1.8.9 {
                        /*|| ((ContainerChest) instance.inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText()
                         *///?} else {
                        || instance.getTitle().getString()
                        //?}
                        .contains("Chest")

        ) return true;

        //? if = 1.8.9 {
        /*List<String> tooltip = slotIn.getStack().getTooltip(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
         *///?} else {
        List<Component> tooltip = slotIn.getItem().getTooltipLines(Item.TooltipContext.EMPTY, Minecraft.getInstance().player, TooltipFlag.NORMAL);
        //?}
        for (
            //? if = 1.8.9 {
            /*String line
             *///?} else {
                Component line
            //?}
                : tooltip
        ) {
            if (moreThanOneButton(
                    line
                            //? if >= 26.1 {
                            .getString()
                    //?}
            )) return true;
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
