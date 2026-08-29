package tomeko.hymod.utils

//? if = 1.8.9 {
/*import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.init.Blocks
import net.minecraft.inventory.ContainerChest
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.MovingObjectPosition
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
*///?} else {
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.inventory.ContainerScreen
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.BlockHitResult
//?}

import tomeko.hymod.config.HyModConfig
import tomeko.hymod.bedwars.BedwarsResourceDisplay
import java.util.regex.Pattern

object ItemTracker {
    private val mc =
    //? if = 1.8.9 {
            /*Minecraft.getMinecraft()
        *///?} else {
        Minecraft.getInstance()
//?}

    private var lastServerName: String? = null
    private var lastBlock: Block =
    //? if = 1.8.9 {
            /*Blocks.chest
        *///?} else {
        Blocks.CHEST
    //?}

    var inventory = HashMap<Item, Int>()
    var enderChest = HashMap<Item, Int>()

    private val trackedItemNames = mutableListOf(
        "Iron Ingot",
        "Gold Ingot",
        "Diamond",
        "Emerald"
    )

    fun register() {
        //? if = 1.8.9 {
        /*MinecraftForge.EVENT_BUS.register(this)
        *///?} else {
        ClientTickEvents.END_CLIENT_TICK.register { _ ->
            scanInventory()
            scanEnderChest()
            stopTracking()
            trackBlock()
        }

        ClientReceiveMessageEvents.GAME.register(ItemTracker::scanMessage)
        //?}

        resetTracker()
    }

    //? if = 1.8.9 {
    /*@SubscribeEvent
    *///?}
    fun scanInventory(
        //? if = 1.8.9 {
        /*event: TickEvent.ClientTickEvent
        *///?}
    ) {
        //? if = 1.8.9 {
        /*if (event.phase != TickEvent.Phase.END || mc.thePlayer == null || mc.thePlayer.inventory == null) return
        *///?} else {
        if (mc.player == null) return
        //?}

        val newInventory = HashMap<Item, Int>()
        for (item in BedwarsResourceDisplay.items) {
            newInventory[item] = 0
        }

        //? if = 1.8.9 {
        /*val mainInventory: Array<ItemStack?> = mc.thePlayer.inventory.mainInventory
        *///?} else {
        val mainInventory: Inventory = mc.player!!.inventory
        //?}

        for (stack in mainInventory) {
            //? if = 1.8.9 {
            /*if (stack == null) continue
            *///?} else {
            if (stack.isEmpty) continue
            //?}

            for (item in BedwarsResourceDisplay.items) {
                if (item == stack.item) {
                    val count =
                    //? if = 1.8.9 {
                            /*stack.stackSize
                        *///?} else {
                        stack.count
                    //?}

                    newInventory[item] = newInventory[item]!! + count
                }
            }
        }

        inventory = newInventory
    }

    //? if = 1.8.9 {
    /*@SubscribeEvent
    *///?}
    fun scanEnderChest(
        //? if = 1.8.9 {
        /*event: TickEvent.ClientTickEvent
        *///?}
    ) {
        val screen =
        //? if = 1.8.9 {
                /*mc.currentScreen
            *///?} else if >= 26.2 {
                /*mc.gui.screen()
                *///?} else {
            mc.screen
        //?}

        //? if = 1.8.9 {
        /*if (event.phase != TickEvent.Phase.END || screen !is GuiChest || mc.thePlayer.openContainer !is ContainerChest) return
        *///?} else {
        if (screen !is ContainerScreen) return
        //?}

        if (lastBlock !=
            //? if = 1.8.9 {
            /*Blocks.ender_chest
        *///?} else {
            Blocks.ENDER_CHEST
        //?}
        ) return

        val containerInventory =
        //? if = 1.8.9 {
                /*(mc.thePlayer.openContainer as ContainerChest).lowerChestInventory
            *///?} else {
            screen.menu.container
        //?}

        val newEnderChest = HashMap<Item, Int>()
        for (item in BedwarsResourceDisplay.items) {
            newEnderChest[item] = 0
        }

        for (
        i in 0 until
                //? if = 1.8.9 {
                /*containerInventory.sizeInventory
        *///?} else {
                containerInventory.containerSize
//?}
        ) {
            val stack =
            //? if = 1.8.9 {
                    /*containerInventory.getStackInSlot(i) ?: continue
                *///?} else {
                containerInventory.getItem(i)
            //?}

            //? if >= 1.21.11 {
            if (stack.isEmpty) continue
            //?}

            for (item in BedwarsResourceDisplay.items) {
                if (item == stack.item) {
                    val count =
                    //? if = 1.8.9 {
                            /*stack.stackSize
                        *///?} else {
                        stack.count
                    //?}

                    newEnderChest[item] = newEnderChest[item]!! + count
                }
            }
        }

        enderChest = newEnderChest
    }

