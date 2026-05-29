package tomeko.hymod.commands;

import net.minecraft.client.Minecraft;
//? if = 1.8.9 {
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.client.ClientCommandHandler;
//?} else {
/*import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.player.LocalPlayer;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
*///?}
import tomeko.hymod.config.HyModConfig;
import tomeko.hymod.utils.HypixelPackets;

public class SendCoordsCommand
        //? if = 1.8.9 {
        extends CommandBase
        //?}
{
    private static final String COMMAND_NAME = "sendcoords";
    private static String defaultMode = HyModConfig.sendcoordsToParty ? "party" : "all";

    public static void register() {
        //? if = 1.8.9 {
        ClientCommandHandler.instance.registerCommand(new SendCoordsCommand());
        //?} else {
        /*ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(literal(COMMAND_NAME)
                        .executes(ctx -> {
                            sendCoords(defaultMode);
                            return 1;
                        })
                        .then(argument("mode", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    builder.suggest("all");
                                    builder.suggest("party");
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    String mode = ctx.getArgument("mode", String.class).toLowerCase();
                                    sendCoords(mode);
                                    return 1;
                                })
                        )
                )
        );
        *///?}
    }

    //? if = 1.8.9 {
    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + COMMAND_NAME + " <all/party>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        String mode = args.length != 0 ? args[0] : defaultMode;
        sendCoords(mode);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
    //?}

    private static void sendCoords(String mode) {
        //? if = 1.8.9 {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        //?} else {
        /*LocalPlayer player = Minecraft.getInstance().player;
        *///?}

        int x = (int)
                //? if = 1.8.9 {
                player.posX;
        //?} else {
            /*player.getX();
        *///?}
        int y = (int)
                //? if = 1.8.9 {
                player.posY;
                //?} else {
                /*player.getY();
        *///?}
        int z = (int)
                //? if = 1.8.9 {
                player.posZ;
                //?} else {
                /*player.getZ();
        *///?}
        String message = "x: " + x + ", y: " + y + ", z: " + z;

        if (!mode.equals("all") && !mode.equals("party")) mode = defaultMode;

        if (HypixelPackets.onHypixel) {
            if (mode.equals("all")) message = "/ac " + message;
            else if (mode.equals("party")) message = "/pc " + message;
        }

        //? if = 1.8.9 {
        player.sendChatMessage(message);
        //?} else {
        /*player.connection.sendChat(message);
        *///?}
    }
}
