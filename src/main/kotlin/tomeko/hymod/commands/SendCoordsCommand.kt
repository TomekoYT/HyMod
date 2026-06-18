package tomeko.hymod.commands

import net.minecraft.client.Minecraft
//? if = 1.8.9 {
/*import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraftforge.client.ClientCommandHandler
*///?} else {
import com.mojang.brigadier.arguments.StringArgumentType
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.client.player.LocalPlayer
import net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal
//?}

import tomeko.hymod.config.HyModConfig
import tomeko.hymod.utils.HypixelPackets

object SendCoordsCommand
//? if = 1.8.9 {
    /*: CommandBase()
*///?}
{
    private const val COMMAND_NAME = "sendcoords"

    private val defaultMode: String
        get() = if (HyModConfig.sendcoordsToParty) "party" else "all"

    fun register() {
        //? if = 1.8.9 {
        /*ClientCommandHandler.instance.registerCommand(this)
        *///?} else {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                literal(COMMAND_NAME)
                    .executes { _ ->
                        sendCoords(defaultMode)
                        1
                    }
                    .then(
                        argument("mode", StringArgumentType.word())
                            .suggests { _, builder ->
                                builder.suggest("all")
                                builder.suggest("party")
                                builder.buildFuture()
                            }
                            .executes { ctx ->
                                val mode = ctx.getArgument("mode", String::class.java)
                                    .lowercase()
                                sendCoords(mode)
                                1
                            }
                    )
            )
        }
        //?}
    }

    //? if = 1.8.9 {
    /*override fun getCommandName(): String = COMMAND_NAME

    override fun getCommandUsage(sender: ICommandSender): String =
        "/$COMMAND_NAME <all/party>"

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        val mode = if (args.isNotEmpty()) args[0] else defaultMode
        sendCoords(mode)
    }

    override fun getRequiredPermissionLevel(): Int = 0
    *///?}

    private fun sendCoords(mode: String) {
        //? if = 1.8.9 {
        /*val player: EntityPlayerSP = Minecraft.getMinecraft().thePlayer
        *///?} else {
        val player: LocalPlayer = Minecraft.getInstance().player!!
         //?}

        val x =
            //? if = 1.8.9 {
            /*player.posX.toInt()
        *///?} else {
        player.x.toInt()
         //?}

        val y =
            //? if = 1.8.9 {
            /*player.posY.toInt()
        *///?} else {
        player.y.toInt()
         //?}

        val z =
            //? if = 1.8.9 {
            /*player.posZ.toInt()
        *///?} else {
        player.z.toInt()
         //?}

        var message = "x: $x, y: $y, z: $z"

        var actualMode = mode
        if (actualMode != "all" && actualMode != "party") {
            actualMode = defaultMode
        }

        if (HypixelPackets.onHypixel) {
            if (actualMode == "all") {
                message = "/ac $message"
            } else if (actualMode == "party") {
                message = "/pc $message"
            }
        }

        //? if = 1.8.9 {
        /*player.sendChatMessage(message)
        *///?} else {
        player.connection.sendChat(message)
         //?}
    }
}