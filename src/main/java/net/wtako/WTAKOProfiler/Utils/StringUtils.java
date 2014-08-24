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

    public static ChatColor getTPSColor(double tps) {
        if (tps >= 19.8) {
            return ChatColor.AQUA;
        } else if (tps >= 18) {
            return ChatColor.GREEN;
        } else if (tps >= 14) {
            return ChatColor.YELLOW;
        } else if (tps >= 10) {
            return ChatColor.RED;
        } else {
            return ChatColor.DARK_RED;
        }
    }

}
