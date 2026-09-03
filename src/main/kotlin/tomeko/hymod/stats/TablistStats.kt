package tomeko.hymod.stats

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.hypixel.modapi.HypixelModAPI
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import tomeko.hymod.config.HyModConfig
import tomeko.hymod.location.HypixelPackets
import java.util.concurrent.ConcurrentHashMap
import java.util.function.BiConsumer
import java.util.function.Consumer

object TablistStats {
    private var lastHypixelState = false

    fun register() {
        //? if = 1.8.9 {
        /*MinecraftForge.EVENT_BUS.register(this)
        *///?} else {
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick)
        //?}
        HypixelModAPI.getInstance().createHandler(ClientboundLocationPacket::class.java, this::onLocationPacket)
        HypixelModAPI.getInstance().subscribeToEventPacket(ClientboundLocationPacket::class.java)
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

        if (lastHypixelState != HypixelPackets.onHypixel) {
            lastHypixelState = HypixelPackets.onHypixel
            clearCache()
        }
    }

    private fun onLocationPacket(packet: ClientboundLocationPacket) {
        clearCache()
    }

    private val prefixCache = ConcurrentHashMap<String, Component>()

    private val pendingBedwars: ConcurrentHashMap.KeySetView<String?, Boolean?> = ConcurrentHashMap.newKeySet<String>()
    private val pendingSkywars: ConcurrentHashMap.KeySetView<String?, Boolean?> = ConcurrentHashMap.newKeySet<String>()
    private val pendingDuels: ConcurrentHashMap.KeySetView<String?, Boolean?> = ConcurrentHashMap.newKeySet<String>()

    fun getTablistPrefix(uuid: String): Component? {
        if (HypixelPackets.inBedwars && HyModConfig.showBedwarsStarsInTablist && pendingBedwars.add(uuid)) {
            AbyssStatsFetcher.getBedwarsStars(uuid)
                .thenAccept(Consumer { bedwars: Component? ->
                    if (bedwars != null) {
                        prefixCache[uuid] = bedwars
                    }
                })
                .whenComplete(BiConsumer { _: Void?, _: Throwable? ->
                    pendingBedwars.remove(uuid)
                })
        }

        if (HypixelPackets.inSkywars && HyModConfig.showSkywarsStarsInTablist && pendingSkywars.add(uuid)) {
            AbyssStatsFetcher.getSkywarsStars(uuid)
                .thenAccept(Consumer { skywars: Component? ->
                    if (skywars != null) {
                        prefixCache[uuid] = skywars
                    }
                })
                .whenComplete(BiConsumer { _: Void?, _: Throwable? ->
                    pendingSkywars.remove(uuid)
                })
        }

        if (HypixelPackets.inDuels && HyModConfig.showDuelsDivisionInTablist && pendingDuels.add(uuid)) {
            AbyssStatsFetcher.getDuelsDivision(uuid, HypixelPackets.duelsMode)
                .thenAccept(Consumer { division: String? ->
                    if (division != null) {
                        prefixCache[uuid] = Component.literal(division)
                    }
                })
                .whenComplete(BiConsumer { _: Void?, _: Throwable? ->
                    pendingDuels.remove(uuid)
                })
        }

        return prefixCache[uuid]
    }

    private fun clearCache() {
        prefixCache.clear()

        pendingBedwars.clear()
        pendingSkywars.clear()
        pendingDuels.clear()
    }
}