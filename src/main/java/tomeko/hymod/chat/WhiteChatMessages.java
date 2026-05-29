package tomeko.hymod.chat;

//? if = 1.8.9 {

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//?} else {
/*import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
*///?}
import tomeko.hymod.config.HyModConfig;
import tomeko.hymod.utils.HypixelPackets;
import tomeko.hymod.utils.StringFormatting;

public class WhiteChatMessages {
    public static void register() {
        //? if = 1.8.9 {
        MinecraftForge.EVENT_BUS.register(new WhiteChatMessages());
        //?} else {
        /*ClientReceiveMessageEvents.MODIFY_GAME.register(WhiteChatMessages::onChatReceive);
         *///?}
    }

    //? if = 1.8.9 {
    @SubscribeEvent
    public void
    //?} else {
    /*public static Component
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

        event.message = modifyMessage(event.message);
        //?} else {
        /*if (fromActionBar || message == null) return message;

        return modifyMessage(message);
        *///?}
    }

    private static
        //? if = 1.8.9 {
    IChatComponent
    //?} else {
    /*Component
     *///?}
    modifyMessage(
            //? if = 1.8.9 {
            IChatComponent
                    //?} else {
                    /*Component
                     *///?}
                    message
    ) {
        //White Private Messages
        final String unformattedMessage = StringFormatting.removeFormatting(
                //? if = 1.8.9 {
                message.getUnformattedText()
                //?} else {
                /*message.getString()
                 *///?}
        );

        if (HyModConfig.whitePrivateMessagesEnabled && HypixelPackets.onHypixel && (unformattedMessage.startsWith("From ") || unformattedMessage.startsWith("To "))) {
            int n = message.getSiblings().size();
            if (n < 1) return message;

            //? if = 1.8.9 {
            IChatComponent newMessage = new ChatComponentText(unformattedMessage.startsWith("From ") ? "From " : "To ");
            newMessage.getChatStyle().setColor(EnumChatFormatting.LIGHT_PURPLE);
            //?} else {
            /*MutableComponent newMessage = message.plainCopy().withStyle(ChatFormatting.LIGHT_PURPLE);
             *///?}

            boolean colonPassed = false;
            for (int i = 0; i < n - 1; i++) {
                //? if = 1.8.9 {
                IChatComponent siblingComponent = message.getSiblings().get(i);
                String sibling = siblingComponent.getUnformattedText();
                //?} else {
                /*Component siblingComponent = message.getSiblings().get(i);
                String sibling = siblingComponent.getString();
                *///?}

                if (!colonPassed && sibling.contains(":")) {
                    int colonIndex = sibling.indexOf(":");
                    if (colonIndex > 0) {
                        //? if = 1.8.9 {
                        ChatComponentText beforeColon = new ChatComponentText(sibling.substring(0, colonIndex));
                        beforeColon.setChatStyle(siblingComponent.getChatStyle().createShallowCopy());
                        newMessage.appendSibling(beforeColon);
                        //?} else {
                        /*newMessage.append(Component.literal(sibling.substring(0, colonIndex)).setStyle(siblingComponent.getStyle()));
                         *///?}
                    }

                    //? if = 1.8.9 {
                    ChatComponentText colon = new ChatComponentText(":");
                    colon.getChatStyle().setColor(EnumChatFormatting.WHITE);
                    newMessage.appendSibling(colon);
                    //?} else {
                    /*newMessage.append(Component.literal(":").withStyle(ChatFormatting.WHITE));
                     *///?}

                    if (colonIndex + 1 <= sibling.length() - 1) {
                        //? if = 1.8.9 {
                        ChatComponentText afterColon = new ChatComponentText(sibling.substring(colonIndex + 1));
                        afterColon.setChatStyle(siblingComponent.getChatStyle().createShallowCopy());
                        newMessage.appendSibling(afterColon);
                        //?} else {
                        /*newMessage.append(Component.literal(sibling.substring(colonIndex + 1)).setStyle(siblingComponent.getStyle()));
                         *///?}
                    }

                    colonPassed = true;
                    continue;
                }

                //? if = 1.8.9 {
                newMessage.appendSibling(siblingComponent.createCopy());
                //?} else {
                /*newMessage.append(siblingComponent);
                 *///?}
            }

            if (!colonPassed) return message;

            //? if = 1.8.9 {
            IChatComponent lastSibling = message.getSiblings().get(n - 1).createCopy();
            lastSibling.getChatStyle().setColor(EnumChatFormatting.WHITE);
            newMessage.appendSibling(lastSibling);
            //?} else {
            /*newMessage.append(message.getSiblings().get(n - 1).copy().withStyle(ChatFormatting.WHITE));
             *///?}

            return newMessage;
        }

        //White No Rank Messages
        final String formattedMessage =
                //? if = 1.8.9 {
                message.getFormattedText();
        //?} else {
        /*message.getString();
         *///?}
        if (HyModConfig.whiteNoRankMessagesEnabled && HypixelPackets.onHypixel && formattedMessage.contains("§7: ")) {
            return
                    //? if = 1.8.9 {
                    new ChatComponentText(
                            //?} else {
                            /*Component.nullToEmpty(
                             *///?}
                            formattedMessage.replace("§7: ", "§f: ")
                    );
        }

        return message;
    }
}
