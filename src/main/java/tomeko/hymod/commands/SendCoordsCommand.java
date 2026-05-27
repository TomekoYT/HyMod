package tomeko.hymod.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.client.ClientCommandHandler;
import tomeko.hymod.config.HyModConfig;
import tomeko.hymod.utils.HypixelPackets;

public class SendCoordsCommand extends CommandBase {
    public static void register() {
        ClientCommandHandler.instance.registerCommand(new SendCoordsCommand());
    }

    @Override
    public String getCommandName() {
        return "sendcoords";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sendcoords <all/party>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        String mode = args.length != 0 ? args[0] : HyModConfig.sendcoordsMode ? "party" : "all";
        sendCoords(mode);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    private static void sendCoords(String mode) {
        Minecraft mc = Minecraft.getMinecraft();
        String message = "x: " + (int) mc.thePlayer.posX + ", y: " + (int) mc.thePlayer.posY + ", z: " + (int) mc.thePlayer.posZ;

        if (HypixelPackets.onHypixel) {
            if (mode.equals("all")) message = "/ac " + message;
            else if (mode.equals("party")) message = "/pc " + message;
        }

        mc.thePlayer.sendChatMessage(message);
    }
}
