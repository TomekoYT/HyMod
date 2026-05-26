package tomeko.hymod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tomeko.hymod.hud.BedwarsResourceDisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemTracker {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static Map<Item, Integer> inventory = new HashMap<>();
    public static Map<Item, Integer> enderChest = new HashMap<>();

    private static final List<String> trackedItemNames = new ArrayList<>();

    static {
        trackedItemNames.add("Iron Ingot");
        trackedItemNames.add("Gold Ingot");
        trackedItemNames.add("Diamond");
        trackedItemNames.add("Emerald");
    }

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new ItemTracker());

        resetTracker();
    }

    @SubscribeEvent
    public void scanInventory(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        if (mc.thePlayer == null || mc.thePlayer.inventory == null) return;

        Map<Item, Integer> newInventory = new HashMap<>();
        for (Item item : BedwarsResourceDisplay.items) {
            newInventory.put(item, 0);
        }

        for (ItemStack stack : mc.thePlayer.inventory.mainInventory) {
            if (stack == null) continue;

            for (Item item : BedwarsResourceDisplay.items) {
                if (item == stack.getItem()) {
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

        Map<Item, Integer> newEnderChest = new HashMap<>();
        for (Item item : BedwarsResourceDisplay.items) {
            newEnderChest.put(item, 0);
        }

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack == null) continue;

            for (Item item : BedwarsResourceDisplay.items) {
                if (item == stack.getItem()) {
                    newEnderChest.put(item, newEnderChest.get(item) + stack.stackSize);
                }
            }
        }
        enderChest = newEnderChest;
    }

    @SubscribeEvent
    public void scanMessage(ClientChatReceivedEvent event) {
        if (event.type == 2 || event.message == null) return;

        String message = event.message.getUnformattedText();
        Pattern pattern = Pattern.compile("^Deposited x\\d+ (.+) into Ender Chest! \\((\\d+) Total\\)$");
        Matcher matcher = pattern.matcher(message);

        if (!matcher.matches()) return;

        String name = matcher.group(1);
        int amount = Integer.parseInt(matcher.group(2));

        if (!trackedItemNames.contains(name)) return;

        String id = "minecraft:" + name.toLowerCase().replace(" ", "_");
        enderChest.put(Item.getByNameOrId(id), amount);
    }

    private static void resetTracker() {
        for (Item item : BedwarsResourceDisplay.items) {
            inventory.put(item, 0);
            enderChest.put(item, 0);
        }
    }
}
