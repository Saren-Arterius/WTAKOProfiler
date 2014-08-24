package net.wtako.WTAKOProfiler.Utils;

import java.util.ArrayList;
import java.util.UUID;

import net.wtako.WTAKOProfiler.Main;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreboardUtils {

    public static ArrayList<UUID> noShowScoreboardPlayers = new ArrayList<UUID>();

    @SuppressWarnings("deprecation")
    public static void showScoreboardMessageLowPriority(final String title, final String message,
            final String delimiter, final Player player) {

        new BukkitRunnable() {

            @Override
            public void run() {

                if (ScoreboardUtils.noShowScoreboardPlayers.contains(player.getUniqueId())) {
                    return;
                }

                final Objective currentObj = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);

                if (currentObj != null && !currentObj.getDisplayName().equalsIgnoreCase(title)) {
                    return;
                }

                final ScoreboardManager manager = Main.getInstance().getServer().getScoreboardManager();
                final Scoreboard board = manager.getNewScoreboard();

                new BukkitRunnable() {

                    @Override
                    public void run() {
                        final Objective objective = board.registerNewObjective("test", "dummy");
                        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                        objective.setDisplayName(title);

                        final String[] messageArray = message.split(delimiter);
                        int currentScore = messageArray.length;
                        for (final String item: messageArray) {
                            final String msg = item.equalsIgnoreCase("") ? StringUtils.toInvisible(String
                                    .valueOf(currentScore)) : item
                                    .substring(0, item.length() > 16 ? 15 : item.length());
                            final Score score = objective
                                    .getScore(Main.getInstance().getServer().getOfflinePlayer(msg));
                            score.setScore(currentScore);
                            currentScore--;
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                                player.setScoreboard(board);
                            }
                        }.runTask(Main.getInstance());
                    }
                }.runTaskAsynchronously(Main.getInstance());
            }
        }.runTask(Main.getInstance());
    }
}
