package tomeko.hymod.commands

import net.minecraft.client.Minecraft
//? if = 1.8.9 {
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
//?} else {
/*import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.commands.CommandBuildContext
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import org.polyfrost.oneconfig.utils.v1.dsl.openUI
//? if >= 26.1 {
/*import net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal
*///?} else {
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
//?}
*///?}
import tomeko.hymod.config.HyModConfig
import tomeko.hymod.utils.Constants

object HyModCommand
//? if = 1.8.9 {
    : CommandBase()
//?}
{
    private var shouldOpenConfig: Boolean = false

    fun register() {
        //? if = 1.8.9 {
        ClientCommandHandler.instance.registerCommand(this)
        //?} else {
        /*ClientCommandRegistrationCallback.EVENT.register { dispatcher: CommandDispatcher<FabricClientCommandSource>, _: CommandBuildContext ->
            dispatcher.register(
                literal(Constants.MOD_ID)
                    .executes { _: CommandContext<FabricClientCommandSource> ->
                        handle()
                        return@executes 1
                    }
            )
        }

        ClientTickEvents.END_CLIENT_TICK.register(HyModCommand::onTick)
        *///?}
    }

    //? if = 1.8.9 {
    override fun getCommandName(): String = Constants.MOD_ID

    override fun getCommandUsage(sender: ICommandSender): String =
        "/${Constants.MOD_ID} <all/party>"

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        handle()
    }

    override fun getRequiredPermissionLevel(): Int = 0
    //?}

    fun handle() {
        shouldOpenConfig = true
    }

    //? if = 1.8.9 {
    @SubscribeEvent
    //?}
    fun onTick(
        //? if = 1.8.9 {
        event: TickEvent.ClientTickEvent
        //?} else {
        /*mc: Minecraft
        *///?}
    ) {
        //? if = 1.8.9 {
        if (event.phase != TickEvent.Phase.END) return
        //?}

        if (!shouldOpenConfig) return

        //? if = 1.8.9 {
        HyModConfig.openGui()
        //?} else {
        /*HyModConfig.openUI()
        *///?}
        shouldOpenConfig = false
    }
}