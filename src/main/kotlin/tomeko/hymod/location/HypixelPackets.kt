package tomeko.hymod.location

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
import tomeko.hymod.utils.Debug
//?}

object HypixelPackets {
    var onHypixel = false
        private set
    var onRBW = false
        private set

    var currentServerName: String? = null
        private set

    var inLobby = false
        private set

    var inBedwars = false
        private set
    var inSkywars = false
        private set
    var inDuels = false
        private set
    var inArcade = false
        private set

    var inFarmHunt = false
        private set
    var duelsMode: DuelsModes? = null
        private set

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

        inLobby = packet.lobbyName.isPresent

        inBedwars = serverTypeName == "Bed Wars"
        inSkywars = serverTypeName == "SkyWars"
        inDuels = serverTypeName == "Duels"
        inArcade = serverTypeName == "Arcade"

        if (!packet.mode.isPresent) {
            disableModes()
            return
        }

        val modeName = packet.mode.get()

        inFarmHunt = inArcade && modeName == "FARM_HUNT"
        duelsMode = if (inDuels) DuelsModes.fromId(modeName) else null
    }

    private fun disableAll() {
        currentServerName = null
        inLobby = false
        disableServerTypes()
        disableModes()
    }

    private fun disableServerTypes() {
        inBedwars = false
        inDuels = false
        inArcade = false
    }

    private fun disableModes() {
        inFarmHunt = false
        duelsMode = null
    }
}