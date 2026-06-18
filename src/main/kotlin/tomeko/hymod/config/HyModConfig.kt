package tomeko.hymod.config

//? if = 1.8.9 {

/*import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.*
import cc.polyfrost.oneconfig.config.core.OneColor
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
*///?} else {
import org.polyfrost.compose.render.PolyColor
import org.polyfrost.oneconfig.api.config.v1.Config
import org.polyfrost.oneconfig.api.config.v1.annotations.Color
import org.polyfrost.oneconfig.api.config.v1.annotations.Dropdown
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch
//?}

import tomeko.hymod.hud.BedwarsResourceDisplay
import tomeko.hymod.utils.Constants

object HyModConfig : Config(
    //? if = 1.8.9 {
    /*Mod(
        Constants.MOD_NAME,
        ModType.HYPIXEL,
        "/assets/${Constants.MOD_ID}/icon.png"
    ),
    "${Constants.MOD_ID}.json"
    *///?} else {
    "${Constants.MOD_ID}.json",
    "/assets/${Constants.MOD_ID}/icon.png",
    Constants.MOD_NAME,
    Category.HYPIXEL
    //?}
) {
    fun register() {
        //? if = 1.8.9 {
        /*initialize()
        *///?} else {
        save()
        //?}
    }

    //? if = 1.8.9 {
    /*@Exclude
    *///?}
    const val CATEGORY_BEDWARS = "Bedwars"

    //? if = 1.8.9 {
    /*@HUD(
        name = "Bedwars Resource Display",
        category = CATEGORY_BEDWARS
    )
    var bedwarsResourceDisplay = BedwarsResourceDisplay()
    *///?} else {
    const val SUBCATEGORY_BEDWARS_RESOURCE_DISPLAY = "Bedwars Resource Display"

    @Slider(
        title = "Width",
        min = 0f,
        max = 100f,
        step = 1f,
        category = CATEGORY_BEDWARS,
        subcategory = SUBCATEGORY_BEDWARS_RESOURCE_DISPLAY
    )
    var bedwarsResourceDisplayWidthPercentage = 0

    @Slider(
        title = "Height",
        min = 0f,
        max = 100f,
        step = 1f,
        category = CATEGORY_BEDWARS,
        subcategory = SUBCATEGORY_BEDWARS_RESOURCE_DISPLAY
    )
    var bedwarsResourceDisplayHeightPercentage = 0

    @Slider(
        title = "Scale",
        min = 0f,
        max = 200f,
        step = 1f,
        category = CATEGORY_BEDWARS,
        subcategory = SUBCATEGORY_BEDWARS_RESOURCE_DISPLAY
    )
    var bedwarsResourceDisplayScalePercentage = 100

    @Slider(
        title = "Item Padding",
        min = 0f,
        max = 10f,
        category = CATEGORY_BEDWARS,
        subcategory = SUBCATEGORY_BEDWARS_RESOURCE_DISPLAY
    )
    var bedwarsResourceDisplayItemPadding = 5f

    @Slider(
        title = "Icon Padding",
        min = 0f,
        max = 10f,
        category = CATEGORY_BEDWARS,
        subcategory = SUBCATEGORY_BEDWARS_RESOURCE_DISPLAY
    )
    var bedwarsResourceDisplayIconPadding = 5f

    @Switch(
        title = "Text Shadow",
        category = CATEGORY_BEDWARS,
        subcategory = SUBCATEGORY_BEDWARS_RESOURCE_DISPLAY
    )
    var bedwarsResourceDisplayTextType = false
    //?}

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
        = "Color",
        //? if = 1.8.9 {
        /*allowAlpha = false,
        *///?}
        category = CATEGORY_CHAT,
        subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    //? if = 1.8.9 {
    /*var coordsWaypointsColor = OneColor(255, 255, 255)
    *///?} else {
    var coordsWaypointsColor = PolyColor.WHITE
     //?}

    @Slider(
        //? if = 1.8.9 {
        /*name
            *///?} else {
            title
             //?}
        = "Box Opacity",
        min = 0f,
        max = 100f,
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
    var coordsWaypointsBoxOpacity = 50

    @Slider(
        //? if = 1.8.9 {
        /*name
            *///?} else {
            title
             //?}
        = "Beam Opacity",
        min = 0f,
        max = 100f,
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
    var coordsWaypointsBeamOpacity = 50

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