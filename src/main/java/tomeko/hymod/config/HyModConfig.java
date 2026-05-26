package tomeko.hymod.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Exclude;
import cc.polyfrost.oneconfig.config.annotations.HUD;
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
    @Exclude
    private static final String SUB_CATEGORY_MIDDLE_CLICK_GUI_ITEMS = "Middle Click GUI Items";

    @Exclude
    private static final String CATEGORY_CHAT = "Chat";
    @Exclude
    private static final String SUB_CATEGORY_WHITE_CHAT_MESSAGES = "White Chat Messages";
    @Exclude
    private static final String SUB_CATEGORY_HIDE_GUILD_MOTD = "Hide Guild MOTD";

    @HUD(
            name = "Bedwars Resource Display",
            category = CATEGORY_BEDWARS
    )
    public BedwarsResourceDisplay bedwarsResourceDisplay = new BedwarsResourceDisplay();

    @Switch(
            name = "White Private Messages",
            category = CATEGORY_CHAT,
            subcategory = SUB_CATEGORY_WHITE_CHAT_MESSAGES
    )
    public static boolean whitePrivateMessagesEnabled = true;

    @Switch(
            name = "White No Rank Messages",
            category = CATEGORY_CHAT,
            subcategory = SUB_CATEGORY_WHITE_CHAT_MESSAGES
    )
    public static boolean whiteNoRankMessagesEnabled = true;

    @Switch(
            name = "Enabled",
            category = CATEGORY_CHAT,
            subcategory = SUB_CATEGORY_HIDE_GUILD_MOTD
    )
    public static boolean hideGuildMOTDEnabled = false;

    @Switch(
            name = "Enabled",
            category = CATEGORY_GUI,
            subcategory = SUB_CATEGORY_MIDDLE_CLICK_GUI_ITEMS
    )
    public static boolean middleClickGUIItemsEnabled = true;
}
