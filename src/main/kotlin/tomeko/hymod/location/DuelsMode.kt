package tomeko.hymod.location

enum class DuelsMode(
    val modeId: String,
    val modeName: String,
    val modeType: DuelsModeType
) {
    OVERALL("OVERALL", "§eOverall", DuelsModeType.OVERALL),
    SKYWARS("DUELS_SW_\\w+", "§bSky§aWars", DuelsModeType.SHORT),
    THE_BRIDGE("DUELS_BRIDGE_\\w+", "§5The Bridge", DuelsModeType.LONG),
    BEDWARS("BEDWARS_TWO_ONE_DUELS(?:_RUSH)?", "§fBed§cWars", DuelsModeType.SHORT),
    CLASSIC("DUELS_CLASSIC_\\w+", "§fClassic", DuelsModeType.SHORT),
    UHC("DUELS_UHC_\\w+", "§6UHC", DuelsModeType.SHORT),
    SUMO("DUELS_SUMO_DUEL", "§bSumo", DuelsModeType.SHORT),
    BOW("DUELS_BOW_DUEL", "§6Bow", DuelsModeType.SHORT),
    MEGA_WALLS("DUELS_MW_DUEL", "§8Mega Walls", DuelsModeType.LONG),
    PARKOUR("DUELS_PARKOUR_EIGHT", "§eParkour", DuelsModeType.LONG),
    QUAKECRAFT("DUELS_QUAKE_DUEL", "§7Quakecraft", DuelsModeType.SHORT),
    SPLEEF("DUELS_(?:BOW)?SPLEEF_DUEL", "§9Spleef", DuelsModeType.SHORT),
    OP("DUELS_OP_\\w+", "§5OP", DuelsModeType.SHORT),
    BLITZ("DUELS_BLITZ_DUEL", "§6Blitz", DuelsModeType.SHORT),
    COMBO("DUELS_COMBO_DUEL", "§cCombo", DuelsModeType.SHORT),
    BOXING("DUELS_BOXING_DUEL", "§4Boxing", DuelsModeType.LONG),
    NO_DEBUFF("DUELS_POTION_DUEL", "§dNoDebuff", DuelsModeType.LONG);

    companion object {
        fun fromId(id: String): DuelsMode =
            entries.firstOrNull { it != OVERALL && Regex(it.modeId).matches(id) } ?: OVERALL
    }
}