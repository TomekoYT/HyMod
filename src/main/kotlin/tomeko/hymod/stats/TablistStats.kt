package tomeko.hymod.stats

//? if = 1.8.9-forge {
/*import net.minecraft.client.Minecraft
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
*///?} else {
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.Minecraft
//?}
import tomeko.hymod.config.HyModConfig
import tomeko.hymod.location.HypixelPackets

object TablistStats {
    fun register() {
        //? if = 1.8.9-forge {
        /*MinecraftForge.EVENT_BUS.register(this)
        *///?} else {
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick)
        //?}
    }

    //? if = 1.8.9-forge {
    /*@SubscribeEvent
    *///?}
    fun onTick(
        //? if = 1.8.9-forge {
        /*event: TickEvent.ClientTickEvent
        *///?} else {
        mc: Minecraft
        //?}
    ) {
        //? if = 1.8.9-forge {
        /*if (event.phase != TickEvent.Phase.END) return
        *///?}

        if (!((HypixelPackets.onHypixel && HyModConfig.showNetworkLevelAboveNametag)
                    || (HypixelPackets.inBedwars && (HyModConfig.showBedwarsStarsInTablist || HyModConfig.showBedwarsStarsAboveNametag))
                    || (HypixelPackets.inSkywars && (HyModConfig.showSkywarsStarsInTablist || HyModConfig.showSkywarsStarsAboveNametag))
                    || (HypixelPackets.inDuels && (HyModConfig.showDuelsDivisionInTablist || HyModConfig.showDuelsDivisionAboveNametag))
                    )
        ) return

        val connection =
        //? if = 1.8.9-forge {
                /*Minecraft.getMinecraft().thePlayer?.sendQueue
                *///?} else {
            Minecraft.getInstance().connection
            //?}
                ?: return

        for (info in
        //? if = 1.8.9-forge {
        /*connection.playerInfoMap
        *///?} else {
        connection.onlinePlayers
        //?}
        ) {
            val uuid =
            //? if = 1.8.9-forge {
                    /*info.gameProfile.id
                    *///?} else {
                info.profile.id
            //?}

            if (uuid.version() == 4)
                HypixelStatsFetcher.requestStats(uuid.toString())
        }
    }
}