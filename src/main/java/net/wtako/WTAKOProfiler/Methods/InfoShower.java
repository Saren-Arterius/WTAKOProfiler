package net.wtako.WTAKOProfiler.Methods;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;

import net.wtako.WTAKOProfiler.Main;
import net.wtako.WTAKOProfiler.Schedulers.CheckScheduler;
import net.wtako.WTAKOProfiler.Schedulers.GlobalCheckScheduler;
import net.wtako.WTAKOProfiler.Utils.Lang;
import net.wtako.WTAKOProfiler.Utils.ScoreboardUtils;
import net.wtako.WTAKOProfiler.Utils.StringUtils;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class InfoShower {

    private static HashMap<Player, InfoShower> infoShowers     = new HashMap<Player, InfoShower>();

    private BukkitRunnable                     currentTask;
    private final Player                       player;
    public static long                         diffLastSeconds = Main.getInstance().getConfig()
                                                                       .getLong("InfoBox.DiffLastSeconds");
    public static int                          dangerValue     = Main.getInstance().getConfig()
                                                                       .getInt("InfoBox.DangerValue");

    public InfoShower(Player player) {
        this.player = player;
    }

    public void showInfo() {
        if (currentTask != null) {
            currentTask.cancel();
        }
        currentTask = new BukkitRunnable() {
            @Override
            public void run() {
                showInfo();
            }
        };
        currentTask.runTaskLaterAsynchronously(Main.getInstance(), InfoShower.diffLastSeconds * 20L);
        final String healthMessage = MessageFormat.format(
                Lang.PLAYER_INFO_HEALTH.toString(),
                player.getHealth() <= InfoShower.dangerValue ? Lang.DANGER.toString()
                        + String.valueOf(player.getHealth()) + Lang.NORMAL.toString() : player.getHealth(),
                getDiffString(0))
                + Lang.PLAYER_INFO_DELIMITER.toString();
        final String foodMessage = MessageFormat.format(
                Lang.PLAYER_INFO_FOOD.toString(),
                player.getFoodLevel() <= InfoShower.dangerValue ? Lang.DANGER.toString()
                        + String.valueOf(player.getHealth()) + Lang.NORMAL.toString() : player.getFoodLevel(),
                getDiffString(1))
                + Lang.PLAYER_INFO_DELIMITER.toString();
        final String expMessage = MessageFormat.format(Lang.PLAYER_INFO_EXP.toString(),
                CheckScheduler.getEXPManager(player).getCurrentExp(), getDiffString(2))
                + Lang.PLAYER_INFO_DELIMITER.toString();
        String balanceMessage = "";
        if (Main.economy != null) {
            balanceMessage = MessageFormat.format(Lang.PLAYER_INFO_BALANCE.toString(),
                    Math.round(Main.economy.getBalance(player) * 10D) / 10D, getDiffString(3))
                    + Lang.PLAYER_INFO_DELIMITER.toString();
        }
        final String onlinePlayersMessage = MessageFormat.format(Lang.PLAYER_INFO_ONLINE_PLAYERS.toString(), Main
                .getInstance().getServer().getOnlinePlayers().length,
                StringUtils.getChangeText(GlobalCheckScheduler.getInstance().getDiffCache(0)))
                + Lang.PLAYER_INFO_DELIMITER.toString();
        final String message = MessageFormat.format(Lang.PLAYER_INFO_FORMAT.toString(), player.getName()
                + Lang.PLAYER_INFO_DELIMITER.toString(), healthMessage, foodMessage, expMessage, balanceMessage,
                onlinePlayersMessage, Lang.PLAYER_INFO_EXTRA_MSG.toString());
        ScoreboardUtils.showScoreboardMessageLowPriority(Lang.PLAYER_INFO_TITLE.toString(), message,
                Lang.PLAYER_INFO_DELIMITER.toString(), player);
    }

    public void addDiff(int index, double value) throws SQLException {
        final PreparedStatement insStmt = MemoryDatabase
                .getInstance()
                .getConn()
                .prepareStatement(
                        "INSERT INTO `player_diffs` (`player`, `index`, `value`, `timestamp`) VALUES (?, ?, ?, ?)");
        insStmt.setString(1, player.getUniqueId().toString());
        insStmt.setInt(2, index);
        insStmt.setDouble(3, value);
        insStmt.setLong(4, System.currentTimeMillis());
        insStmt.execute();
        insStmt.close();
    }

    public double getDiff(int index, Long last) throws SQLException {
        double diff = 0;
        final PreparedStatement selStmt = MemoryDatabase
                .getInstance()
                .getConn()
                .prepareStatement(
                        "SELECT SUM(value) FROM `player_diffs` WHERE `player` = ? AND `index` = ? AND `timestamp` >= ?");
        selStmt.setString(1, player.getUniqueId().toString());
        selStmt.setInt(2, index);
        selStmt.setLong(3, last);
        final ResultSet result = selStmt.executeQuery();
        result.next();
        diff = result.getDouble(1);
        result.close();
        selStmt.close();
        return diff;
    }

    public String getDiffString(int index) {
        double diff = 0;
        try {
            diff = getDiff(index, System.currentTimeMillis() - (InfoShower.diffLastSeconds * 1000L));
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return StringUtils.getChangeText(diff);
    }

    public static InfoShower getInfoShower(Player player) {
        if (!InfoShower.infoShowers.containsKey(player)) {
            InfoShower.infoShowers.put(player, new InfoShower(player));
        }
        return InfoShower.infoShowers.get(player);
    }

    public static void reload() {
        InfoShower.infoShowers.clear();
    }
}
