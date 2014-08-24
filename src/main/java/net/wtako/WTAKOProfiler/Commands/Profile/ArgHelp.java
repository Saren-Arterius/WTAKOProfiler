package net.wtako.WTAKOProfiler.Commands.Profile;

import net.wtako.WTAKOProfiler.Utils.CommandHelper;
import net.wtako.WTAKOProfiler.Utils.CommandsProfile;

import org.bukkit.command.CommandSender;

public class ArgHelp {

    public ArgHelp(final CommandSender sender, String[] args) {
        CommandHelper.sendHelp(sender, CommandsProfile.values(), "");
    }
}
