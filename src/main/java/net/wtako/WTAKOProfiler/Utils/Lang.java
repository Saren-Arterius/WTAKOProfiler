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

    TITLE("title", "[" + Main.getInstance().getName() + "]"),

    PLAYER_INFO_TITLE("player-info-title", "Player info"),
    PLAYER_INFO_HEALTH("player-info-health", "H: {0}{1}"),
    PLAYER_INFO_FOOD("player-info-food", "F: {0}{1}"),
    PLAYER_INFO_EXP("player-info-exp", "XP: {0}{1}"),

    PLAYER_INFO_BALANCE("player-info-balance", "${0}{1}"),

    PLAYER_INFO_ONLINE_PLAYERS("player-info-online-players", "Onl: {0}{1}"),

    PLAYER_INFO_EXTRA_MSG("player-info-extra-msg", "Extra message%lb%lies here."),
    PLAYER_INFO_FORMAT("player-info-format", "%lb%{0}{1}{2}{3}%lb%{4}%lb%{5}%lb%{6}"),
    PLAYER_INFO_DELIMITER("player-info-delimiter", "%lb%"),

    PLAYER_INFO_NO_LONGER_SHOW(
            "player-info-no-longer-show",
            "&aPlayer info will no longer be shown until server restart."),
    PLAYER_INFO_SHOW_AGAIN("player-info-show-again", "&aPlayer info will show upon your player."),

    DIGIT_CHANGE_FORMAT("digit-change-format", " {0}{1}"),
    ADD_SYMBOL("add-symbol", "&a+"),
    MINUS_SYMBOL("minus-symbol", "&c-"),
    DANGER("danger", "&c"),
    NORMAL("normal", "&f"),

    HELP_TSH("help-tsh", "Type &a/" + Main.getInstance().getProperty("mainCommand").toLowerCase()
            + " tsh&f to toggle player info showing."),
    HELP_RELOAD("help-reload", "Type &a/" + Main.getInstance().getProperty("mainCommand")
            + " reload&f to reload the plugin. &c(OP only)"),
    PLUGIN_RELOADED("plugin-reloaded", "&aPlugin reloaded."),
    NO_PERMISSION_COMMAND("no-permission-command", "&cYou are not allowed to use this command.");

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
    Lang(String path, String start) {
        this.path = path;
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