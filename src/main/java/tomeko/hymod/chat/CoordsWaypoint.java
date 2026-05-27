package tomeko.hymod.chat;

import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tomeko.hymod.config.HyModConfig;
import tomeko.hymod.utils.StringFormatting;
import tomeko.hymod.utils.Waypoint;
import tomeko.hymod.utils.WaypointRenderer;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoordsWaypoint {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new CoordsWaypoint());
    }

    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent event) {
        if (event.type == 2 || event.message == null) return;

        String message = StringFormatting.removeFormatting(event.message.getUnformattedText());

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
                        new Color(HyModConfig.coordsWaypointColor.getRed(), HyModConfig.coordsWaypointColor.getGreen(), HyModConfig.coordsWaypointColor.getBlue()),
                        nickname,
                        (float) HyModConfig.coordsWaypointBoxOpacity / 100f,
                        (float) HyModConfig.coordsWaypointBeamOpacity / 100f,
                        HyModConfig.coordsWaypointRenderText,
                        HyModConfig.coordsWaypointRenderDistance,
                        20 * HyModConfig.coordsWaypointTime
                )
        );
    }
}
