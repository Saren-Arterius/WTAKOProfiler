package net.wtako.WTAKOProfiler.Utils;

import net.wtako.WTAKOProfiler.Main;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * An enum for requesting strings from the language file.
 * 
 * @author gomeow
 */
public enum Lang {

    TITLE("[" + Main.getInstance().getName() + "]"),

    PLAYER_INFO_TITLE("Player info"),
    PLAYER_INFO_HEALTH("H: {0}{1}"),
    PLAYER_INFO_FOOD("F: {0}{1}"),
    PLAYER_INFO_EXP("XP: {0}{1}"),
    PLAYER_INFO_BALANCE("${0}{1}"),
    PLAYER_INFO_ONLINE_PLAYERS("Onl: {0}{1}"),
    PLAYER_INFO_TPS("TPS: {0}{1}{2}"),
    PLAYER_INFO_EXTRA_MSG("Extra message%lb%lies here."),
    PLAYER_INFO_FORMAT("%lb%{0}{1}{2}{3}%lb%{4}%lb%{5}%lb%{6}"),
    PLAYER_INFO_DELIMITER("%lb%"),
    PLAYER_INFO_NO_LONGER_SHOW("&aPlayer info will no longer be shown until server restart."),
    PLAYER_INFO_SHOW_AGAIN("&aPlayer info will show upon your player."),

    DIGIT_CHANGE_FORMAT(" {0}{1}"),
    ADD_SYMBOL("&a+"),
    MINUS_SYMBOL("&c-"),
    DANGER("&c"),
    NORMAL("&f"),

    COMMAND_HELP_SEPERATOR("&6 | &a"),
    COMMAND_ARG_IN_USE("&e{0}&a"),
    SUB_COMMAND("Sub-command: &e{0}"),
    HELP_HELP("Type &b/" + Main.getInstance().getProperty("mainCommand") + " &a{0}&f to show help (this message)."),
    HELP_TSH("Type &a/" + Main.getInstance().getProperty("mainCommand").toLowerCase()
            + " &a{0}&f to toggle player info showing."),
            HELP_RELOAD("Type &a/" + Main.getInstance().getProperty("mainCommand")
                    + " &a{0}&f to reload the plugin. &c(OP only)"),
                    PLUGIN_RELOADED("&aPlugin reloaded."),
                    NO_PERMISSION_HELP(" (&cno permission&f)"),
                    NO_PERMISSION_COMMAND("&cYou are not allowed to use this command.");

    private String                   path;
    private String                   def;
    private static YamlConfiguration LANG;

    /**
     * Lang enum constructor.
     * 
     * @param path
     *            The string path.
     * @param start
     *            The default string.
     */
    Lang(String start) {
        path = name().toLowerCase().replace("_", "-");
        def = start;
    }

    /**
     * Set the {@code YamlConfiguration} to use.
     * 
     * @param config
     *            The config to set.
     */
    public static void setFile(YamlConfiguration config) {
        Lang.LANG = config;
    }

    @Override
    public String toString() {
        if (this == TITLE) {
            return ChatColor.translateAlternateColorCodes('&', Lang.LANG.getString(path, def)) + " ";
        }
        return ChatColor.translateAlternateColorCodes('&', Lang.LANG.getString(path, def));
    }

    /**
     * Get the default value of the path.
     * 
     * @return The default value of the path.
     */
    public String getDefault() {
        return def;
    }

    /**
     * Get the path to the string.
     * 
     * @return The path to the string.
     */
    public String getPath() {
        return path;
    }
}