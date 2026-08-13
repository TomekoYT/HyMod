package tomeko.hymod.config

//? if = 1.8.9 {
/*import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.*
import cc.polyfrost.oneconfig.config.core.OneColor
import cc.polyfrost.oneconfig.config.data.InfoType
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
*///?} else {
import org.polyfrost.compose.render.PolyColor
import org.polyfrost.oneconfig.api.config.v1.Config
import org.polyfrost.oneconfig.api.config.v1.annotations.*
//?}
//? if = 1.8.9 {
//import tomeko.hymod.bedwars.BedwarsResourceDisplay
//?}
import tomeko.hymod.utils.Constants

object HyModConfig : Config(
    //? if = 1.8.9 {
    /*Mod(
        Constants.MOD_NAME,
        ModType.HYPIXEL,
        Constants.MOD_ICON
    ),
    "${Constants.MOD_ID}.json"
    *///?} else {
    "${Constants.MOD_ID}.json",
    Constants.MOD_ICON,
    Constants.MOD_NAME,
    Category.HYPIXEL
    //?}
) {
    //? if >= 1.21.11 {
    val DEPENDENCIES: List<Pair<String, List<String>>> = listOf(
        "coordsWaypointsEnabled" to listOf(
            "coordsWaypointsBoxColor",
            "coordsWaypointsBeamColor",
            "coordsWaypointsRenderOwner",
            "coordsWaypointsOwnerColor",
            "coordsWaypointsRenderText",
            "coordsWaypointsTextColor",
            "coordsWaypointsRenderDistance",
            "coordsWaypointsDistanceTextColor",
            "coordsWaypointsTime"
        )
    )
    //?}

    fun register() {
        //? if = 1.8.9 {
        /*initialize()
        *///?} else {
        preload()
        for ((condition, dependencies) in DEPENDENCIES) {
            for (dependency in dependencies) {
                addDependency(dependency, condition)
            }
        }
        //?}
    }

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    private const val CATEGORY_BEDWARS = "Bedwars"

    //? if = 1.8.9 {
    //@Exclude
    //?}
    private const val SUBCATEGORY_SHOP = "Shop"

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Use Middle Click in Shop",
        description = "Replace middle click with left click in bedwars item shop",
        category = CATEGORY_BEDWARS,
        subcategory = SUBCATEGORY_SHOP
    )
    var middleClickInBedwarsShop = true


    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    private const val SUBCATEGORY_BEDWARS_RESOURCE_DISPLAY = "Resource Display"

    //? if = 1.8.9 {
    /*@HUD(
        name = SUBCATEGORY_BEDWARS_RESOURCE_DISPLAY,
        category = CATEGORY_BEDWARS
    )
    var bedwarsResourceDisplay = BedwarsResourceDisplay()
    *///?} else {
    @Info(
        title = "Resource Display can be edited by clicking Edit HUD in the top left corner",
        category = CATEGORY_BEDWARS,
        subcategory = SUBCATEGORY_BEDWARS_RESOURCE_DISPLAY
    )
    var bedwarsResourceDisplayInfo: Nothing? = null
    //?}

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    private const val CATEGORY_ARCADE = "Arcade"

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    private const val SUBCATEGORY_FARM_HUNT = "Farm Hunt"

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Dangerous Taunt Waypoint",
        description = "Show waypoint on dangerous taunt message in Farm Hunt",
        category = CATEGORY_ARCADE,
        subcategory = SUBCATEGORY_FARM_HUNT
    )
    var dangerousTauntWaypointEnabled = true


    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    private const val CATEGORY_CHAT = "Chat"

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    private const val SUBCATEGORY_WHITE_CHAT_MESSAGES = "White Chat Messages"

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "White Private Messages",
        description = "Color private messages white instead of gray on Hypixel",
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_WHITE_CHAT_MESSAGES
    )
    var whitePrivateMessagesEnabled = true

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "White No Rank Messages",
        description = "Color messages from players with no rank white instead of gray on Hypixel",
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_WHITE_CHAT_MESSAGES
    )
    var whiteNoRankMessagesEnabled = true

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    private const val SUBCATEGORY_HIDE_GUILD_MOTD = "Hide Guild MOTD"

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Hide Guild MOTD",
        description = "Hide guild message of the day on Hypixel",
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_HIDE_GUILD_MOTD
    )
    var hideGuildMOTDEnabled = false

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    private const val SUBCATEGORY_MVP_EMOJIS = "MVP++ Emojis"

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "MVP++ Emojis",
        description = "Replace emojis like <3 with ❤ on Hypixel",
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_MVP_EMOJIS
    )
    var mvpEmojisEnabled = true

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    private const val SUBCATEGORY_SENDCOORDS_COMMAND = "/sendcoords Command"

    @Dropdown(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Default Mode",
        description = "Set default /sendcoords command mode",
        options = [
            "All",
            "Party",
            "Guild"
        ],
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_SENDCOORDS_COMMAND
    )
    var sendcoordsMode = 1

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    private const val SUBCATEGORY_COORDS_WAYPOINTS = "Coords Waypoints"

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Coords Waypoints",
        description = "Show waypoint on coords from chat",
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    var coordsWaypointsEnabled = true

    @Color(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Box Color",
        //? if = 1.8.9 {
        /*allowAlpha = true,
        *///?}
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    var coordsWaypointsBoxColor =
    //? if = 1.8.9 {
            /*OneColor(
            *///?} else {
        PolyColor(
            //?}
            0xFFFFFFFF.toInt()
        )

    @Color(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Beam Color",
        //? if = 1.8.9 {
        /*allowAlpha = true,
        *///?}
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    var coordsWaypointsBeamColor =
    //? if = 1.8.9 {
            /*OneColor(
            *///?} else {
        PolyColor(
            //?}
            0xC0FFFFFF.toInt()
        )

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Render Owner",
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    var coordsWaypointsRenderOwner = true

    @Color(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Owner Color",
        //? if = 1.8.9 {
        /*allowAlpha = false,
        *///?} else {
        alpha = false,
        //?}
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    var coordsWaypointsOwnerColor =
    //? if = 1.8.9 {
            /*OneColor(
            *///?} else {
        PolyColor(
            //?}
            0xFFFFFFFF.toInt()
        )

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Render Text",
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    var coordsWaypointsRenderText = true

    @Color(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Text Color",
        //? if = 1.8.9 {
        /*allowAlpha = false,
        *///?} else {
        alpha = false,
        //?}
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    var coordsWaypointsTextColor =
    //? if = 1.8.9 {
            /*OneColor(
            *///?} else {
        PolyColor(
            //?}
            0xFFFFFFFF.toInt()
        )

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Render Distance",
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    var coordsWaypointsRenderDistance = true

    @Color(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Distance Text Color",
        //? if = 1.8.9 {
        /*allowAlpha = false,
        *///?} else {
        alpha = false,
        //?}
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    var coordsWaypointsDistanceTextColor =
    //? if = 1.8.9 {
            /*OneColor(
            *///?} else {
        PolyColor(
            //?}
            0xFFFFFF00.toInt()
        )

    @Slider(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Time",
        min = 0f,
        max = 120f,
        step =
            //? if = 1.8.9{
            /*1
        *///?} else {
            1f
        //?}
        ,
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    var coordsWaypointsTime = 60

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    private const val CATEGORY_LOBBY = "Lobby"

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    private const val SUBCATEGORY_MIDDLE_CLICK_GUI_ITEMS = "Middle Click GUI Items"

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Middle Click GUI Items",
        description = "Replace left click with middle click in GUIs in Hypixel lobbies",
        category = CATEGORY_LOBBY,
        subcategory = SUBCATEGORY_MIDDLE_CLICK_GUI_ITEMS
    )
    var middleClickInLobby = true

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    private const val CATEGORY_DEBUG = "Debug"

    @Info(
        //? if = 1.8.9 {
        /*text
            *///?} else {
        title
            //?}
        = "Probably should stay disabled",
        //? if = 1.8.9 {
        /*type = InfoType.WARNING,
        *///?}
        category = CATEGORY_DEBUG
    )
    var debugModeInfo: Nothing? = null

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Debug Mode",
        category = CATEGORY_DEBUG
    )
    var debugModeEnabled = false
}