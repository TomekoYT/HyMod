package tomeko.hymod.utils

//? if = 1.8.9 {
/*import cc.polyfrost.oneconfig.config.core.OneColor
import net.minecraft.util.BlockPos

*///?} else {
import net.minecraft.core.BlockPos
import org.polyfrost.compose.render.PolyColor
//?}

class Waypoint(
    var pos: BlockPos,
    var boxColor:
    //? if = 1.8.9 {
    /*OneColor,
    *///?} else {
    PolyColor,
    //?}
    var beamColor:
    //? if = 1.8.9 {
    /*OneColor,
    *///?} else {
    PolyColor,
    //?}
    var owner: String,
    var renderOwner: Boolean,
    var ownerColor:
    //? if = 1.8.9 {
    /*OneColor,
    *///?} else {
    PolyColor,
    //?}
    var text: String,
    var renderText: Boolean,
    var textColor:
    //? if = 1.8.9 {
    /*OneColor,
    *///?} else {
    PolyColor,
    //?}
    var renderDistance: Boolean,
    var distanceTextColor:
    //? if = 1.8.9 {
    /*OneColor,
    *///?} else {
    PolyColor,
    //?}
    var tickTime: Int
)