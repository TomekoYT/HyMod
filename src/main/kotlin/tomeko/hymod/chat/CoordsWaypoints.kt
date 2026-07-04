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

import java.awt.Color
import java.util.regex.Pattern

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

        val pattern = Pattern.compile(
            """.*?[<\[]?([A-Za-z0-9_]+)[>\]]?\s*:?\s*x:\s*(-?\d+),\s*y:\s*(-?\d+),\s*z:\s*(-?\d+)(?:\s*(?:\|\s*)?(.*))?$"""
        )

        val matcher = pattern.matcher(message)
        if (!matcher.matches()) return

        val owner = matcher.group(1)
        val x = matcher.group(2).toInt()
        val y = matcher.group(3).toInt()
        val z = matcher.group(4).toInt()
        val text: String = matcher.group(5)?.takeIf { it.isNotBlank() } ?: ""

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