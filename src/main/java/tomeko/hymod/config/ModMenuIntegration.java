package tomeko.hymod.config;

//? if >= 1.21.9 {
/*import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import org.polyfrost.oneconfig.utils.v1.dsl.ScreensKt;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (Screen parent) -> ScreensKt.createScreen(HyModConfig.INSTANCE);
    }
}
*///?}
