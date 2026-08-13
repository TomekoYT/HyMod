package tomeko.hymod.utils

fun String.removeFormatting(): String = replace(Regex("§[0-9A-FK-OR]", RegexOption.IGNORE_CASE), "")