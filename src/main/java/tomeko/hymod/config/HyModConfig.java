package tomeko.hymod.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
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
    private static final String CATEGORY_ARCADE = "Arcade";
    @Exclude
    private static final String SUBCATEGORY_FARM_HUNT = "Farm Hunt";

    @Exclude
    private static final String CATEGORY_CHAT = "Chat";
    @Exclude
    private static final String SUBCATEGORY_WHITE_CHAT_MESSAGES = "White Chat Messages";
    @Exclude
    private static final String SUBCATEGORY_HIDE_GUILD_MOTD = "Hide Guild MOTD";
    @Exclude
    private static final String SUBCATEGORY_MVP_EMOJIS = "MVP++ Emojis";
    @Exclude
    private static final String SUBCATEGORY_SENDCOORDS_COMMAND = "/sendcoords Command";
    @Exclude
    private static final String SUBCATEGORY_COORDS_WAYPOINTS = "Coords Waypoints";

    @Exclude
    private static final String CATEGORY_GUI = "GUI";
    @Exclude
    private static final String SUBCATEGORY_MIDDLE_CLICK_GUI_ITEMS = "Middle Click GUI Items";

    @HUD(
            name = "Bedwars Resource Display",
            category = CATEGORY_BEDWARS
    )
    public BedwarsResourceDisplay bedwarsResourceDisplay = new BedwarsResourceDisplay();

    @Switch(
            name = "Dangerous Taunt Waypoint",
            category = CATEGORY_ARCADE,
            subcategory = SUBCATEGORY_FARM_HUNT
    )
    public static boolean dangerousTauntWaypointEnabled = true;

    @Switch(
            name = "White Private Messages",
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_WHITE_CHAT_MESSAGES
    )
    public static boolean whitePrivateMessagesEnabled = true;

    @Switch(
            name = "White No Rank Messages",
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_WHITE_CHAT_MESSAGES
    )
    public static boolean whiteNoRankMessagesEnabled = true;

    @Switch(
            name = "Enabled",
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_HIDE_GUILD_MOTD
    )
    public static boolean hideGuildMOTDEnabled = false;

    @Switch(
            name = "Enabled",
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_MVP_EMOJIS
    )
    public static boolean mvpEmojisEnabled = true;

    @DualOption(
            name = "Default /sendcoords mode",
            left = "All",
            right = "Party",
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_SENDCOORDS_COMMAND
    )
    public static boolean sendcoordsMode = true;

    @Switch(
            name = "Enabed",
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    public static boolean coordsWaypointsEnabled = true;

    @Color(
            name = "Color",
            allowAlpha = false,
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    public static OneColor coordsWaypointsColor = new OneColor(255, 255, 255);

    @Slider(
            name = "Box Opacity",
            min = 0,
            max = 100,
            step = 1,
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    public static int coordsWaypointsBoxOpacity = 50;

    @Slider(
            name = "Beam Opacity",
            min = 0,
            max = 100,
            step = 1,
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    public static int coordsWaypointsBeamOpacity = 50;

    @Switch(
            name = "Render Text",
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    public static boolean coordsWaypointsRenderText = true;

    @Switch(
            name = "Render Distance",
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    public static boolean coordsWaypointsRenderDistance = true;

    @Slider(
            name = "Time",
            min = 0,
            max = 120,
            step = 1,
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    public static int coordsWaypointsTime = 30;

    @Switch(
            name = "Enabled",
            category = CATEGORY_GUI,
            subcategory = SUBCATEGORY_MIDDLE_CLICK_GUI_ITEMS
    )
    public static boolean middleClickGUIItemsEnabled = true;
}
