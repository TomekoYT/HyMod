package tomeko.hymod;

import cc.polyfrost.oneconfig.events.EventManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import tomeko.hymod.config.HyModConfig;
import tomeko.hymod.config.CloseInactiveConfigScreen;
import tomeko.hymod.gui.ItemTracker;
import tomeko.hymod.utils.Constants;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter")
public class HyMod {
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        EventManager.INSTANCE.register(this);

        new HyModConfig();

        CloseInactiveConfigScreen.register();

        ItemTracker.register();
    }
}
