package it.vitalegi.mangar.util;

public class StringUtil {

    public static String leftPadding(String str, int len, char character) {
        while (str.length() < len) {
            str = character + str;
        }
        return str;
    }
}
