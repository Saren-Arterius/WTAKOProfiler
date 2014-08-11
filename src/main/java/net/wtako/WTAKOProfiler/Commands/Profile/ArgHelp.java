package net.wtako.WTAKOProfiler.Commands.Profile;

import net.wtako.WTAKOProfiler.Main;
import net.wtako.WTAKOProfiler.Utils.Lang;

import org.bukkit.command.CommandSender;

public class ArgHelp {

    public ArgHelp(CommandSender sender) {
        sender.sendMessage(Main.getInstance().getName() + " v" + Main.getInstance().getProperty("version"));
        sender.sendMessage("Author: " + Main.getInstance().getProperty("author"));
        sender.sendMessage(Lang.HELP_TSH.toString());
        sender.sendMessage(Lang.HELP_RELOAD.toString());
    }

}
