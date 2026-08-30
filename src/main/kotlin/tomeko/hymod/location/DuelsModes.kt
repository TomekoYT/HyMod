package tomeko.hymod.location

enum class DuelsModes(
    val modeId: String,
    val modeName: String
) {
    SKYWARS("DUELS_SW_\\w+", "SkyWars Duels"),
    THE_BRIDGE("DUELS_BRIDGE_\\w+", "The Bridge Duels"),
    BEDWARS("BEDWARS_TWO_ONE_DUELS(?:_RUSH)?", "BedWars Duels"),
    CLASSIC("DUELS_CLASSIC_\\w+", "Classic Duels"),
    UHC("DUELS_UHC_\\w+", "UHC Duels"),
    SUMO("DUELS_SUMO_DUEL", "Sumo Duels"),
    BOW("DUELS_BOW_DUEL", "Bow Duels"),
    MEGA_WALLS("DUELS_MW_DUEL", "Mega Walls Duels"),
    PARKOUR("DUELS_PARKOUR_EIGHT", "Parkour Duels"),
    QUAKECRAFT("DUELS_QUAKE_DUEL", "Quakecraft Duels"),
    SPLEEF("DUELS_(?:BOW)?SPLEEF_DUEL", "Spleef Duels"),
    OP("DUELS_OP_\\w+", "OP Duels"),
    BLITZ("DUELS_BLITZ_DUEL", "Blitz Duels"),
    COMBO("DUELS_COMBO_DUEL", "Combo Duels"),
    BOXING("DUELS_BOXING_DUEL", "Boxing Duels"),
    NO_DEBUFF("DUELS_POTION_DUEL", "NoDebuff Duels");

    companion object {
        fun fromId(id: String): DuelsModes? = entries.firstOrNull { Regex(it.modeId).matches(id) }
    }
}