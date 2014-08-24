package net.wtako.WTAKOProfiler.Utils;

import net.wtako.WTAKOProfiler.Main;

import org.bukkit.configuration.file.FileConfiguration;

public enum Config {

    INFOBOX_ENABLED("infobox.enabled", true),
    CHECK_ECON("infobox.show-econ", true),
    CHECKER_INTERVAL("infobox.checker-interval-ticks", 2),
    CHECK_PLAYER_INFOBOX_INTERVAL("infobox.check-player-infobox-interval", 20),
    TPS_CHECK_INTERVAL("infobox.tps-check-interval-ticks", 40),
    DANGER_VALUE("infobox.danger-value", 6),
    DIFF_LAST("infobox.diff-last", 10);

    private String path;
    private Object value;

    Config(String path, Object var) {
        this.path = path;
        final FileConfiguration config = Main.getInstance().getConfig();
        if (config.contains(path)) {
            value = config.get(path);
        } else {
            value = var;
        }
    }

    public Object getValue() {
        return value;
    }

    public boolean getBoolean() {
        return (boolean) value;
    }

    public String getString() {
        return (String) value;
    }

    public int getInt() {
        if (value instanceof Double) {
            return ((Double) value).intValue();
        }
        return (int) value;
    }

    public long getLong() {
        return Integer.valueOf(getInt()).longValue();
    }

    public double getDouble() {
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        }
        return (double) value;
    }

    public String getPath() {
        return path;
    }

    @SuppressWarnings("unchecked")
    public List<Object> getValues() {
        return (List<Object>) value;
    }

    @SuppressWarnings("unchecked")
    public List<String> getStrings() {
        return (List<String>) value;
    }

    public static void saveAll() {
        final FileConfiguration config = Main.getInstance().getConfig();
        for (final Config setting: Config.values()) {
            if (!config.contains(setting.getPath())) {
                config.set(setting.getPath(), setting.getValue());
            }
        }
        Main.getInstance().saveConfig();
    }

}