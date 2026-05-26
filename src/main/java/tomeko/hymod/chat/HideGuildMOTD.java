package tomeko.hymod.chat;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tomeko.hymod.config.HyModConfig;

public class HideGuildMOTD {
    private static boolean guildMOTD = false;

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new WhiteChatMessages());
    }

    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent event) {
        if (event.type == 2 || event.message == null) return;

        //Hide Guild MOTD
        String unformattedMessage = event.message.getUnformattedText();
        if (HyModConfig.hideGuildMOTDEnabled) {
            if (unformattedMessage.startsWith("--------------  Guild: Message Of The Day  --------------")) {
                guildMOTD = true;
            }

            if (guildMOTD) {
                if (unformattedMessage.endsWith("-----------------------------------------------------")) {
                    guildMOTD = false;
                }

                event.setCanceled(true);
            }
        }
    }
}
