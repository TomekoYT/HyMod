package tomeko.hymod.utils

object StringFormatting {
    fun removeFormatting(string: String): String {
        return string.replace("§.", "")
    }
}
