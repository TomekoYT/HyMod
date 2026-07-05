package tomeko.hymod.config

//? if = 1.8.9 {

/*import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.*
import cc.polyfrost.oneconfig.config.core.OneColor
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import tomeko.hymod.hud.*
*///?} else {
import org.polyfrost.compose.render.PolyColor
import org.polyfrost.oneconfig.api.config.v1.Config
import org.polyfrost.oneconfig.api.config.v1.annotations.Color
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch
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
    //? if >= 26.1 {
    val DEPENDENCIES: List<Pair<String, List<String>>> = listOf(
        "bedwarsResourceDisplayEnabled" to listOf(
            "bedwarsResourceDisplayShowIron",
            "bedwarsResourceDisplayShowGold",
            "bedwarsResourceDisplayShowDiamond",
            "bedwarsResourceDisplayShowEmerald",
            "bedwarsResourceDisplayShowInventory",
            "bedwarsResourceDisplayShowEnderChest",
            "bedwarsResourceDisplayShowTotal"
        ),
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
    const val CATEGORY_BEDWARS = "Bedwars"

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    const val SUBCATEGORY_BEDWARS_RESOURCE_DISPLAY = "Bedwars Resource Display"

    //? if >= 26.1 {
    @Switch(
        title = "Enabled",
        category = CATEGORY_BEDWARS,
        subcategory = SUBCATEGORY_BEDWARS_RESOURCE_DISPLAY
    )
    var bedwarsResourceDisplayEnabled = true
    //?}

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Show Iron",
        category = CATEGORY_BEDWARS,
        subcategory = SUBCATEGORY_BEDWARS_RESOURCE_DISPLAY
    )
    var bedwarsResourceDisplayShowIron = true

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Show Gold",
        category = CATEGORY_BEDWARS,
        subcategory = SUBCATEGORY_BEDWARS_RESOURCE_DISPLAY
    )
    var bedwarsResourceDisplayShowGold = true

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Show Diamond",
        category = CATEGORY_BEDWARS,
        subcategory = SUBCATEGORY_BEDWARS_RESOURCE_DISPLAY
    )
    var bedwarsResourceDisplayShowDiamond = true

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Show Emerald",
        category = CATEGORY_BEDWARS,
        subcategory = SUBCATEGORY_BEDWARS_RESOURCE_DISPLAY
    )
    var bedwarsResourceDisplayShowEmerald = true

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Show Inventory",
        category = CATEGORY_BEDWARS,
        subcategory = SUBCATEGORY_BEDWARS_RESOURCE_DISPLAY
    )
    var bedwarsResourceDisplayShowInventory = true

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Show Ender Chest",
        category = CATEGORY_BEDWARS,
        subcategory = SUBCATEGORY_BEDWARS_RESOURCE_DISPLAY
    )
    var bedwarsResourceDisplayShowEnderChest = true

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Show Total",
        category = CATEGORY_BEDWARS,
        subcategory = SUBCATEGORY_BEDWARS_RESOURCE_DISPLAY
    )
    var bedwarsResourceDisplayShowTotal = true

    //? if = 1.8.9 {
    /*@HUD(
        name = "Bedwars Resource Display HUD",
        category = CATEGORY_BEDWARS
    )
    var bedwarsResourceDisplay = BedwarsResourceDisplay()
    *///?}

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    const val CATEGORY_ARCADE = "Arcade"

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    const val SUBCATEGORY_FARM_HUNT = "Farm Hunt"

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Dangerous Taunt Waypoint",
        category = CATEGORY_ARCADE,
        subcategory = SUBCATEGORY_FARM_HUNT
    )
    var dangerousTauntWaypointEnabled = true


    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    const val CATEGORY_CHAT = "Chat"

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    const val SUBCATEGORY_WHITE_CHAT_MESSAGES = "White Chat Messages"

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "White Private Messages",
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
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_WHITE_CHAT_MESSAGES
    )
    var whiteNoRankMessagesEnabled = true

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    const val SUBCATEGORY_HIDE_GUILD_MOTD = "Hide Guild MOTD"

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Enabled",
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_HIDE_GUILD_MOTD
    )
    var hideGuildMOTDEnabled = false

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    const val SUBCATEGORY_MVP_EMOJIS = "MVP++ Emojis"

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Enabled",
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_MVP_EMOJIS
    )
    var mvpEmojisEnabled = true

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    const val SUBCATEGORY_SENDCOORDS_COMMAND = "/sendcoords Command"

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Use party as default",
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_SENDCOORDS_COMMAND
    )
    var sendcoordsToParty = true

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    const val SUBCATEGORY_COORDS_WAYPOINTS = "Coords Waypoints"

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Enabled",
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
    var coordsWaypointsTime = 30

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    const val CATEGORY_GUI = "GUI"

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    const val SUBCATEGORY_MIDDLE_CLICK_GUI_ITEMS = "Middle Click GUI Items"

    @JvmField
    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Enabled",
        category = CATEGORY_GUI,
        subcategory = SUBCATEGORY_MIDDLE_CLICK_GUI_ITEMS
    )
    var middleClickGUIItemsEnabled = true


    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    const val CATEGORY_DEBUG = "Debug"

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
        title
            //?}
        = "Debug Mode Enabled",
        category = CATEGORY_DEBUG
    )
    var debugModeEnabled = false
}