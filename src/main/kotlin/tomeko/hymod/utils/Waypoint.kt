package tomeko.hymod.utils

//? if = 1.8.9 {
/*import net.minecraft.util.BlockPos
*///?} else {
import net.minecraft.core.BlockPos
//?}

import java.awt.Color

class Waypoint(
    var pos: BlockPos,
    var color: Color,
    var text: String,
    var boxOpacity: Float,
    var beamOpacity: Float,
    var renderText: Boolean,
    var renderDistance: Boolean,
    var tickTime: Int
)