package tomeko.hymod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tomeko.hymod.hud.BedwarsResourceDisplay;

import java.util.HashMap;
import java.util.Map;

public class ItemTracker {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static Map<ItemStack, Integer> inventory = new HashMap<>();
    public static Map<ItemStack, Integer> enderChest = new HashMap<>();

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new ItemTracker());

        for (ItemStack item : BedwarsResourceDisplay.items) {
            inventory.put(item, 0);
            enderChest.put(item, 0);
        }
    }

    @SubscribeEvent
    public void scanInventory(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        if (mc.thePlayer == null || mc.thePlayer.inventory == null) return;

        Map<ItemStack, Integer> newInventory = new HashMap<>();
        for (ItemStack item : BedwarsResourceDisplay.items) {
            newInventory.put(item, 0);
        }

        for (ItemStack stack : mc.thePlayer.inventory.mainInventory) {
            if (stack == null) continue;

            for (ItemStack item : BedwarsResourceDisplay.items) {
                if (item.getItem() == stack.getItem()) {
                    newInventory.put(item, newInventory.get(item) + stack.stackSize);
                }
            }
        }
        inventory = newInventory;
    }

    @SubscribeEvent
    public void scanEnderChest(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(mc.currentScreen instanceof GuiChest)) return;

        Container container = mc.thePlayer.openContainer;
        if (!(container instanceof ContainerChest)) return;

        ContainerChest chest = (ContainerChest) container;
        IInventory inventory = chest.getLowerChestInventory();
        if (inventory.getDisplayName() == null || !inventory.getDisplayName().getUnformattedText().equals("Ender Chest"))
            return;

        Map<ItemStack, Integer> newEnderChest = new HashMap<>();
        for (ItemStack item : BedwarsResourceDisplay.items) {
            newEnderChest.put(item, 0);
        }

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack == null) continue;

            for (ItemStack item : BedwarsResourceDisplay.items) {
                if (item.getItem() == stack.getItem()) {
                    newEnderChest.put(item, newEnderChest.get(item) + stack.stackSize);
                }
            }
        }
        enderChest = newEnderChest;
    }
}
