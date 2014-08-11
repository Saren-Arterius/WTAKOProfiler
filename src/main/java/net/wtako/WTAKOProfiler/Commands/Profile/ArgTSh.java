package net.wtako.WTAKOProfiler.Commands.Profile;

import net.wtako.WTAKOProfiler.Main;
import net.wtako.WTAKOProfiler.Utils.Lang;
import net.wtako.WTAKOProfiler.Utils.ScoreboardUtils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ArgTSh {

    public ArgTSh(CommandSender sender) {
        if (ScoreboardUtils.noShowScoreboardPlayers.contains(((Player) sender).getUniqueId())) {
            ScoreboardUtils.noShowScoreboardPlayers.remove(((Player) sender).getUniqueId());
            sender.sendMessage(Lang.PLAYER_INFO_SHOW_AGAIN.toString());
        } else {
            ScoreboardUtils.noShowScoreboardPlayers.add(((Player) sender).getUniqueId());
            final ScoreboardManager manager = Main.getInstance().getServer().getScoreboardManager();
            final Scoreboard board = manager.getNewScoreboard();
            ((Player) sender).setScoreboard(board);
            sender.sendMessage(Lang.PLAYER_INFO_NO_LONGER_SHOW.toString());
        }
    }

}
