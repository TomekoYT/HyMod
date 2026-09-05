package tomeko.hymod.mixins;

//? if = 1.8.9-forge {

/*import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
*///?} else {
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
//?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tomeko.hymod.config.HyModConfig;
import tomeko.hymod.location.HypixelPackets;
import tomeko.hymod.stats.HypixelStatsFetcher;
import java.util.UUID;

@Mixin(
        //? if = 1.8.9-forge {
        /*GuiPlayerTabOverlay.class
        *///?} else {
        PlayerTabOverlay.class
        //?}
)
public abstract class TablistStatsMixin {
    @Inject(
            method =
                    //? if = 1.8.9-forge {
                    /*"getPlayerName",
                     *///?} else {
                    "getNameForDisplay",
            //?}
            at = @At("RETURN"),
            cancellable = true
    )
    private void hymod$tablistStats(
            //? if = 1.8.9-forge {
            /*NetworkPlayerInfo info,
            CallbackInfoReturnable<String> cir
            *///?} else {
            PlayerInfo info,
            CallbackInfoReturnable<Component> cir
            //?}
    ) {
        if (!HypixelPackets.INSTANCE.getOnHypixel()) return;

        //? if = 1.8.9-forge {
        /*String original
         *///?} else {
        Component original
                //?}
                = cir.getReturnValue();

        UUID uuid =
                //? if = 1.8.9-forge {
                /*info.getGameProfile().getId();
                 *///?} else {
                info.getProfile().id();
        //?}

        HypixelStatsFetcher.CachedStats stats = HypixelStatsFetcher.INSTANCE.getCachedStats(uuid.toString());

        //? if = 1.8.9-forge {
        /*IChatComponent prefix
         *///?} else {
        Component prefix
                //?}
                = null;
        if (uuid.version() == 1) {
            if (HyModConfig.INSTANCE.getShowNickedIndicatorInTablist()) {
                prefix =
                        //? if = 1.8.9-forge {
                        //new ChatComponentText(HyModConfig.INSTANCE.getNickedIndicatorText());
                        //?} else {
                        Component.literal(HyModConfig.INSTANCE.getNickedIndicatorText());
                //?}
            }
        } else {
            if (HypixelPackets.INSTANCE.getInBedwars() && HyModConfig.INSTANCE.getShowBedwarsStarsInTablist()) {
                prefix = stats.getBedwars();
            } else if (HypixelPackets.INSTANCE.getInSkywars() && HyModConfig.INSTANCE.getShowSkywarsStarsInTablist()) {
                prefix = stats.getSkywars();
            } else if (HypixelPackets.INSTANCE.getInDuels() && HyModConfig.INSTANCE.getShowDuelsDivisionInTablist()) {
                prefix = stats.getDuels();
            }
        }

        if (prefix != null) cir.setReturnValue(
                //? if = 1.8.9-forge {
                /*prefix.createCopy().appendSibling(new ChatComponentText(" ")).appendText(original).getFormattedText()
                 *///?} else {
                prefix.copy().append(Component.literal(" ")).append(original)
                //?}
        );
    }
}