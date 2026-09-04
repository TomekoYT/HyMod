package tomeko.hymod.stats

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.Minecraft
import tomeko.hymod.config.HyModConfig
import tomeko.hymod.location.HypixelPackets

object TablistStats {
    fun register() {
        //? if = 1.8.9 {
        /*MinecraftForge.EVENT_BUS.register(this)
        *///?} else {
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick)
        //?}
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

        if (!((HypixelPackets.onHypixel && HyModConfig.showNetworkLevelAboveNametag)
                    || (HypixelPackets.inBedwars && (HyModConfig.showBedwarsStarsInTablist || HyModConfig.showBedwarsStarsAboveNametag))
                    || (HypixelPackets.inSkywars && (HyModConfig.showSkywarsStarsInTablist || HyModConfig.showSkywarsStarsAboveNametag))
                    || (HypixelPackets.inDuels && (HyModConfig.showDuelsDivisionInTablist || HyModConfig.showDuelsDivisionAboveNametag))
                    )
        ) return

        val connection = Minecraft.getInstance().connection ?: return

        for (info in connection.onlinePlayers) {
            val uuid = info.profile.id.toString()
            AbyssStatsFetcher.requestStats(uuid)
        }
    }
}