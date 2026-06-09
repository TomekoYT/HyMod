package tomeko.hymod.config;

//? if = 1.8.9 {

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
//?} else {

/*import org.polyfrost.compose.render.PolyColor;
import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.annotations.Color;
import org.polyfrost.oneconfig.api.config.v1.annotations.Dropdown;
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider;
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch;
*///?}
import tomeko.hymod.hud.BedwarsResourceDisplay;
import tomeko.hymod.utils.Constants;

public class HyModConfig extends Config {
    public static final HyModConfig INSTANCE = new HyModConfig();

    public HyModConfig() {
        //? if = 1.8.9 {
        super(new Mod(Constants.MOD_NAME, ModType.HYPIXEL, "/assets/" + Constants.MOD_ID + "/icon.png"), Constants.MOD_ID + ".json");
        initialize();
        //?} else {
        /*super(Constants.MOD_ID + ".json", "/assets/" + Constants.MOD_ID + "/icon.png", Constants.MOD_NAME, Category.HYPIXEL);
        save();
        *///?}
    }

    //? if = 1.8.9 {
    @Exclude
            //?}
    public static final String CATEGORY_BEDWARS = "Bedwars";


    //? if = 1.8.9 {
    @HUD(
            name = "Bedwars Resource Display",
            category = CATEGORY_BEDWARS
    )
            //?}
    public BedwarsResourceDisplay bedwarsResourceDisplay = new BedwarsResourceDisplay();


    //? if = 1.8.9 {
    @Exclude
            //?}
    public static final String CATEGORY_ARCADE = "Arcade";


    //? if = 1.8.9 {
    @Exclude
            //?}
    public static final String SUBCATEGORY_FARM_HUNT = "Farm Hunt";

    @Switch(
            //? if = 1.8.9 {
            name
                    //?} else {
                    /*title
                     *///?}
                    = "Dangerous Taunt Waypoint",
            category = CATEGORY_ARCADE,
            subcategory = SUBCATEGORY_FARM_HUNT
    )
    public static boolean dangerousTauntWaypointEnabled = true;


    //? if = 1.8.9 {
    @Exclude
            //?}
    public static final String CATEGORY_CHAT = "Chat";


    //? if = 1.8.9 {
    @Exclude
            //?}
    public static final String SUBCATEGORY_WHITE_CHAT_MESSAGES = "White Chat Messages";


    @Switch(
            //? if = 1.8.9 {
            name
                    //?} else {
                    /*title
                     *///?}
                    = "White Private Messages",
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_WHITE_CHAT_MESSAGES
    )
    public static boolean whitePrivateMessagesEnabled = true;

    @Switch(
            //? if = 1.8.9 {
            name
                    //?} else {
                    /*title
                     *///?}
                    = "White No Rank Messages",
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_WHITE_CHAT_MESSAGES
    )
    public static boolean whiteNoRankMessagesEnabled = true;


    //? if = 1.8.9 {
    @Exclude
            //?}
    public static final String SUBCATEGORY_HIDE_GUILD_MOTD = "Hide Guild MOTD";

    @Switch(
            //? if = 1.8.9 {
            name
                    //?} else {
                    /*title
                     *///?}
                    = "Enabled",
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_HIDE_GUILD_MOTD
    )
    public static boolean hideGuildMOTDEnabled = false;


    //? if = 1.8.9 {
    @Exclude
            //?}
    public static final String SUBCATEGORY_MVP_EMOJIS = "MVP++ Emojis";

    @Switch(
            //? if = 1.8.9 {
            name
                    //?} else {
                    /*title
                     *///?}
                    = "Enabled",
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_MVP_EMOJIS
    )
    public static boolean mvpEmojisEnabled = true;


    //? if = 1.8.9 {
    @Exclude
            //?}
    public static final String SUBCATEGORY_SENDCOORDS_COMMAND = "/sendcoords Command";

    @Dropdown(
            //? if = 1.8.9 {
            name
                    //?} else {
                    /*title
                     *///?}
                    = "Default /sendcoords mode",
            options = {"All", "Party"},
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_SENDCOORDS_COMMAND
    )
    public static int sendcoordsToParty = 1;


    //? if = 1.8.9 {
    @Exclude
            //?}
    public static final String SUBCATEGORY_COORDS_WAYPOINTS = "Coords Waypoints";

    @Switch(
            //? if = 1.8.9 {
            name
                    //?} else {
                    /*title
                     *///?}
                    = "Enabled",
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    public static boolean coordsWaypointsEnabled = true;

    @Color(
            //? if = 1.8.9 {
            name
                    //?} else {
                    /*title
                     *///?}
                    = "Color",
            //? if = 1.8.9 {
            allowAlpha = false,
            //?}
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
            //? if = 1.8.9 {
    public static OneColor coordsWaypointsColor = new OneColor(255, 255, 255);
    //?} else {
    /*public static PolyColor coordsWaypointsColor = PolyColor.Companion.getWHITE();
     *///?}

    @Slider(
            //? if = 1.8.9 {
            name
                    //?} else {
                    /*title
                     *///?}
                    = "Box Opacity",
            min = 0,
            max = 100,
            step = 1,
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    public static int coordsWaypointsBoxOpacity = 50;

    @Slider(
            //? if = 1.8.9 {
            name
                    //?} else {
                    /*title
                     *///?}
                    = "Beam Opacity",
            min = 0,
            max = 100,
            step = 1,
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    public static int coordsWaypointsBeamOpacity = 50;

    @Switch(
            //? if = 1.8.9 {
            name
                    //?} else {
                    /*title
                     *///?}
                    = "Render Text",
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    public static boolean coordsWaypointsRenderText = true;

    @Switch(
            //? if = 1.8.9 {
            name
                    //?} else {
                    /*title
                     *///?}
                    = "Render Distance",
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    public static boolean coordsWaypointsRenderDistance = true;

    @Slider(
            //? if = 1.8.9 {
            name
                    //?} else {
                    /*title
                     *///?}
                    = "Time",
            min = 0,
            max = 120,
            step = 1,
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    public static int coordsWaypointsTime = 30;


    //? if = 1.8.9 {
    @Exclude
            //?}
    public static final String CATEGORY_GUI = "GUI";


    //? if = 1.8.9 {
    @Exclude
            //?}
    public static final String SUBCATEGORY_MIDDLE_CLICK_GUI_ITEMS = "Middle Click GUI Items";


    @Switch(
            //? if = 1.8.9 {
            name
                    //?} else {
                    /*title
                     *///?}
                    = "Enabled",
            category = CATEGORY_GUI,
            subcategory = SUBCATEGORY_MIDDLE_CLICK_GUI_ITEMS
    )
    public static boolean middleClickGUIItemsEnabled = true;
}
