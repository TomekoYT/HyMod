package tomeko.hymod.location

import tomeko.hymod.config.HyModConfig

enum class DuelsModeType {
    OVERALL,
    SHORT,
    LONG
}

enum class DuelsMode(
    val modeId: String,
    val modeName: String,
    val modeType: DuelsModeType
) {
    OVERALL("OVERALL", HyModConfig.overallDuelsTextAboveNametag, DuelsModeType.OVERALL),
    SKYWARS("DUELS_SW_\\w+", HyModConfig.skywarsDuelsTextAboveNametag, DuelsModeType.SHORT),
    THE_BRIDGE("DUELS_BRIDGE_\\w+", HyModConfig.theBridgeDuelsTextAboveNametag, DuelsModeType.LONG),
    BEDWARS("BEDWARS_TWO_ONE_DUELS(?:_RUSH)?", HyModConfig.bedwarsDuelsTextAboveNametag, DuelsModeType.SHORT),
    CLASSIC("DUELS_CLASSIC_\\w+", HyModConfig.classicDuelsTextAboveNametag, DuelsModeType.SHORT),
    UHC("DUELS_UHC_\\w+", HyModConfig.uhcDuelsTextAboveNametag, DuelsModeType.SHORT),
    SUMO("DUELS_SUMO_DUEL", HyModConfig.sumoDuelsTextAboveNametag, DuelsModeType.SHORT),
    BOW("DUELS_BOW_DUEL", HyModConfig.bowDuelsTextAboveNametag, DuelsModeType.SHORT),
    MEGA_WALLS("DUELS_MW_DUEL", HyModConfig.megaWallsDuelsTextAboveNametag, DuelsModeType.LONG),
    PARKOUR("DUELS_PARKOUR_EIGHT", HyModConfig.parkourDuelsTextAboveNametag, DuelsModeType.LONG),
    QUAKECRAFT("DUELS_QUAKE_DUEL", HyModConfig.quakecraftDuelsTextAboveNametag, DuelsModeType.SHORT),
    SPLEEF("DUELS_(?:BOW)?SPLEEF_DUEL", HyModConfig.spleefDuelsTextAboveNametag, DuelsModeType.SHORT),
    OP("DUELS_OP_\\w+", HyModConfig.opDuelsTextAboveNametag, DuelsModeType.SHORT),
    BLITZ("DUELS_BLITZ_DUEL", HyModConfig.blitzDuelsTextAboveNametag, DuelsModeType.SHORT),
    COMBO("DUELS_COMBO_DUEL", HyModConfig.comboDuelsTextAboveNametag, DuelsModeType.SHORT),
    BOXING("DUELS_BOXING_DUEL", HyModConfig.boxingDuelsTextAboveNametag, DuelsModeType.LONG),
    NO_DEBUFF("DUELS_POTION_DUEL", HyModConfig.noDebuffDuelsTextAboveNametag, DuelsModeType.LONG);

    companion object {
        fun fromId(id: String): DuelsMode =
            entries.firstOrNull { it != OVERALL && Regex(it.modeId).matches(id) } ?: OVERALL
    }
}