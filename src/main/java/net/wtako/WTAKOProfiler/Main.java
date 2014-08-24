package net.wtako.WTAKOProfiler;

import net.milkbowl.vault.economy.Economy;
import net.wtako.WTAKOProfiler.Commands.CommandProfile;
import net.wtako.WTAKOProfiler.Methods.InfoShower;
import net.wtako.WTAKOProfiler.Methods.MemoryDatabase;
import net.wtako.WTAKOProfiler.Schedulers.CheckScheduler;
import net.wtako.WTAKOProfiler.Schedulers.GlobalCheckScheduler;
import net.wtako.WTAKOProfiler.Utils.Config;
import net.wtako.WTAKOProfiler.Utils.Lang;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;

public final class Main extends JavaPlugin {

    private static Main             instance;
    public static String            artifactId;
    public static YamlConfiguration LANG;
    public static File              LANG_FILE;
    public static Logger            log     = Logger.getLogger("WTAKOProfiler");
    public static Economy           economy = null;

    @Override
    @Override
    public void onEnable() {
        Main.instance = this;
        Main.artifactId = getProperty("artifactId");
        getCommand(getProperty("mainCommand")).setExecutor(new CommandProfile());
        Config.saveAll();
        loadLang();
        if (Config.CHECK_ECON.getBoolean()) {
            setupEconomy();
        }
        if (MemoryDatabase.getInstance() == null) {
            try {
                new MemoryDatabase();
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }
        if (Config.INFOBOX_ENABLED.getBoolean() && CheckScheduler.getInstance() == null) {
            new CheckScheduler();
        }
        if (Config.INFOBOX_ENABLED.getBoolean() && GlobalCheckScheduler.getInstance() == null) {
            new GlobalCheckScheduler();
        }
    }

    @Override
    @Override
    public void onDisable() {
        CheckScheduler.reload();
        InfoShower.reload();
        for (final Player player: getServer().getOnlinePlayers()) {
            player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        }
    }

    public void loadLang() {
        final File lang = new File(getDataFolder(), "messages.yml");
        if (!lang.exists()) {
            try {
                getDataFolder().mkdir();
                lang.createNewFile();
                final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(lang);
                defConfig.save(lang);
                Lang.setFile(defConfig);
                return;
            } catch (final IOException e) {
                e.printStackTrace(); // So they notice
                Main.log.severe("[" + Main.getInstance().getName() + "] Couldn't create language file.");
                Main.log.severe("[" + Main.getInstance().getName() + "] This is a fatal error. Now disabling");
                setEnabled(false); // Without it loaded, we can't send them
                // messages
            }
        }
        final YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
        for (final Lang item: Lang.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefault());
            }
        }
        Lang.setFile(conf);
        Main.LANG = conf;
        Main.LANG_FILE = lang;
        try {
            conf.save(getLangFile());
        } catch (final IOException e) {
            Main.log.log(Level.WARNING, "[" + Main.getInstance().getName() + "] Failed to save messages.yml.");
            Main.log.log(Level.WARNING, "[" + Main.getInstance().getName() + "] Report this stack trace to "
                    + getProperty("author") + ".");
            e.printStackTrace();
        }
    }

    private boolean setupEconomy() {
        final RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(
                net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            Main.economy = economyProvider.getProvider();
        }

        return (Main.economy != null);
    }

    /**
     * Gets the messages.yml config.
     * 
     * @return The messages.yml config.
     */
    public YamlConfiguration getLang() {
        return Main.LANG;
    }

    /**
     * Get the messages.yml file.
     * 
     * @return The messages.yml file.
     */
    public File getLangFile() {
        return Main.LANG_FILE;
    }

    public String getProperty(String key) {
        final YamlConfiguration spawnConfig = YamlConfiguration.loadConfiguration(getResource("plugin.yml"));
        return spawnConfig.getString(key);
    }

    public static Main getInstance() {
        return Main.instance;
    }

}
