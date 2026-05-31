package tomeko.hymod;

//? if = 1.8.9 {
import cc.polyfrost.oneconfig.events.EventManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
//?} else {
/*import net.fabricmc.api.ClientModInitializer;
*///?}
import tomeko.hymod.chat.*;
import tomeko.hymod.commands.*;
import tomeko.hymod.config.*;
import tomeko.hymod.utils.*;

//? if = 1.8.9 {
@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, dependencies = "required-after:hypixel_mod_api")
//?}
public class HyMod
        //? if >= 1.21.9 {
        /*implements ClientModInitializer
        *///?}
{
    //? if = 1.8.9 {
    @Mod.EventHandler
    //?} else {
    /*@Override
            *///?}
    public void onInitializeClient(
            //? if = 1.8.9 {
            FMLInitializationEvent event
            //?}
    ) {
        //? if = 1.8.9 {
        EventManager.INSTANCE.register(this);
        //?}

        CoordsWaypoints.register();
        DangerousTauntWaypoint.register();
        HideGuildMOTD.register();
        //? if >= 1.21.9 {
        /*MVPEmoji.register();
         *///?}
        WhiteChatMessages.register();

        SendCoordsCommand.register();

        //? if = 1.8.9 {
        CloseInactiveConfigScreen.register();
        new HyModConfig();
        //?} else {
        /*HyModConfig.register();
        *///?}

        HypixelPackets.register();
        //? if = 1.8.9 {
        ItemTracker.register();
        //?}
        WaypointRenderer.register();
    }
}
