package tomeko.hymod.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Header;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import tomeko.hymod.utils.Constants;

public class HyModConfig extends Config {
    public static final HyModConfig INSTANCE = new HyModConfig();

    public HyModConfig() {
        super(new Mod(Constants.MOD_NAME, ModType.HYPIXEL, "/assets/" + Constants.MOD_ID + "/icon.png"), Constants.MOD_ID + ".json");
        initialize();
    }

    private static final String BEDWARS_CATEGORY = "Bedwars";

    @Header(
            text = "Bedwars Resource Display",
            category = BEDWARS_CATEGORY
    )
    private static boolean bedwarsResourceDisplayHeader;

    @Switch(
            name = "Enabled",
            category = BEDWARS_CATEGORY
    )
    public static boolean bedwarsResourceDisplayEnabled = true;
}
