package tomeko.hymod;

import cc.polyfrost.oneconfig.events.EventManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import tomeko.hymod.chat.*;
import tomeko.hymod.commands.*;
import tomeko.hymod.config.*;
import tomeko.hymod.utils.*;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, dependencies = "required-after:hypixel_mod_api")
public class HyMod {
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        EventManager.INSTANCE.register(this);

        CoordsWaypoints.register();
        DangerousTauntWaypoint.register();
        HideGuildMOTD.register();
        WhiteChatMessages.register();

        SendCoordsCommand.register();

        CloseInactiveConfigScreen.register();
        new HyModConfig();

        HypixelPackets.register();
        ItemTracker.register();
        WaypointRenderer.register();
    }
}
