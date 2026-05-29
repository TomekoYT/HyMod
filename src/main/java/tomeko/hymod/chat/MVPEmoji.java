package tomeko.hymod.chat;

//? if >= 1.21.9 {
/*import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
*///?}
import tomeko.hymod.config.HyModConfig;

import java.util.HashMap;
import java.util.Map;

public class MVPEmoji {
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
        emojis.put("h/", "ヽ(^◇^*)/");
        emojis.put("o/", "( ﾟ◡ﾟ)/");
    }

    //? if >= 1.21.9 {
    /*public static void register() {
        ClientSendMessageEvents.MODIFY_CHAT.register(MVPEmoji::replaceWithEmoji);
        ClientSendMessageEvents.MODIFY_COMMAND.register(MVPEmoji::replaceWithEmoji);
    }
    *///?}

    public static String replaceWithEmoji(String message) {
        if (!HyModConfig.mvpEmojisEnabled) return message;

        for (Map.Entry<String, String> entry : emojis.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }
        return message;
    }
}
