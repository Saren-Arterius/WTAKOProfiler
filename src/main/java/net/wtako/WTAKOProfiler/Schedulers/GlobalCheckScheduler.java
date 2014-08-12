package net.wtako.WTAKOProfiler.Schedulers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.wtako.WTAKOProfiler.Main;
import net.wtako.WTAKOProfiler.Methods.InfoShower;
import net.wtako.WTAKOProfiler.Methods.MemoryDatabase;

import org.bukkit.scheduler.BukkitRunnable;

public class GlobalCheckScheduler {

    private static GlobalCheckScheduler instance   = null;
    private static final double[]       values     = new double[] {0, 20};
    private static final double[]       diffCaches = new double[] {0, 0};

    public GlobalCheckScheduler() {
        GlobalCheckScheduler.instance = this;
        Main.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    if (checkOnlinePlayers()) {
                        CheckScheduler.notify(0);
                    }
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }
        }, 0L, Main.getInstance().getConfig().getLong("InfoBox.CheckerTicksInterval"));
        Main.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                checkTPS();
            }
        }, 0L, 40L);
    }

    private boolean checkOnlinePlayers() throws SQLException {
        boolean hasChange = false;
        final int playersOnline = Main.getInstance().getServer().getOnlinePlayers().length;
        if (playersOnline != GlobalCheckScheduler.values[0]) {
            final double diff = playersOnline - GlobalCheckScheduler.values[0];
            addDiff(0, diff);
            hasChange = true;
        }
        GlobalCheckScheduler.diffCaches[0] = getDiff(0, System.currentTimeMillis()
                - (InfoShower.diffLastSeconds * 1000L));
        GlobalCheckScheduler.values[0] = playersOnline;
        return hasChange;
    }

    private void checkTPS() {
        final long currentTime = System.currentTimeMillis();
        new BukkitRunnable() {
            @Override
            public void run() {
                final long endTime = System.currentTimeMillis();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        double tps = 20000D / (endTime - currentTime);
                        tps = tps > 20 ? 20 : tps;
                        boolean hasChange = false;
                        if (Math.round(GlobalCheckScheduler.values[1] * 10D) / 10D != tps) {
                            try {
                                addDiff(1, tps - GlobalCheckScheduler.values[1]);
                            } catch (final SQLException e) {
                                e.printStackTrace();
                            }
                            hasChange = true;
                        }
                        try {
                            GlobalCheckScheduler.diffCaches[1] = getDiff(1, System.currentTimeMillis()
                                    - (InfoShower.diffLastSeconds * 1000L));
                        } catch (final SQLException e) {
                            e.printStackTrace();
                        }
                        GlobalCheckScheduler.values[1] = tps;
                        if (hasChange) {
                            CheckScheduler.notify(1);
                        }
                    }
                }.runTaskAsynchronously(Main.getInstance());
            }
        }.runTaskLater(Main.getInstance(), 20L);
    }

    private void addDiff(int index, double diff) throws SQLException {
        final PreparedStatement insStmt = MemoryDatabase.getInstance().getConn()
                .prepareStatement("INSERT INTO `global_diffs` (`index`, `value`, `timestamp`) VALUES (?, ?, ?)");
        insStmt.setInt(1, index);
        insStmt.setDouble(2, diff);
        insStmt.setLong(3, System.currentTimeMillis());
        insStmt.execute();
        insStmt.close();
    }

    private double getDiff(int index, Long last) throws SQLException {
        double diff = 0;
        final PreparedStatement selStmt = MemoryDatabase.getInstance().getConn()
                .prepareStatement("SELECT SUM(value) FROM `global_diffs` WHERE `index` = ? AND `timestamp` >= ?");
        selStmt.setInt(1, index);
        selStmt.setLong(2, last);
        final ResultSet result = selStmt.executeQuery();
        result.next();
        diff = result.getDouble(1);
        result.close();
        selStmt.close();
        return diff;
    }

    public double getValue(int index) {
        return GlobalCheckScheduler.values[index];
    }

    public double getDiffCache(int index) {
        return GlobalCheckScheduler.diffCaches[index];
    }

    public static void reload() {
        GlobalCheckScheduler.instance = null;
    }

    public static GlobalCheckScheduler getInstance() {
        return GlobalCheckScheduler.instance;
    }

}
