package tomeko.hymod.utils;

import net.minecraft.util.BlockPos;

import java.awt.*;

public class Waypoint {
    public BlockPos pos;
    public Color color;
    public String text;
    public float boxOpacity;
    public float beamOpacity;
    public boolean renderText;
    public boolean renderDistance;
    public int tickTime;

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
