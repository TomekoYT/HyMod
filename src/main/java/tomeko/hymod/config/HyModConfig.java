package tomeko.hymod.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import tomeko.hymod.utils.Constants;

public class HyModConfig extends Config {
    public HyModConfig() {
        super(new Mod(Constants.MOD_NAME, ModType.HYPIXEL, "/assets/${Constants.MOD_ID}/icon.png"), Constants.MOD_ID + ".json");
        initialize();
    }
}
