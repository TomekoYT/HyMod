package tomeko.hymod.chat;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tomeko.hymod.config.HyModConfig;
import tomeko.hymod.utils.HypixelPackets;

public class WhiteChatMessages {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new WhiteChatMessages());
    }

    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent event) {
        if (event.type == 2 || event.message == null) return;

        //White Private Messages
        if (HyModConfig.whitePrivateMessagesEnabled && HypixelPackets.onHypixel && (event.message.getUnformattedText().startsWith("From ") || event.message.getUnformattedText().startsWith("To "))) {
            IChatComponent message = event.message;
            ChatComponentText newMessage = (ChatComponentText) new ChatComponentText("").setChatStyle(message.getChatStyle().createShallowCopy());

            int n = message.getSiblings().size();
            if (n < 1) return;

            boolean colonPassed = false;
            for (int i = 0; i < n - 1; i++) {
                IChatComponent siblingComponent = message.getSiblings().get(i);
                String sibling = siblingComponent.getUnformattedText();

                if (!colonPassed && sibling.contains(":")) {
                    int colonIndex = sibling.indexOf(":");
                    if (colonIndex > 0) {
                        ChatComponentText beforeColon = new ChatComponentText(sibling.substring(0, colonIndex));
                        beforeColon.setChatStyle(siblingComponent.getChatStyle().createShallowCopy());
                        newMessage.appendSibling(beforeColon);
                    }

                    ChatComponentText colon = new ChatComponentText(":");
                    colon.getChatStyle().setColor(EnumChatFormatting.WHITE);
                    newMessage.appendSibling(colon);

                    if (colonIndex + 1 <= sibling.length() - 1) {
                        ChatComponentText afterColon = new ChatComponentText(sibling.substring(colonIndex + 1));
                        afterColon.setChatStyle(siblingComponent.getChatStyle().createShallowCopy());
                        newMessage.appendSibling(afterColon);
                    }

                    colonPassed = true;
                    continue;
                }

                newMessage.appendSibling(siblingComponent.createCopy());
            }

            if (!colonPassed) {
                return;
            }

            IChatComponent lastSibling = message.getSiblings().get(n - 1).createCopy();
            lastSibling.getChatStyle().setColor(EnumChatFormatting.WHITE);
            newMessage.appendSibling(lastSibling);

            event.message = newMessage;
            return;
        }

        //White No Rank Messages
        if (HyModConfig.whiteNoRankMessagesEnabled && HypixelPackets.onHypixel && event.message.getFormattedText().contains("§7: ")) {
            event.message = new ChatComponentText(event.message.getFormattedText().replace("§7: ", "§f: "));
        }
    }
}
