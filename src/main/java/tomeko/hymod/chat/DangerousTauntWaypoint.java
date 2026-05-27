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

public class DangerousTauntWaypoint {
    private static final int y = 50;
    private static final int seconds = 15;

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new DangerousTauntWaypoint());
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (event.type == 2 || event.message == null || !HyModConfig.dangerousTauntWaypointEnabled) return;

        String message = StringFormatting.removeFormatting(event.message.getUnformattedText());
        String[] words = message.split(" ");
        if (words.length != 12
                || (!words[0].equals("A") && !words[0].equals("An"))
                || !words[3].equals("from")
                || !words[5].equals("blocks")
                || !words[6].equals("away!")
                || !words[7].equals("They")
                || !words[8].equals("are")
                || !words[9].equals("around:")
        ) return;

        String animal = words[1];
        int x = Integer.parseInt(words[10].substring(1, words[10].length() - 1));
        int z = Integer.parseInt(words[11].substring(0, words[11].length() - 1));

        Color color = Color.GREEN;
        switch (animal) {
            case "Chicken":
                color = Color.YELLOW;
                break;
            case "Sheep":
                color = Color.WHITE;
                break;
            case "Pig":
                color = Color.PINK;
                break;
            case "Cow":
                color = Color.BLACK;
                break;
            case "Horse":
                color = new Color(150, 75, 0);
                break;
            case "Ocelot":
                color = new Color(241, 226, 201);
                break;
            case "Wolf":
                color = Color.LIGHT_GRAY;
                break;
            case "Donkey":
                color = Color.GRAY;
                break;
        }

        WaypointRenderer.waypoints.add(
                new Waypoint(
                        new BlockPos(x, y, z),
                        color,
                        animal,
                        1f,
                        1f,
                        true,
                        true,
                        20 * seconds
                )
        );
    }
}
