package net.wtako.WTAKOProfiler.Commands;

import net.wtako.WTAKOProfiler.Commands.Profile.ArgHelp;
import net.wtako.WTAKOProfiler.Commands.Profile.ArgReload;
import net.wtako.WTAKOProfiler.Commands.Profile.ArgTSh;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandProfile implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                new ArgHelp(sender);
                return true;
            } else if (args[0].equalsIgnoreCase("reload")) {
                new ArgReload(sender);
                return true;
            } else if (args[0].equalsIgnoreCase("tsh")) {
                new ArgTSh(sender);
                return true;
            }
        }
        return false;
    }
}
