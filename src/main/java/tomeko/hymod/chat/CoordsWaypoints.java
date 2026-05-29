package tomeko.hymod.chat;

//? if = 1.8.9 {
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//?} else {
/*import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
*///?}
import tomeko.hymod.config.HyModConfig;
import tomeko.hymod.utils.StringFormatting;
import tomeko.hymod.utils.Waypoint;
import tomeko.hymod.utils.WaypointRenderer;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoordsWaypoints {
    public static void register() {
        //? if = 1.8.9 {
        MinecraftForge.EVENT_BUS.register(new CoordsWaypoints());
        //?} else {
        /*ClientReceiveMessageEvents.GAME.register(CoordsWaypoints::onChatReceive);
        *///?}
    }

    //? if = 1.8.9 {
    @SubscribeEvent
    //?} else {
    /*static
            *///?}
    public void onChatReceive(
            //? if = 1.8.9 {
            ClientChatReceivedEvent event
            //?} else {
            /*Component component, boolean fromActionBar
            *///?}
    ) {
        //? if = 1.8.9 {
        if (event.type == 2 || event.message == null) return;
        //?} else {
        /*if (fromActionBar || component == null) return;
        *///?}

        if (!HyModConfig.coordsWaypointsEnabled) return;

        String message = StringFormatting.removeFormatting(
                //? if = 1.8.9 {
                event.message.getUnformattedText()
                //?} else {
                /*component.getString()
                *///?}
        );

        Pattern pattern = Pattern.compile(".*?[<\\[]?([A-Za-z0-9_]+)[>\\]]?\\s*:?\\s*x:\\s*(-?\\d+),\\s*y:\\s*(-?\\d+),\\s*z:\\s*(-?\\d+)$");
        Matcher matcher = pattern.matcher(message);
        if (!matcher.matches()) return;

        String nickname = matcher.group(1);
        int x = Integer.parseInt(matcher.group(2));
        int y = Integer.parseInt(matcher.group(3));
        int z = Integer.parseInt(matcher.group(4));

        WaypointRenderer.waypoints.add(
                new Waypoint(
                        new BlockPos(x, y, z),
                        new Color(HyModConfig.coordsWaypointsColor.getRed(), HyModConfig.coordsWaypointsColor.getGreen(), HyModConfig.coordsWaypointsColor.getBlue()),
                        nickname,
                        (float) HyModConfig.coordsWaypointsBoxOpacity / 100f,
                        (float) HyModConfig.coordsWaypointsBeamOpacity / 100f,
                        HyModConfig.coordsWaypointsRenderText,
                        HyModConfig.coordsWaypointsRenderDistance,
                        20 * HyModConfig.coordsWaypointsTime
                )
        );
    }
}
