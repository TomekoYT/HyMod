package tomeko.hymod.chat

import net.minecraft.client.Minecraft
//? if = 1.8.9 {
import net.minecraft.block.Block
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//?} else {
/*import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
*///?}

import tomeko.hymod.config.HyModConfig
import tomeko.hymod.utils.HypixelPackets
import tomeko.hymod.utils.StringFormatting
import tomeko.hymod.utils.Waypoint
import tomeko.hymod.utils.WaypointRenderer

import java.awt.Color

object DangerousTauntWaypoint {
    private val AIR: Block =
        //? if = 1.8.9 {
        Blocks.air
    //?} else {
    /*Blocks.AIR
     *///?}

    private const val SECONDS = 15

    fun register() {
        //? if = 1.8.9 {
        MinecraftForge.EVENT_BUS.register(this)
        //?} else {
        /*ClientReceiveMessageEvents.GAME.register(DangerousTauntWaypoint::onChat)
         *///?}
    }

    //? if = 1.8.9 {
    @SubscribeEvent
//?}

    fun onChat(
        //? if = 1.8.9 {
        event: ClientChatReceivedEvent
        //?} else {
        /*component: Component,
        fromActionBar: Boolean
        *///?}
    ) {
        //? if = 1.8.9 {
        if (event.type.toInt() == 2 || event.message == null)
        //?} else {
        /*if (fromActionBar || component == null)
         *///?}
            return

        if (!HyModConfig.dangerousTauntWaypointEnabled || !HypixelPackets.inFarmHunt) return

        val message = StringFormatting.removeFormatting(
            //? if = 1.8.9 {
            event.message.unformattedText
            //?} else {
            /*component.string
             *///?}
        )

        val words = message.split(" ")
        if (words.size != 12
            || (words[0] != "A" && words[0] != "An")
            || words[3] != "from"
            || words[5] != "blocks"
            || words[6] != "away!"
            || words[7] != "They"
            || words[8] != "are"
            || words[9] != "around:"
        ) return

        val animal = words[1]
        val x = words[10].substring(1, words[10].length - 1).toInt()
        val z = words[11].substring(0, words[11].length - 1).toInt()

        val color = when (animal) {
            "Chicken" -> Color.YELLOW
            "Sheep" -> Color.WHITE
            "Pig" -> Color.PINK
            "Cow" -> Color.BLACK
            "Horse" -> Color(150, 75, 0)
            "Ocelot" -> Color(241, 226, 201)
            "Wolf" -> Color.LIGHT_GRAY
            "Donkey" -> Color.GRAY
            else -> Color.GREEN
        }

        WaypointRenderer.waypoints.add(
            Waypoint(
                getPos(x, z),
                color,
                animal,
                0.5f,
                1f,
                true,
                true,
                20 * SECONDS
            )
        )
    }

    private fun getPos(x: Int, z: Int): BlockPos {
        //? if = 1.8.9 {
        val world: WorldClient = Minecraft.getMinecraft().theWorld
        //?} else {
        /*val world: ClientLevel = Minecraft.getInstance().level!!
         *///?}

        var pos = BlockPos(x, 0, z)

        while (world.getBlockState(pos).block == AIR) {
            pos = oneHigher(pos)
        }

        while (world.getBlockState(pos).block != AIR) {
            pos = oneHigher(pos)
        }

        return pos
    }

    private fun oneHigher(pos: BlockPos): BlockPos =
        //? if = 1.8.9 {
        pos.up()
    //?} else {
    /*pos.above()
     *///?}
}