package net.wtako.WTAKOProfiler.Methods;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import net.wtako.WTAKOProfiler.Main;

import org.bukkit.scheduler.BukkitRunnable;

public class MemoryDatabase {

    private static MemoryDatabase instance;
    private final Connection      conn;

    public MemoryDatabase() throws SQLException {
        MemoryDatabase.instance = this;
        conn = DriverManager.getConnection("jdbc:sqlite::memory:");
        createTables();
        final BukkitRunnable timer = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    MemoryDatabase.getInstance().purgeData();
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.runTaskTimerAsynchronously(Main.getInstance(), 30L * 20L, 180L * 20L);
    }

    public void createTables() throws SQLException {
        final Statement cur = conn.createStatement();
        cur.execute("CREATE TABLE `player_diffs` (`row_id` INTEGER PRIMARY KEY AUTOINCREMENT, `player` VARCHAR(36) NOT NULL, `index` INT NOT NULL, `value` DOUBLE NOT NULL, `timestamp` INT NOT NULL)");
        cur.execute("CREATE TABLE `global_diffs` (`row_id` INTEGER PRIMARY KEY AUTOINCREMENT, `index` INT NOT NULL, `value` DOUBLE NOT NULL, `timestamp` INT NOT NULL)");
        cur.close();
    }

    public void purgeData() throws SQLException {
        final long oldTime = (System.currentTimeMillis() / 1000L) - (InfoShower.diffLastSeconds * 1000L);
        final PreparedStatement delStmt1 = conn.prepareStatement("DELETE FROM `player_diffs` WHERE timestamp < ?");
        delStmt1.setLong(1, oldTime);
        delStmt1.execute();
        delStmt1.close();
        final PreparedStatement delStmt2 = conn.prepareStatement("DELETE FROM `global_diffs` WHERE timestamp < ?");
        delStmt2.setLong(1, oldTime);
        delStmt2.execute();
        delStmt2.close();
    }

    public static MemoryDatabase getInstance() {
        return MemoryDatabase.instance;
    }

    public Connection getConn() {
        return conn;
    }

}
