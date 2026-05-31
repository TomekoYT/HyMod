package tomeko.hymod.utils;

import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
//? if = 1.8.9 {
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
//?} else {
/*import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
 *///?}

public class HypixelPackets {
    public static boolean onHypixel = false;
    public static boolean inSkyblock = false;
    public static boolean inBedwars = false;
    public static boolean inArcade = false;

    public static void register() {
        //? if = 1.8.9 {
        MinecraftForge.EVENT_BUS.register(new HypixelPackets());
        //?} else {
        /*ClientTickEvents.END_CLIENT_TICK.register(HypixelPackets::onTick);
         *///?}
        HypixelModAPI.getInstance().createHandler(ClientboundLocationPacket.class, HypixelPackets::onLocationPacket);
        HypixelModAPI.getInstance().subscribeToEventPacket(ClientboundLocationPacket.class);
    }

    //? if = 1.8.9 {
    @SubscribeEvent
            //?} else {
    /*static
     *///?}
    public void onTick(
            //? if = 1.8.9 {
            TickEvent.ClientTickEvent event
            //?} else {
            /*Minecraft mc
             *///?}
    ) {
        //? if = 1.8.9 {
        if (event.phase != TickEvent.Phase.END) return;
        //?}

        checkHypixel();
    }

    private static void checkHypixel() {
        ServerData server =
                //? if = 1.8.9 {
                Minecraft.getMinecraft().getCurrentServerData();
        //?} else {
        /*Minecraft.getInstance().getCurrentServer();
         *///?}

        if (
                server == null
                        //? if = 1.8.9 {
                        || !server.serverIP
                        //?} else {
                        /*|| !server.ip
                        *///?}
                        .contains("hypixel")
        ) {
            onHypixel = false;
            disableModes();
            return;
        }

        onHypixel = true;
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
