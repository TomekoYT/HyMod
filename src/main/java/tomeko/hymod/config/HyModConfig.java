package tomeko.hymod.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Exclude;
import cc.polyfrost.oneconfig.config.annotations.HUD;
import cc.polyfrost.oneconfig.config.annotations.Header;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import tomeko.hymod.hud.BedwarsResourceDisplay;
import tomeko.hymod.utils.Constants;

public class HyModConfig extends Config {
    public HyModConfig() {
        super(new Mod(Constants.MOD_NAME, ModType.HYPIXEL, "/assets/" + Constants.MOD_ID + "/icon.png"), Constants.MOD_ID + ".json");
        initialize();
    }

    @Exclude
    private static final String CATEGORY_BEDWARS = "Bedwars";
    @Exclude
    private static final String CATEGORY_GUI = "GUI";

    @HUD(
            name = "Bedwars Resource Display",
            category = CATEGORY_BEDWARS
    )
    public BedwarsResourceDisplay bedwarsResourceDisplay = new BedwarsResourceDisplay();

    @Header(
            text = "Middle Click GUI Items",
            category = CATEGORY_GUI
    )
    private boolean middleClickGUIItemsHeader;

    @Switch(
            name = "Enabled",
            category = CATEGORY_GUI
    )
    public static boolean middleClickGUIItemsEnabled = true;
}
