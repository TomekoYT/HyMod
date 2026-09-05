package tomeko.hymod

//? if = 1.8.9-forge {
/*import cc.polyfrost.oneconfig.events.EventManager
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
*///?} else {
import net.fabricmc.api.ClientModInitializer
//?}

import tomeko.hymod.chat.*
import tomeko.hymod.commands.*
import tomeko.hymod.config.*
import tomeko.hymod.hud.*
import tomeko.hymod.location.*
import tomeko.hymod.stats.*
import tomeko.hymod.utils.*

//? if = 1.8.9-forge {
/*@Mod(
    modid = Constants.MOD_ID,
    name = Constants.MOD_NAME,
    version = Constants.MOD_VERSION,
    modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter",
    dependencies = "required-after:hypixel_mod_api"
)
*///?}
class HyMod
//? if >= 1.21.11-fabric {
    : ClientModInitializer
//?}
{
    //? if = 1.8.9-forge {
    /*@Mod.EventHandler
    *///?} else {
    override
    //?}
    fun onInitializeClient(
        //? if = 1.8.9-forge {
        /*event: FMLInitializationEvent
        *///?}
    ) {
        //? if = 1.8.9-forge {
        /*EventManager.INSTANCE.register(this)
        *///?}

        CoordsWaypoints.register()
        DangerousTauntWaypoint.register()
        HideGuildMOTD.register()
        //? if >= 1.21.11-fabric {
        MVPEmoji.register()
        //?}
        WhiteChatMessages.register()

        HyModCommand.register()
        SendCoordsCommand.register()

        HyModConfig.register()

        //? if >= 1.21.11-fabric {
        BedwarsResourceDisplay.register()
        //?}

        HypixelPackets.register()

        NametagStats.register()

        ItemTracker.register()
        WaypointRenderer.register()

        Debug.forceLog("Initialized!")
    }
}