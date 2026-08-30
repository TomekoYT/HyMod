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
//? if >= 26.1 {
import net.minecraft.world.inventory.ContainerInput;
//?} else {
/*import net.minecraft.world.inventory.ClickType;
 *///?}
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
//?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import tomeko.hymod.config.HyModConfig;
import tomeko.hymod.location.HypixelPackets;

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
                    target =
                            //? if >= 26.1 {
                            "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V"
                    //?} else {
                    /*"Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ClickType;)V"
            *///?}
            )
    )
            //?}
    private void hymod$useMiddleClick(
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
            //? if >= 26.1 {
            ContainerInput
                    //?} else {
                    /*ClickType
                     *///?}
                    clickType,
            //?}
            //? if >= 1.21.11 {
            Operation<Void> original
            //?}
    ) {
        if (hymod$shouldCallOriginal(instance, slotIn, clickedButton, clickType)) {
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
                //? if >= 26.1 {
                ContainerInput.CLONE
                //?} else {
                /*ClickType.CLONE
                 *///?}
        );
        //?}
    }


    private static boolean hymod$shouldCallOriginal(
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
            //? if >= 26.1 {
            ContainerInput
                    //?} else {
                    /*ClickType
                     *///?}
                    clickType
            //?}
    ) {
        if (
                clickedButton != 0
                        //? if = 1.8.9 {
                        /*|| clickType != 0
                         *///?} else {
                        || clickType !=
                        //? if >= 26.1 {
                        ContainerInput.PICKUP
                        //?} else {
                        /*ClickType.PICKUP
                         *///?}
                        //?}
                        //? if = 1.8.9 {
                        /*|| !(instance instanceof GuiChest)
                         *///?} else {
                        || !(instance.getMenu() instanceof ChestMenu)
                        //?}
                        || !HypixelPackets.INSTANCE.getOnHypixel()
                        || slotIn == null
            //? if = 1.8.9 {
                        /*|| !slotIn.getHasStack()
                        || !(instance.inventorySlots instanceof ContainerChest)
                        *///?}
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
            if (hymod$moreThanOneButton(
                    line
                            //? if >= 1.21.11 {
                            .getString()
                    //?}
            )) return true;
        }

        if (HypixelPackets.INSTANCE.getInDuels() && HypixelPackets.INSTANCE.getInLobby())
            return true;

        if (HyModConfig.INSTANCE.getMiddleClickInLobby() && HypixelPackets.INSTANCE.getInLobby())
            return false;

        String containerTitle =
                //? if = 1.8.9 {
                /*((ContainerChest) instance.inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText();
                 *///?} else {
                instance.getTitle().getString();
        //?}


        if (HyModConfig.INSTANCE.getMiddleClickInBedwarsShop()
                && HypixelPackets.INSTANCE.getInBedwars()
                && !HypixelPackets.INSTANCE.getInLobby()
                && (containerTitle.equals("Quick Buy")
                || containerTitle.equals("Blocks")
                || containerTitle.equals("Melee")
                || containerTitle.equals("Armor")
                || containerTitle.equals("Tools")
                || containerTitle.equals("Ranged")
                || containerTitle.equals("Potions")
                || containerTitle.equals("Utility")
                || containerTitle.equals("Rotating Items")
                || containerTitle.equals("Upgrades & Traps")
        )) return false;

        return true;
    }

    private static boolean hymod$moreThanOneButton(String text) {
        text = text.toLowerCase();

        return text.contains("right-click")
                || text.contains("right click")
                || text.contains("left-click")
                || text.contains("left click");
    }
}
