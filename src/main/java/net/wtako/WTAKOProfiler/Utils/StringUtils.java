package net.wtako.WTAKOProfiler.Utils;

import java.text.MessageFormat;

import org.bukkit.ChatColor;

public class StringUtils {

    public static String toInvisible(String s) {
        String hidden = "";
        for (final char c: s.toCharArray()) {
            hidden += ChatColor.COLOR_CHAR + "" + c;
        }
        return hidden;
    }

    public static String fromInvisible(String s) {
        return s.replaceAll("§", "");
    }

    public static String getChangeText(Double oldVal, Double newVal) {
        return StringUtils.getChangeText(newVal - oldVal);
    }

    public static String getChangeText(double diff) {
        if (diff == 0) {
            return "";
        }
        String symbol = "";
        if (diff < 0) {
            symbol = Lang.MINUS_SYMBOL.toString();
        } else {
            symbol = Lang.ADD_SYMBOL.toString();
        }
        return MessageFormat.format(Lang.DIGIT_CHANGE_FORMAT.toString(), symbol, Math.abs(diff));
    }

}
