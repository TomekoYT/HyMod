package tomeko.hymod.config;

//? if = 1.8.9 {

import org.lwjgl.opengl.Display;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class CloseInactiveConfigScreen {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new CloseInactiveConfigScreen());
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event) {
        if (Display.isActive() || mc.currentScreen == null || !mc.currentScreen.getClass().getName().contains("oneconfig"))
            return;
        mc.displayGuiScreen(null);
    }
}
//?}
