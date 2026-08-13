package tomeko.hymod.chat

//? if = 1.8.9 {

/*import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
*///?} else {

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.network.chat.Component
//?}

import tomeko.hymod.config.HyModConfig
import tomeko.hymod.utils.removeFormatting

object HideGuildMOTD {
    private var guildMOTD = false

    fun register() {
        //? if = 1.8.9 {
        /*MinecraftForge.EVENT_BUS.register(this)
        *///?} else {
        ClientReceiveMessageEvents.ALLOW_GAME.register(HideGuildMOTD::onChatReceive)
        //?}
    }

    //? if = 1.8.9 {
    /*@SubscribeEvent
*///?}
    fun onChatReceive(
        //? if = 1.8.9 {
        /*event: ClientChatReceivedEvent
        *///?} else {
        message: Component, fromActionBar: Boolean
        //?}
    )
//? if >= 1.21.11 {
            : Boolean
    //?}
    {
        //? if = 1.8.9 {
        /*if (event.type.toInt() == 2 || event.message == null) return

        if (shouldCancel(event.message.unformattedText)) {
            event.isCanceled = true
        }
        *///?} else {
        if (fromActionBar) return true

        return !shouldCancel(message.string)
        //?}
    }

    private fun shouldCancel(message: String): Boolean {
        if (!HyModConfig.hideGuildMOTDEnabled) return false

        val cleanMessage = message.removeFormatting()

        if (cleanMessage.startsWith("--------------  Guild: Message Of The Day  --------------")) {
            guildMOTD = true
        }

        if (guildMOTD) {
            if (cleanMessage.endsWith("-----------------------------------------------------")) {
                guildMOTD = false
            }
            return true
        }

        return false
    }
}