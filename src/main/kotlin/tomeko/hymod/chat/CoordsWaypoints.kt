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

        val regex = Regex(
            "^(?:\\w+\\s*>\\s*)?" +
                    "(?:\\[[^]]+]\\s*)*" +
                    "(?:<(?<owner1>\\w+)>|(?<owner2>\\w+)[^:]*:)\\s*" +
                    "x:\\s*(?<x>-?\\d+),\\s*" +
                    "y:\\s*(?<y>-?\\d+),\\s*" +
                    "z:\\s*(?<z>-?\\d+)" +
                    "(?:\\s*(?:\\|\\s*)?(?<text>.*))?$"
        )

        val match = regex.matchEntire(message) ?: return

        val owner = match.groups["owner1"]?.value
            ?: match.groups["owner2"]!!.value

        val x = match.groups["x"]!!.value.toInt()
        val y = match.groups["y"]!!.value.toInt()
        val z = match.groups["z"]!!.value.toInt()
        val text = match.groups["text"]?.value.orEmpty()

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