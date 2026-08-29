package tomeko.hymod.commands

//? if = 1.8.9 {
/*import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraftforge.client.ClientCommandHandler
*///?} else {
import com.mojang.brigadier.arguments.StringArgumentType
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
//? if >= 26.1 {
import net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal
//?} else {
/*import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
*///?}
//?}
import tomeko.hymod.config.HyModConfig
import tomeko.hymod.utils.HypixelPackets

object SendCoordsCommand
//? if = 1.8.9 {
/*: CommandBase()
*///?}
{
    private const val COMMAND_NAME = "sendcoords"

    fun register() {
        //? if = 1.8.9 {
        /*ClientCommandHandler.instance.registerCommand(this)
        *///?} else {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                literal(COMMAND_NAME)
                    .executes {
                        sendCoords("")
                        1
                    }
                    .then(
                        argument("message", StringArgumentType.greedyString())
                            .executes { ctx ->
                                sendCoords(ctx.getArgument("message", String::class.java))
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
        "/$COMMAND_NAME <all/party/guild>"

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
    sendCoords(args.joinToString(" "))
}

    override fun getRequiredPermissionLevel(): Int = 0
    *///?}

    private fun sendCoords(args: String) {
        val first = args.substringBefore(" ")
        val text =
            if (isMode(first)) args.substringAfter(" ", "")
            else args

        val prefix = convertToPrefix(
            if (isMode(first)) first
            else convertToMode(HyModConfig.sendcoordsMode)
        )

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
        if (!text.isEmpty()) message += " | $text"

        if (HypixelPackets.onHypixel) {
            message = "$prefix $message"
        }

        //? if = 1.8.9 {
        /*player.sendChatMessage(message)
        *///?} else {
        player.connection.sendChat(message)
        //?}
    }

    private fun isMode(string: String): Boolean {
        return string == "all" || string == "party" || string == "guild"
    }

    private fun convertToMode(dropdown: Int) : String = when(dropdown) {
        1 -> "party"
        2 -> "guild"
        else -> "all"
    }

    private fun convertToPrefix(mode: String): String = when (mode) {
        "party" -> "/pc"
        "guild" -> "/gc"
        else -> "/ac"
    }
}