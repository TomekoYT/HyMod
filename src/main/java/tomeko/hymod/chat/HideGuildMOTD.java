package tomeko.hymod.chat;

//? if = 1.8.9 {
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//?} else {
/*import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.network.chat.Component;
*///?}
import tomeko.hymod.config.HyModConfig;
import tomeko.hymod.utils.StringFormatting;

public class HideGuildMOTD {
    private static boolean guildMOTD = false;

    public static void register() {
        //? if = 1.8.9 {
        MinecraftForge.EVENT_BUS.register(new HideGuildMOTD());
        //?} else {
        /*ClientReceiveMessageEvents.ALLOW_GAME.register(HideGuildMOTD::onChatReceive);
        *///?}
    }

    //? if = 1.8.9 {
    @SubscribeEvent
    //?}
    public
            //? if = 1.8.9 {
    void
            //?} else {
    /*static boolean
    *///?}
    onChatReceive(
            //? if = 1.8.9 {
            ClientChatReceivedEvent event
            //?} else {
            /*Component message, boolean fromActionBar
            *///?}
    ) {
        //? if = 1.8.9 {
        if (event.type == 2 || event.message == null) return;

        if (shouldCancel(event.message.getUnformattedText())) event.setCanceled(true);
        //?} else {
        /*if (fromActionBar || message == null) return true;

        return !shouldCancel(message.getString());
        *///?}
    }

    private static boolean shouldCancel(String message) {
        if (!HyModConfig.hideGuildMOTDEnabled) return false;

        message = StringFormatting.removeFormatting(message);

        if (message.startsWith("--------------  Guild: Message Of The Day  --------------")) {
            guildMOTD = true;
        }

        if (guildMOTD) {
            if (message.endsWith("-----------------------------------------------------")) {
                guildMOTD = false;
            }
            return true;
        }
        return false;
    }
}
