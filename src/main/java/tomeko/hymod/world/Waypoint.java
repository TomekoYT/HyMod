package tomeko.hymod.world;

import net.minecraft.util.BlockPos;

import java.awt.*;

public class Waypoint {
    BlockPos pos;
    Color color;
    String text;
    float boxOpacity;
    float beamOpacity;
    boolean renderText;
    boolean renderDistance;
    int tickTime;

    public Waypoint(
            BlockPos pos,
            Color color,
            String text,
            float boxOpacity,
            float beamOpacity,
            boolean renderText,
            boolean renderDistance,
            int tickTime
    ) {
        this.pos = pos;
        this.color = color;
        this.text = text;
        this.boxOpacity = boxOpacity;
        this.beamOpacity = beamOpacity;
        this.renderText = renderText;
        this.renderDistance = renderDistance;
        this.tickTime = tickTime;
    }
}
