package tomeko.hymod.chat

//? if >= 1.21.9 {
/*import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents
 *///?}

import tomeko.hymod.config.HyModConfig

object MVPEmoji {
    private val emojis = mapOf(
        "<3" to "❤",
        ":arrow:" to "➜",
        ":bum:" to "♿",
        ":cat:" to "= ＾● ⋏ ●＾ =",
        ":cute:" to "(✿◠‿◠)",
        ":dab:" to "<o/",
        ":dj:" to "ヽ(⌐■_■)ノ♬",
        ":dog:" to "(ᵔᴥᵔ)",
        ":gimme:" to "༼つ◕_◕༽つ",
        ":java:" to "☕",
        ":maths:" to "√(π+x)=L",
        ":no:" to "✖",
        ":peace:" to "✌",
        ":puffer:" to "<('O')>",
        ":pvp:" to "⚔",
        ":shrug:" to "¯\\_(ツ)_/¯",
        ":skull:" to "☠",
        ":sloth:" to "(・⊝・)",
        ":snail:" to "@'-'",
        ":snow:" to "☃",
        ":star:" to "✮",
        ":tableflip:" to "(╯°□°）╯︵ ┻━┻",
        ":thinking:" to "(0.o?)",
        ":totem:" to "☉_☉",
        ":typing:" to "✎...",
        ":wizard:" to "('-')⊃━☆ﾟ.*･｡ﾟ",
        ":yes:" to "✔",
        ":yey:" to "ヽ (◕◡◕) ﾉ",
        "h/" to "ヽ(^◇^*)/",
        "o/" to "( ﾟ◡ﾟ)/"
    )

    //? if >= 1.21.9 {
    /*fun register() {
        ClientSendMessageEvents.MODIFY_CHAT.register(::replaceWithEmoji)
        ClientSendMessageEvents.MODIFY_COMMAND.register(::replaceWithEmoji)
    }
    *///?}

    @JvmStatic
    fun replaceWithEmoji(message: String): String {
        if (!HyModConfig.mvpEmojisEnabled) return message

        var result = message
        for ((key, value) in emojis) {
            result = result.replace(key, value)
        }

        return result
    }
}