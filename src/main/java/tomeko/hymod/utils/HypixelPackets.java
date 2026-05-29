package tomeko.hymod.utils;

import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class HypixelPackets {
    public static boolean onHypixel = false;
    public static boolean inSkyblock = false;
    public static boolean inBedwars = false;
    public static boolean inArcade = false;

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new HypixelPackets());
        HypixelModAPI.getInstance().createHandler(ClientboundLocationPacket.class, HypixelPackets::onLocationPacket);
        HypixelModAPI.getInstance().subscribeToEventPacket(ClientboundLocationPacket.class);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.getCurrentServerData() == null || mc.getCurrentServerData().serverIP == null) {
            onHypixel = false;
            disableModes();
            return;
        }

        onHypixel = mc.getCurrentServerData().serverIP.contains("hypixel");
    }

    private static void onLocationPacket(ClientboundLocationPacket packet) {
        if (!packet.getServerType().isPresent()) {
            disableModes();
            return;
        }

        String packetName = packet.getServerType().get().getName();

        inSkyblock = packetName.equalsIgnoreCase("skyblock");
        inBedwars = packetName.equalsIgnoreCase("bed wars");
        inArcade = packetName.equalsIgnoreCase("arcade");
    }

    private static void disableModes() {
        inSkyblock = false;
        inBedwars = false;
        inArcade = false;
    }
}
