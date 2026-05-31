package tomeko.hymod.utils;

import net.minecraft.client.Minecraft;
//? if = 1.8.9 {
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
//?} else {
/*import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
*///?}
import tomeko.hymod.hud.BedwarsResourceDisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemTracker {
    private static final Minecraft mc =
            //? if = 1.8.9 {
            Minecraft.getMinecraft();
    //?} else {
    /*Minecraft.getInstance();
     *///?}

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
        //? if = 1.8.9 {
        MinecraftForge.EVENT_BUS.register(new ItemTracker());
        //?} else {
        /*ClientTickEvents.END_CLIENT_TICK.register(client -> {
            scanInventory();
            scanEnderChest();
            stopTracking();
        });

        ClientReceiveMessageEvents.GAME.register(ItemTracker::scanMessage);
        *///?}

        resetTracker();
    }

    //? if = 1.8.9 {
    @SubscribeEvent
            //?} else {
    /*static
     *///?}
    public void scanInventory(
            //? if = 1.8.9 {
            TickEvent.ClientTickEvent event
            //?}
    ) {
        //? if = 1.8.9 {
        if (event.phase != TickEvent.Phase.END || mc.thePlayer == null || mc.thePlayer.inventory == null) return;
        //?} else {
        /*if (mc.player == null) return;
         *///?}

        Map<Item, Integer> newInventory = new HashMap<>();
        for (Item item : BedwarsResourceDisplay.items) {
            newInventory.put(item, 0);
        }

        //? if = 1.8.9 {
        ItemStack[] mainInventory = mc.thePlayer.inventory.mainInventory;
        //?} else {
        /*Inventory mainInventory = mc.player.getInventory();
         *///?}
        for (ItemStack stack : mainInventory) {
            //? if = 1.8.9 {
            if (stack == null) continue;
            //?} else {
            /*if (stack.isEmpty()) continue;
             *///?}

            for (Item item : BedwarsResourceDisplay.items) {
                if (item == stack.getItem()) {
                    int count =
                            //? if = 1.8.9 {
                            stack.stackSize;
                    //?} else {
                    /*stack.getCount();
                     *///?}

                    newInventory.put(item, newInventory.get(item) + count);
                }
            }
        }
        inventory = newInventory;
    }

    //? if = 1.8.9 {
    @SubscribeEvent
            //?} else {
    /*static
     *///?}
    public void scanEnderChest(
            //? if = 1.8.9 {
            TickEvent.ClientTickEvent event
            //?}
    ) {
        //? if = 1.8.9 {
        if (event.phase != TickEvent.Phase.END || !(mc.currentScreen instanceof GuiChest)) return;

        Container container = mc.thePlayer.openContainer;
        if (!(container instanceof ContainerChest)) return;

        ContainerChest chest = (ContainerChest) container;
        IInventory containerInventory = chest.getLowerChestInventory();
        if (containerInventory.getDisplayName() == null || !containerInventory.getDisplayName().getUnformattedText().equals("Ender Chest"))
            return;
        //?} else {
        /*if (!(mc.screen instanceof ContainerScreen screen) || screen.getTitle().getString().equals("Ender Chest"))
            return;

        Container containerInventory = screen.getMenu().getContainer();
        *///?}

        Map<Item, Integer> newEnderChest = new HashMap<>();
        for (Item item : BedwarsResourceDisplay.items) {
            newEnderChest.put(item, 0);
        }

        for (
                int i = 0;
            //? if = 1.8.9 {
                i < containerInventory.getSizeInventory();
            //?} else {
            /*i < containerInventory.getContainerSize();
             *///?}
                i++
        ) {
            ItemStack stack =
                    //? if = 1.8.9 {
                    containerInventory.getStackInSlot(i);
            //?} else {
            /*containerInventory.getItem(i);
             *///?}

            //? if = 1.8.9 {
            if (stack == null) continue;
            //?} else {
            /*if (stack.isEmpty()) continue;
             *///?}

            for (Item item : BedwarsResourceDisplay.items) {
                if (item == stack.getItem()) {
                    int count =
                            //? if = 1.8.9 {
                            stack.stackSize;
                    //?} else {
                    /*stack.getCount();
                     *///?}

                    newEnderChest.put(item, newEnderChest.get(item) + count);
                }
            }
        }
        enderChest = newEnderChest;
    }

    //? if = 1.8.9 {
    @SubscribeEvent
            //?} else {
    /*static
     *///?}
    public void scanMessage(
            //? if = 1.8.9 {
            ClientChatReceivedEvent event
            //?} else {
            /*Component component,
            boolean fromActionBar
            *///?}
    ) {
        //? if = 1.8.9 {
        if (event.type == 2 || event.message == null) return;
        //?} else {
        /*if (fromActionBar || component == null) return;
         *///?}

        String message = StringFormatting.removeFormatting(
                //? if = 1.8.9 {
                event.message.getUnformattedText()
                //?} else {
                /*component.getString()
                 *///?}
        );
        Pattern pattern = Pattern.compile("^Deposited x\\d+ (.+) into Ender Chest! \\((\\d+) Total\\)$");
        Matcher matcher = pattern.matcher(message);

        if (!matcher.matches()) return;

        String name = matcher.group(1);
        int amount = Integer.parseInt(matcher.group(2));

        if (!trackedItemNames.contains(name)) return;

        String id = name.toLowerCase().replace(" ", "_");
        enderChest.put(
                //? if = 1.8.9 {
                Item.getByNameOrId("minecraft:" + id),
                //?} else {
                /*BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath("minecraft", id)),
                 *///?}
                amount
        );
    }

    //? if = 1.8.9 {
    @SubscribeEvent
            //?} else {
    /*static
     *///?}
    public void stopTracking(
            //? if = 1.8.9 {
            TickEvent.ClientTickEvent event
            //?}
    ) {
        //? if = 1.8.9 {
        if (event.phase != TickEvent.Phase.END) return;
        //?}

        if (HypixelPackets.inBedwars) return;

        resetTracker();
    }

    private static void resetTracker() {
        for (Item item : BedwarsResourceDisplay.items) {
            inventory.put(item, 0);
            enderChest.put(item, 0);
        }
    }
}