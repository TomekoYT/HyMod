package tomeko.hymod;

import cc.polyfrost.oneconfig.events.EventManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import tomeko.hymod.config.HyModConfig;
import tomeko.hymod.gui.CloseInactiveConfigScreen;
import tomeko.hymod.utils.Constants;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter")
public class HyMod {
    public static HyModConfig config;

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        config = new HyModConfig();
        EventManager.INSTANCE.register(this);

        CloseInactiveConfigScreen.register();
    }
}
