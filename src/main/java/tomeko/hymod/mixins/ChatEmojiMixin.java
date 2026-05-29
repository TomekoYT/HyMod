package tomeko.hymod.mixins;

import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import tomeko.hymod.config.HyModConfig;

import java.util.HashMap;
import java.util.Map;

@Mixin(GuiScreen.class)
public class ChatEmojiMixin {
    private static final Map<String, String> emojis = new HashMap<>();

    static {
        emojis.put("<3", "❤");
        emojis.put(":arrow:", "➜");
        emojis.put(":bum:", "♿");
        emojis.put(":cat:", "= ＾● ⋏ ●＾ =");
        emojis.put(":cute:", "(✿◠‿◠)");
        emojis.put(":dab:", "<o/");
        emojis.put(":dj:", "ヽ(⌐■_■)ノ♬");
        emojis.put(":dog:", "(ᵔᴥᵔ)");
        emojis.put(":gimme:", "༼つ◕_◕༽つ");
        emojis.put(":java:", "☕");
        emojis.put(":maths:", "√(π+x)=L");
        emojis.put(":no:", "✖");
        emojis.put(":peace:", "✌");
        emojis.put(":puffer:", "<('O')>");
        emojis.put(":pvp:", "⚔");
        emojis.put(":shrug:", "¯\\_(ツ)_/¯");
        emojis.put(":skull:", "☠");
        emojis.put(":sloth:", "(・⊝・)");
        emojis.put(":snail:", "@'-'");
        emojis.put(":snow:", "☃");
        emojis.put(":star:", "✮");
        emojis.put(":tableflip:", "(╯°□°）╯︵ ┻━┻");
        emojis.put(":thinking:", "(0.o?)");
        emojis.put(":totem:", "☉_☉");
        emojis.put(":typing:", "✎...");
        emojis.put(":wizard:", "('-')⊃━☆ﾟ.*･｡ﾟ");
        emojis.put(":yes:", "✔");
        emojis.put(":yey:", "ヽ (◕◡◕) ﾉ");
        emojis.put("ez", "ｅｚ");
        emojis.put("h/", "ヽ(^◇^*)/");
        emojis.put("o/", "( ﾟ◡ﾟ)/");
    }

    @ModifyVariable(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private String replaceWithEmoji(String message) {
        if (!HyModConfig.mvpEmojisEnabled) return message;

        for (Map.Entry<String, String> entry : emojis.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }
        return message;
    }
}