    //? if = 1.8.9 {
    /*@SubscribeEvent
    *///?}
    fun scanMessage(
        //? if = 1.8.9 {
        /*event: ClientChatReceivedEvent
        *///?} else {
        component: Component,
        fromActionBar: Boolean
        //?}
    ) {
        //? if = 1.8.9 {
        /*if (event.type.toInt() == 2 || event.message == null) return
        *///?} else {
        if (fromActionBar) return
        //?}

        val message =
        //? if = 1.8.9 {
                /*event.message.unformattedText
                *///?} else {
            component.string
                //?}
                .removeFormatting()

        val pattern =
            Pattern.compile("^Deposited x\\d+ (.+) into Ender Chest! \\((\\d+) Total\\)$")
        val matcher = pattern.matcher(message)

        if (!matcher.matches()) return

        val name = matcher.group(1)
        val amount = matcher.group(2).toInt()

        if (!trackedItemNames.contains(name)) return

        val id = name.lowercase().replace(" ", "_")

        enderChest[
            //? if = 1.8.9 {
            /*Item.getByNameOrId("minecraft:$id")
            *///?} else {
            BuiltInRegistries.ITEM.getValue(
                Identifier.fromNamespaceAndPath("minecraft", id)
            )
            //?}
        ] = amount
    }

    //? if = 1.8.9 {
    /*@SubscribeEvent
    *///?}
    fun stopTracking(
        //? if = 1.8.9 {
        /*event: TickEvent.ClientTickEvent
        *///?}
    ) {
        //? if = 1.8.9 {
        /*if (event.phase != TickEvent.Phase.END) return
        *///?}

        if (HypixelPackets.inBedwars && lastServerName != HypixelPackets.currentServerName) {
            resetTracker()
        }

        lastServerName = HypixelPackets.currentServerName

        if (HyModConfig.debugModeEnabled || HypixelPackets.inBedwars || HypixelPackets.onRBW) return

        resetTracker()
    }

    private fun resetTracker() {
        for (item in BedwarsResourceDisplay.items) {
            inventory[item] = 0
            enderChest[item] = 0
        }
    }

    //? if = 1.8.9 {
    /*@SubscribeEvent
    *///?}
    fun trackBlock(
        //? if = 1.8.9 {
        /*event: TickEvent.ClientTickEvent
        *///?}
    ) {
        //? if = 1.8.9 {
        /*if (event.phase != TickEvent.Phase.END) return
        *///?}

        val lookingAt =
        //? if = 1.8.9 {
                /*mc.objectMouseOver ?: return
            *///?} else {
            mc.hitResult ?: return
        //?}

        //? if = 1.8.9 {
        /*if (lookingAt.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) return
        *///?} else {
        if (lookingAt !is BlockHitResult) return
        //?}

        val block =
        //? if = 1.8.9 {
                /*mc.theWorld?.getBlockState(lookingAt.blockPos)?.block ?: return
            *///?} else {
            mc.level?.getBlockState(lookingAt.blockPos)?.block ?: return
        //?}

        //? if = 1.8.9 {
        /*if (block != Blocks.chest && block != Blocks.trapped_chest && block != Blocks.ender_chest) return
        *///?} else {
        if (block != Blocks.CHEST && block != Blocks.TRAPPED_CHEST && block != Blocks.ENDER_CHEST) return
        //?}

        lastBlock = block
    }
}