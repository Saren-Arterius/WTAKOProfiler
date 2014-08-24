package net.wtako.WTAKOProfiler.Commands.Profile;

import net.wtako.WTAKOProfiler.Main;
import net.wtako.WTAKOProfiler.Utils.Lang;

import org.bukkit.command.CommandSender;

public class ArgReload {

    public ArgReload(final CommandSender sender, String[] args) {
        Main.getInstance().reloadConfig();
        Main.getInstance().getServer().getPluginManager().disablePlugin(Main.getInstance());
        Main.getInstance().getServer().getPluginManager().enablePlugin(Main.getInstance());
        sender.sendMessage(Lang.PLUGIN_RELOADED.toString());
    }

}
