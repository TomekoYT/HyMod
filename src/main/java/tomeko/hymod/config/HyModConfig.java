package tomeko.hymod.config;

//? if = 1.8.9 {

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import tomeko.hymod.hud.BedwarsResourceDisplay;
//?} else {
/*import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.platform.YACLPlatform;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.awt.Color;
*///?}
import tomeko.hymod.utils.Constants;

public class HyModConfig
        //? if = 1.8.9 {
        extends Config
         //?}
{
    //? if = 1.8.9 {
    public HyModConfig() {
        super(new Mod(Constants.MOD_NAME, ModType.HYPIXEL, "/assets/" + Constants.MOD_ID + "/icon.png"), Constants.MOD_ID + ".json");
        initialize();
    }
    //?} else {
    /*public static final ConfigClassHandler<HyModConfig> CONFIG = ConfigClassHandler.createBuilder(HyModConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(YACLPlatform.getConfigDir().resolve(Constants.MOD_ID + ".json"))
                    .build())
            .build();
    *///?}

    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String CATEGORY_BEDWARS = "Bedwars";


    //? if = 1.8.9 {
    @Exclude
    //?}
    private static final String bedwarsResourceDisplayName = "Bedwars Resource Display";

    //? if = 1.8.9 {
    @HUD(
            name = bedwarsResourceDisplayName,
            category = CATEGORY_BEDWARS
    )
    public BedwarsResourceDisplay bedwarsResourceDisplay = new BedwarsResourceDisplay();
    //?}


    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String CATEGORY_ARCADE = "Arcade";


    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String SUBCATEGORY_FARM_HUNT = "Farm Hunt";

    //? if = 1.8.9 {
    @Exclude
    //?}
    private static final String dangerousTauntWaypointName = "Dangerous Taunt Waypoint";

    //? if = 1.8.9 {
    @Switch(
            name = dangerousTauntWaypointName,
            category = CATEGORY_ARCADE,
            subcategory = SUBCATEGORY_FARM_HUNT
    )
            //?} else {
    /*@SerialEntry
            *///?}
    public static boolean dangerousTauntWaypointEnabled = true;


    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String CATEGORY_CHAT = "Chat";


    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String SUBCATEGORY_WHITE_CHAT_MESSAGES = "White Chat Messages";


    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String whitePrivateMessagesName = "White Private Messages";

    //? if = 1.8.9 {
    @Switch(
            name = whitePrivateMessagesName,
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_WHITE_CHAT_MESSAGES
    )
            //?} else {
    /*@SerialEntry
            *///?}
    public static boolean whitePrivateMessagesEnabled = true;

    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String whiteNoRankMessagesName = "White No Rank Messages";

    //? if = 1.8.9 {
    @Switch(
            name = whiteNoRankMessagesName,
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_WHITE_CHAT_MESSAGES
    )
            //?} else {
    /*@SerialEntry
            *///?}
    public static boolean whiteNoRankMessagesEnabled = true;


    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String SUBCATEGORY_HIDE_GUILD_MOTD = "Hide Guild MOTD";


    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String hideGuildMOTDEnabledName = "Enabled";

    //? if = 1.8.9 {
    @Switch(
            name = hideGuildMOTDEnabledName,
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_HIDE_GUILD_MOTD
    )
            //?} else {
    /*@SerialEntry
            *///?}
    public static boolean hideGuildMOTDEnabled = false;


    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String SUBCATEGORY_MVP_EMOJIS = "MVP++ Emojis";


    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String mvpEmojisEnabledName = "Enabled";

    //? if = 1.8.9 {
    @Switch(
            name = mvpEmojisEnabledName,
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_MVP_EMOJIS
    )
            //?} else {
    /*@SerialEntry
            *///?}
    public static boolean mvpEmojisEnabled = true;


    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String SUBCATEGORY_SENDCOORDS_COMMAND = "/sendcoords Command";


    //? if = 1.8.9 {
    @DualOption(
            name = "Default /sendcoords mode",
            left = "All",
            right = "Party",
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_SENDCOORDS_COMMAND
    )
            //?} else {
    /*@SerialEntry
            *///?}
    public static boolean sendcoordsToParty = true;


    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String SUBCATEGORY_COORDS_WAYPOINTS = "Coords Waypoints";


    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String coordsWaypointsEnabledName = "Enabled";

    //? if = 1.8.9 {
    @Switch(
            name = coordsWaypointsEnabledName,
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )

            //?} else {
    /*@SerialEntry
            *///?}
    public static boolean coordsWaypointsEnabled = true;

    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String coordsWaypointsColorName = "Color";

    //? if = 1.8.9 {
    @Color(
            name = coordsWaypointsColorName,
            allowAlpha = false,
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
    public static OneColor coordsWaypointsColor = new OneColor(255, 255, 255);
    //?} else {
    /*@SerialEntry
    public static Color coordsWaypointsColor = new Color(255, 255, 255);
    *///?}

    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String coordsWaypointsBoxOpacityName = "Box Opacity";
    //? if = 1.8.9 {
    @Exclude
    //?}
    private static final int coordsWaypointsBoxOpacityMin = 0;
    //? if = 1.8.9 {
    @Exclude
    //?}
    private static final int coordsWaypointsBoxOpacityMax = 100;
    //? if = 1.8.9 {
    @Exclude
    //?}
    private static final int coordsWaypointsBoxOpacityStep = 1;

    //? if = 1.8.9 {
    @Slider(
            name = coordsWaypointsBoxOpacityName,
            min = coordsWaypointsBoxOpacityMin,
            max = coordsWaypointsBoxOpacityMax,
            step = coordsWaypointsBoxOpacityStep,
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
            //?} else {
    /*@SerialEntry
            *///?}
    public static int coordsWaypointsBoxOpacity = 50;

    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String coordsWaypointsBeamOpacityName =  "Beam Opacity";
    //? if = 1.8.9 {
    @Exclude
    //?}
    private static final int coordsWaypointsBeamOpacityMin = 0;
    //? if = 1.8.9 {
    @Exclude
    //?}
    private static final int coordsWaypointsBeamOpacityMax = 100;
    //? if = 1.8.9 {
    @Exclude
    //?}
    private static final int coordsWaypointsBeamOpacityStep = 1;

    //? if = 1.8.9 {
    @Slider(
            name = coordsWaypointsBeamOpacityName,
            min = coordsWaypointsBeamOpacityMin,
            max = coordsWaypointsBeamOpacityMax,
            step = coordsWaypointsBeamOpacityStep,
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
            //?} else {
    /*@SerialEntry
            *///?}
    public static int coordsWaypointsBeamOpacity = 50;

    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String coordsWaypointsRenderTextName =  "Render Text";

    //? if = 1.8.9 {
    @Switch(
            name = coordsWaypointsRenderTextName,
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
            //?} else {
    /*@SerialEntry
            *///?}
    public static boolean coordsWaypointsRenderText = true;

    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String coordsWaypointsRenderDistanceName =  "Render Distance";

    //? if = 1.8.9 {
    @Switch(
            name = coordsWaypointsRenderDistanceName,
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
            //?} else {
    /*@SerialEntry
            *///?}
    public static boolean coordsWaypointsRenderDistance = true;

    //? if = 1.8.9 {
    @Exclude
    //?}
    private static final String coordsWaypointsTimeName = "Time";
    //? if = 1.8.9 {
    @Exclude
     //?}
    private static final int coordsWaypointsTimeMin = 0;
    //? if = 1.8.9 {
    @Exclude
     //?}
    private static final int coordsWaypointsTimeMax = 120;
    //? if = 1.8.9 {
    @Exclude
     //?}
    private static final int coordsWaypointsTimeStep = 1;

    //? if = 1.8.9 {
    @Slider(
            name = coordsWaypointsTimeName,
            min = coordsWaypointsTimeMin,
            max = coordsWaypointsTimeMax,
            step = coordsWaypointsTimeStep,
            category = CATEGORY_CHAT,
            subcategory = SUBCATEGORY_COORDS_WAYPOINTS
    )
            //?} else {
    /*@SerialEntry
            *///?}
    public static int coordsWaypointsTime = 30;


    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String CATEGORY_GUI = "GUI";


    //? if = 1.8.9 {
    @Exclude
            //?}
    private static final String SUBCATEGORY_MIDDLE_CLICK_GUI_ITEMS = "Middle Click GUI Items";


    //? if = 1.8.9 {
    @Exclude
    //?}
    private static final String middleClickGUIItemsEnabledName = "Enabled";

    //? if = 1.8.9 {
    @Switch(
            name = middleClickGUIItemsEnabledName,
            category = CATEGORY_GUI,
            subcategory = SUBCATEGORY_MIDDLE_CLICK_GUI_ITEMS
    )
            //?} else {
    /*@SerialEntry
            *///?}
    public static boolean middleClickGUIItemsEnabled = true;

    //? if >= 1.21.9 {
    /*public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Component.literal(Constants.MOD_NAME))

                .category(ConfigCategory.createBuilder()
                        .name(Component.literal(CATEGORY_BEDWARS))
                        .build())

                .category(ConfigCategory.createBuilder()
                        .name(Component.literal(CATEGORY_ARCADE))

                        .group(OptionGroup.createBuilder()
                                .name(Component.literal(SUBCATEGORY_FARM_HUNT))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal(dangerousTauntWaypointName))
                                        .binding(defaults.dangerousTauntWaypointEnabled, () -> config.dangerousTauntWaypointEnabled, newVal -> config.dangerousTauntWaypointEnabled = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        .build())

                .category(ConfigCategory.createBuilder()
                        .name(Component.literal(CATEGORY_CHAT))

                        .group(OptionGroup.createBuilder()
                                .name(Component.literal(SUBCATEGORY_WHITE_CHAT_MESSAGES))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal(whitePrivateMessagesName))
                                        .binding(defaults.whitePrivateMessagesEnabled, () -> config.whitePrivateMessagesEnabled, newVal -> config.whitePrivateMessagesEnabled = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal(whiteNoRankMessagesName))
                                        .binding(defaults.whiteNoRankMessagesEnabled, () -> config.whiteNoRankMessagesEnabled, newVal -> config.whiteNoRankMessagesEnabled = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Component.literal(SUBCATEGORY_HIDE_GUILD_MOTD))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal(hideGuildMOTDEnabledName))
                                        .binding(defaults.hideGuildMOTDEnabled, () -> config.hideGuildMOTDEnabled, newVal -> config.hideGuildMOTDEnabled = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Component.literal(SUBCATEGORY_MVP_EMOJIS))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal(mvpEmojisEnabledName))
                                        .binding(defaults.mvpEmojisEnabled, () -> config.mvpEmojisEnabled, newVal -> config.mvpEmojisEnabled = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Component.literal(SUBCATEGORY_SENDCOORDS_COMMAND))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal("/sendcoords to party instead of all by default"))
                                        .binding(defaults.sendcoordsToParty, () -> config.sendcoordsToParty, newVal -> config.sendcoordsToParty = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Component.literal(SUBCATEGORY_COORDS_WAYPOINTS))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal(coordsWaypointsEnabledName))
                                        .binding(defaults.coordsWaypointsEnabled, () -> config.coordsWaypointsEnabled, newVal -> config.coordsWaypointsEnabled = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Color>createBuilder()
                                        .name(Component.literal(coordsWaypointsColorName))
                                        .binding(defaults.coordsWaypointsColor, () -> config.coordsWaypointsColor, newVal -> config.coordsWaypointsColor = newVal)
                                        .controller(opt -> ColorControllerBuilder.create(opt))
                                        .build())

                                .option(Option.<Integer>createBuilder()
                                        .name(Component.literal(coordsWaypointsBoxOpacityName))
                                        .binding(defaults.coordsWaypointsBoxOpacity, () -> config.coordsWaypointsBoxOpacity, newVal -> config.coordsWaypointsBoxOpacity = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .formatValue(value -> Component.literal(value + "%"))
                                                .range(coordsWaypointsBoxOpacityMin, coordsWaypointsBoxOpacityMax)
                                                .step(coordsWaypointsBoxOpacityStep))
                                        .build())

                                .option(Option.<Integer>createBuilder()
                                        .name(Component.literal(coordsWaypointsBeamOpacityName))
                                        .binding(defaults.coordsWaypointsBeamOpacity, () -> config.coordsWaypointsBeamOpacity, newVal -> config.coordsWaypointsBeamOpacity = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .formatValue(value -> Component.literal(value + "%"))
                                                .range(coordsWaypointsBeamOpacityMin, coordsWaypointsBeamOpacityMax)
                                                .step(coordsWaypointsBeamOpacityStep))
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal(coordsWaypointsRenderTextName))
                                        .binding(defaults.coordsWaypointsRenderText, () -> config.coordsWaypointsRenderText, newVal -> config.coordsWaypointsRenderText = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal(coordsWaypointsRenderDistanceName))
                                        .binding(defaults.coordsWaypointsRenderDistance, () -> config.coordsWaypointsRenderDistance, newVal -> config.coordsWaypointsRenderDistance = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Integer>createBuilder()
                                        .name(Component.literal(coordsWaypointsTimeName))
                                        .binding(defaults.coordsWaypointsTime, () -> config.coordsWaypointsTime, newVal -> config.coordsWaypointsTime = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .formatValue(value -> Component.literal(value + "s"))
                                                .range(coordsWaypointsTimeMin, coordsWaypointsTimeMax)
                                                .step(coordsWaypointsTimeStep))
                                        .build())

                                .build())

                        .build())

                .category(ConfigCategory.createBuilder()
                        .name(Component.literal(CATEGORY_GUI))

                        .group(OptionGroup.createBuilder()
                                .name(Component.literal(SUBCATEGORY_MIDDLE_CLICK_GUI_ITEMS))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal(middleClickGUIItemsEnabledName))
                                        .binding(defaults.middleClickGUIItemsEnabled, () -> config.middleClickGUIItemsEnabled, newVal -> config.middleClickGUIItemsEnabled = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build())

                        .build())

        )).generateScreen(parent);
    }

    public static void register() {
        HyModConfig.CONFIG.load();
    }
    *///?}
}
