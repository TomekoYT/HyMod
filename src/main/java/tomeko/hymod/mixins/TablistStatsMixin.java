package tomeko.hymod.mixins;

import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tomeko.hymod.stats.TablistStats;

@Mixin(PlayerTabOverlay.class)
public abstract class TablistStatsMixin {
    @Inject(
            method = "getNameForDisplay",
            at = @At("RETURN"),
            cancellable = true
    )
    private void hymod$tablistStats(
            PlayerInfo info,
            CallbackInfoReturnable<Component> cir
    ) {
        Component original = cir.getReturnValue();

        String uuid = info.getProfile().id().toString();
        Component prefix = TablistStats.INSTANCE.getTablistPrefix(uuid);
        if (prefix != null) cir.setReturnValue(prefix.copy().append(Component.literal(" ")).append(original));
    }
}
