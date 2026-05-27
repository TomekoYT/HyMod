package tomeko.hymod.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.client.ClientCommandHandler;

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
        sendCoords();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    private static void sendCoords() {
        Minecraft mc = Minecraft.getMinecraft();
        mc.thePlayer.sendChatMessage("x: " + (int) mc.thePlayer.posX + ", y: " + (int) mc.thePlayer.posY + ", z: " + (int) mc.thePlayer.posZ);
    }
}
