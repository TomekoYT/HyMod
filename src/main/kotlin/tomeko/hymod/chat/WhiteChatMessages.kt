package tomeko.hymod.chat

//? if = 1.8.9-forge {
/*import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.IChatComponent
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
*///?} else {
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
//?}

import tomeko.hymod.config.HyModConfig
import tomeko.hymod.location.HypixelPackets
import tomeko.hymod.utils.removeFormatting

object WhiteChatMessages {
    fun register() {
        //? if = 1.8.9-forge {
        /*MinecraftForge.EVENT_BUS.register(this)
        *///?} else {
        ClientReceiveMessageEvents.MODIFY_GAME.register(WhiteChatMessages::onChatReceive)
        //?}
    }

    //? if = 1.8.9-forge {
    /*@SubscribeEvent
*///?}
    fun onChatReceive(
        //? if = 1.8.9-forge {
        /*event: ClientChatReceivedEvent
        *///?} else {
        message: Component, fromActionBar: Boolean
        //?}
    )
    //? if >= 1.21.11-fabric {
            : Component
    //?}
    {
        //? if = 1.8.9-forge {
        /*if (event.type.toInt() == 2 || event.message == null) return

        event.message = modifyMessage(event.message)
        *///?} else {
        if (fromActionBar) return message

        return modifyMessage(message)
        //?}
    }

    private fun modifyMessage(
        //? if = 1.8.9-forge {
        /*message: IChatComponent
        *///?} else {
        message: Component
        //?}
    ) =
    //? if = 1.8.9-forge {
            /*run {
                // White Private Messages
                val unformattedMessage = message.unformattedText.removeFormatting()

                if (HyModConfig.whitePrivateMessagesEnabled
                    && HypixelPackets.onHypixel
                    && (unformattedMessage.startsWith("From ")
                            || unformattedMessage.startsWith("To "))
                ) {
                    val n = message.siblings.size
                    if (n < 1) return@run message

                    val newMessage = ChatComponentText(
                        if (unformattedMessage.startsWith("From ")) "From " else "To "
                    )
                    newMessage.chatStyle.color = EnumChatFormatting.LIGHT_PURPLE

                    var colonPassed = false

                    for (i in 0 until n - 1) {
                        val siblingComponent = message.siblings[i]
                        val sibling = siblingComponent.unformattedText

                        if (!colonPassed && sibling.contains(":")) {
                            val colonIndex = sibling.indexOf(":")

                            if (colonIndex > 0) {
                                val beforeColon =
                                    ChatComponentText(sibling.substring(0, colonIndex))
                                beforeColon.chatStyle =
                                    siblingComponent.chatStyle.createShallowCopy()
                                newMessage.appendSibling(beforeColon)
                            }

                            val colon = ChatComponentText(":")
                            colon.chatStyle.color = EnumChatFormatting.WHITE
                            newMessage.appendSibling(colon)

                            if (colonIndex + 1 <= sibling.length - 1) {
                                val afterColon =
                                    ChatComponentText(sibling.substring(colonIndex + 1))
                                afterColon.chatStyle =
                                    siblingComponent.chatStyle.createShallowCopy()
                                newMessage.appendSibling(afterColon)
                            }

                            colonPassed = true
                            continue
                        }

                        newMessage.appendSibling(siblingComponent.createCopy())
                    }

                    if (!colonPassed) return@run message

                    val lastSibling = message.siblings[n - 1].createCopy()
                    lastSibling.chatStyle.color = EnumChatFormatting.WHITE
                    newMessage.appendSibling(lastSibling)

                    return@run newMessage
                }

                // White No Rank Messages
                val formattedMessage = message.formattedText

                if (HyModConfig.whiteNoRankMessagesEnabled
                    && HypixelPackets.onHypixel
                    && formattedMessage.contains("§7: ")
                ) {
                    return@run ChatComponentText(
                        formattedMessage.replace("§7: ", "§f: ")
                    )
                }

                message
            }
        *///?} else {
        run {
            // White Private Messages
            val unformattedMessage = message.string.removeFormatting()

            if (HyModConfig.whitePrivateMessagesEnabled
                && HypixelPackets.onHypixel
                && (unformattedMessage.startsWith("From ")
                        || unformattedMessage.startsWith("To "))
            ) {
                val n = message.siblings.size
                if (n < 1) return@run message

                val newMessage: MutableComponent =
                    message.plainCopy().withStyle(ChatFormatting.LIGHT_PURPLE)

                var colonPassed = false

                for (i in 0 until n - 1) {
                    val siblingComponent = message.siblings[i]
                    val sibling = siblingComponent.string

                    if (!colonPassed && sibling.contains(":")) {
                        val colonIndex = sibling.indexOf(":")

                        if (colonIndex > 0) {
                            newMessage.append(
                                Component.literal(
                                    sibling.substring(0, colonIndex)
                                ).setStyle(siblingComponent.style)
                            )
                        }

                        newMessage.append(
                            Component.literal(":")
                                .withStyle(ChatFormatting.WHITE)
                        )

                        if (colonIndex + 1 <= sibling.length - 1) {
                            newMessage.append(
                                Component.literal(
                                    sibling.substring(colonIndex + 1)
                                ).setStyle(siblingComponent.style)
                            )
                        }

                        colonPassed = true
                        continue
                    }

                    newMessage.append(siblingComponent)
                }

                if (!colonPassed) return@run message

                newMessage.append(
                    message.siblings[n - 1]
                        .copy()
                        .withStyle(ChatFormatting.WHITE)
                )

                return@run newMessage
            }

            // White No Rank Messages
            val formattedMessage = message.string

            if (HyModConfig.whiteNoRankMessagesEnabled
                && HypixelPackets.onHypixel
                && formattedMessage.contains("§7: ")
            ) {
                return@run Component.nullToEmpty(
                    formattedMessage.replace("§7: ", "§f: ")
                )
            }

            message
        }
    //?}
}