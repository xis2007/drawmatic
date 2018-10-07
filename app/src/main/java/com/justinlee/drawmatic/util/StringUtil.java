package com.justinlee.drawmatic.util;

public class StringUtil {
    public StringUtil() {
    }

    public static boolean isEmptyString(String string) {
        string = string.replaceAll(" ", "");

        return "".equals(string) || " ".equals(string) || string.isEmpty();
    }
}
