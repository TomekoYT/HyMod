package tomeko.hymod.chat

//? if = 1.8.9 {
/*import net.minecraft.util.BlockPos
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
*///?} else {
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
//?}
import tomeko.hymod.config.HyModConfig
import tomeko.hymod.utils.StringFormatting
import tomeko.hymod.utils.Waypoint
import tomeko.hymod.utils.WaypointRenderer

object CoordsWaypoints {
    fun register() {
        //? if = 1.8.9 {
        /*MinecraftForge.EVENT_BUS.register(this)
        *///?} else {
        ClientReceiveMessageEvents.GAME.register(CoordsWaypoints::onChatReceive)
        //?}
    }

    //? if = 1.8.9 {
    /*@SubscribeEvent
    *///?} else {
    @JvmStatic
    //?}
    fun onChatReceive(
        //? if = 1.8.9 {
        /*event: ClientChatReceivedEvent
        *///?} else {
        component: Component, fromActionBar: Boolean
        //?}
    ) {
        //? if = 1.8.9 {
        /*if (event.type.toInt() == 2 || event.message == null) return
        *///?} else {
        if (fromActionBar) return
        //?}

        if (!HyModConfig.coordsWaypointsEnabled) return

        val message = StringFormatting.removeFormatting(
            //? if = 1.8.9 {
            /*event.message.unformattedText
            *///?} else {
            component.string
            //?}
        )

        val match = Regex(
            """^(?:<|\[[^]]+]\s*)*(\w+)[>:]\s*x:\s*(-?\d+),\s*y:\s*(-?\d+),\s*z:\s*(-?\d+)(?:\s*(?:\|\s*)?(.*))?$"""
        ).find(message) ?: return

        val owner = match.groupValues[1]
        val x = match.groupValues[2].toInt()
        val y = match.groupValues[3].toInt()
        val z = match.groupValues[4].toInt()
        val text: String = match.groupValues[5].takeIf { it.isNotBlank() } ?: ""

        WaypointRenderer.waypoints.add(
            Waypoint(
                pos = BlockPos(x, y, z),
                boxColor = HyModConfig.coordsWaypointsBoxColor,
                beamColor = HyModConfig.coordsWaypointsBeamColor,
                owner = owner,
                renderOwner = HyModConfig.coordsWaypointsRenderOwner,
                ownerColor = HyModConfig.coordsWaypointsOwnerColor,
                text = text,
                renderText = HyModConfig.coordsWaypointsRenderText,
                textColor = HyModConfig.coordsWaypointsTextColor,
                renderDistance = HyModConfig.coordsWaypointsRenderDistance,
                distanceTextColor = HyModConfig.coordsWaypointsDistanceTextColor,
                tickTime = 20 * HyModConfig.coordsWaypointsTime
            )
        )
    }
}