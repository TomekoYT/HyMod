package tomeko.hymod;

import cc.polyfrost.oneconfig.events.EventManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import tomeko.hymod.config.*;
import tomeko.hymod.gui.*;
import tomeko.hymod.utils.*;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter", dependencies = "required-after:hypixel_mod_api")
public class HyMod {
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        EventManager.INSTANCE.register(this);

        new HyModConfig();

        CloseInactiveConfigScreen.register();

        ItemTracker.register();

        HypixelPackets.register();
    }
}
