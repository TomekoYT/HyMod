package tomeko.hymod.chat;

import net.minecraft.client.Minecraft;
//? if = 1.8.9 {
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//?} else {
/*import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
*///?}
import tomeko.hymod.config.HyModConfig;
import tomeko.hymod.utils.HypixelPackets;
import tomeko.hymod.utils.StringFormatting;
import tomeko.hymod.utils.Waypoint;
import tomeko.hymod.utils.WaypointRenderer;

import java.awt.Color;

public class DangerousTauntWaypoint {
    private static final Block AIR =
            //? if = 1.8.9 {
            Blocks.air;
            //?} else {
            /*Blocks.AIR;
    *///?}

    private static final int seconds = 15;

    public static void register() {
        //? if = 1.8.9 {
        MinecraftForge.EVENT_BUS.register(new DangerousTauntWaypoint());
        //?} else {
        /*ClientReceiveMessageEvents.GAME.register(DangerousTauntWaypoint::onChat);
        *///?}
    }

    //? if = 1.8.9 {
    @SubscribeEvent
    //?} else {
    /*static
            *///?}
    public void onChat(
            //? if = 1.8.9 {
            ClientChatReceivedEvent event
            //?} else {
            /*Component component,
            boolean fromActionBar
            *///?}
    ) {
        //? if = 1.8.9 {
        if (event.type == 2 || event.message == null)
        //?} else {
        /*if (fromActionBar || component == null)
            *///?}
            return;

        if (!HyModConfig.dangerousTauntWaypointEnabled || !HypixelPackets.inArcade) return;

        String message = StringFormatting.removeFormatting(
                //? if = 1.8.9 {
                event.message.getUnformattedText()
                //?} else {
                /*component.getString()
                *///?}
        );
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
                        getPos(x, z),
                        color,
                        animal,
                        0.5f,
                        1f,
                        true,
                        true,
                        20 * seconds
                )
        );
    }

    private static BlockPos getPos(int x, int z) {
        //? if = 1.8.9 {
        WorldClient world = Minecraft.getMinecraft().theWorld;
        //?} else {
        /*ClientLevel world = Minecraft.getInstance().level;
        *///?}

        BlockPos pos = new BlockPos(x, 0, z);
        while (world.getBlockState(pos).getBlock() == AIR) {
            pos = oneHigher(pos);
        }

        while (world.getBlockState(pos).getBlock() != AIR) {
            pos = oneHigher(pos);
        }

        return pos;
    }

    private static BlockPos oneHigher(BlockPos pos) {
        //? if = 1.8.9 {
        return pos.up();
        //?} else {
        /*return pos.above();
        *///?}
    }
}
