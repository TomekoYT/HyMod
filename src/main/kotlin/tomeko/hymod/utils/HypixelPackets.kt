package tomeko.hymod.utils

import net.hypixel.modapi.HypixelModAPI
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ServerData
//? if = 1.8.9 {
/*import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
*///?} else {
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
//?}

object HypixelPackets {
    @JvmField
    var onHypixel = false
    var onRBW = false

    var currentServerName: String? = null

    @JvmField
    var inSkyblock = false
    @JvmField
    var inRavengard = false
    var inBedwars = false
    var inArcade = false

    var inFarmHunt = false

    fun register() {
        //? if = 1.8.9 {
        /*MinecraftForge.EVENT_BUS.register(this)
        *///?} else {
        ClientTickEvents.END_CLIENT_TICK.register(HypixelPackets::onTick)
        //?}

        HypixelModAPI.getInstance()
            .createHandler(
                ClientboundLocationPacket::class.java,
                HypixelPackets::onLocationPacket
            )

        HypixelModAPI.getInstance()
            .subscribeToEventPacket(ClientboundLocationPacket::class.java)
    }

    //? if = 1.8.9 {
    /*@SubscribeEvent
    *///?}
    fun onTick(
        //? if = 1.8.9 {
        /*event: TickEvent.ClientTickEvent
        *///?} else {
        mc: Minecraft
        //?}
    ) {
        //? if = 1.8.9 {
        /*if (event.phase != TickEvent.Phase.END) return
        *///?}

        checkHypixel()
    }

    private fun checkHypixel() {
        val server: ServerData? =
        //? if = 1.8.9 {
                /*Minecraft.getMinecraft().currentServerData
            *///?} else {
            Minecraft.getInstance().currentServer
        //?}

        val ip =
        //? if = 1.8.9 {
                /*server?.serverIP ?: return
            *///?} else {
            server?.ip ?: return
        //?}

        onHypixel = ip.endsWith("hypixel.net")
        onRBW = ip.endsWith("rbw.gg")

        if (!onHypixel) {
            disableAll()
        }
    }

    private fun onLocationPacket(packet: ClientboundLocationPacket) {
        if (!packet.serverType.isPresent) {
            disableAll()
            return
        }

        currentServerName = packet.serverName

        val serverTypeName = packet.serverType.get().name

        inSkyblock = serverTypeName == "SkyBlock"
        inBedwars = serverTypeName == "Bed Wars"
        inArcade = serverTypeName == "Arcade"

        if (!packet.mode.isPresent) {
            disableModes()
            return
        }

        val modeName = packet.mode.get()

        inFarmHunt = inArcade && modeName == "FARM_HUNT"

        inRavengard = modeName.startsWith("RAVENGARD")
    }

    private fun disableAll() {
        currentServerName = null
        disableServerTypes()
        disableModes()
    }

    private fun disableServerTypes() {
        inSkyblock = false
        inBedwars = false
        inArcade = false
    }

    private fun disableModes() {
        inFarmHunt = false
        inRavengard = false
    }
}