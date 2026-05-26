package tomeko.hymod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tomeko.hymod.hud.BedwarsResourceDisplay;

import java.util.HashMap;
import java.util.Map;

public class ItemTracker {
    public static Map<ItemStack, Integer> inventory = new HashMap<>();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();
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
}
