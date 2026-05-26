package tomeko.hymod.utils;

import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;

public class HypixelPackets {
    public static boolean onHypixel = false;
    public static boolean inSkyblock = false;
    public static boolean inBedwars = false;

    public static void register() {
        HypixelModAPI.getInstance().createHandler(ClientboundLocationPacket.class, HypixelPackets::onLocationPacket);
        HypixelModAPI.getInstance().subscribeToEventPacket(ClientboundLocationPacket.class);
    }

    private static void onLocationPacket(ClientboundLocationPacket packet) {
        if (!packet.getServerType().isPresent()) {
            onHypixel = false;
            inSkyblock = false;
            inBedwars = false;
            return;
        }
        onHypixel = true;

        inSkyblock = packet.getServerType().get().getName().equalsIgnoreCase("skyblock");
        inBedwars = packet.getServerType().get().getName().equalsIgnoreCase("bed wars");
    }
}
