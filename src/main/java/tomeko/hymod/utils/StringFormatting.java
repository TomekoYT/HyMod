package tomeko.hymod.utils;

public class StringFormatting {
    public static String removeFormatting(String string) {
        return string.replaceAll("§.", "");
    }
}
