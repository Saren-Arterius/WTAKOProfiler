package net.wtako.WTAKOProfiler.Schedulers;

import java.sql.SQLException;
import java.util.HashMap;

import net.wtako.WTAKOProfiler.Main;
import net.wtako.WTAKOProfiler.Methods.InfoShower;
import net.wtako.WTAKOProfiler.Utils.ExperienceManager;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

public class CheckScheduler {

    private static CheckScheduler                     instance          = null;
    private static HashMap<Player, Double>            playerHealths     = new HashMap<Player, Double>();
    private static HashMap<Player, Double>            playerFoods       = new HashMap<Player, Double>();
    private static HashMap<Player, ExperienceManager> playerEXPManagers = new HashMap<Player, ExperienceManager>();
    private static HashMap<Player, Double>            playerEXPs        = new HashMap<Player, Double>();
    private static HashMap<Player, Double>            playerBalances    = new HashMap<Player, Double>();
    private static final boolean[]                    notified          = new boolean[] {false, false};

    public CheckScheduler() {
        CheckScheduler.instance = this;
        Main.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    for (final Player player: Main.getInstance().getServer().getOnlinePlayers()) {
                        boolean hasChange = false;
                        if (checkHealth(player)) {
                            hasChange = true;
                        }
                        if (checkEcon(player)) {
                            hasChange = true;
                        }
                        if (checkFood(player)) {
                            hasChange = true;
                        }
                        if (checkEXP(player)) {
                            hasChange = true;
                        }
                        for (int i = 0; i < CheckScheduler.notified.length; i++) {
                            if (checkNotified(i)) {
                                hasChange = true;
                                break;
                            }
                        }
                        if (hasChange || player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null) {
                            InfoShower.getInfoShower(player).showInfo();
                        }
                    }
                } catch (final SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, 0L, Main.getInstance().getConfig().getLong("InfoBox.CheckerTicksInterval"));
    }

    private boolean checkHealth(Player player) throws SQLException {
        boolean hasChange = false;
        final double currentHealth = player.getHealth();
        if (!CheckScheduler.playerHealths.containsKey(player)
                || CheckScheduler.playerHealths.get(player) != currentHealth) {
            final double diff = CheckScheduler.playerHealths.get(player) != null ? currentHealth
                    - CheckScheduler.playerHealths.get(player) : 0;
            InfoShower.getInfoShower(player).addDiff(0, diff);
            hasChange = true;
        }
        CheckScheduler.playerHealths.put(player, currentHealth);
        return hasChange;
    }

    private boolean checkFood(Player player) throws SQLException {
        boolean hasChange = false;
        final double currentFood = player.getFoodLevel();
        if (!CheckScheduler.playerFoods.containsKey(player) || CheckScheduler.playerFoods.get(player) != currentFood) {
            final double diff = CheckScheduler.playerFoods.get(player) != null ? currentFood
                    - CheckScheduler.playerFoods.get(player) : 0;
            InfoShower.getInfoShower(player).addDiff(1, diff);
            hasChange = true;
        }
        CheckScheduler.playerFoods.put(player, currentFood);
        return hasChange;
    }

    private boolean checkEXP(Player player) throws SQLException {
        boolean hasChange = false;
        if (!CheckScheduler.playerEXPManagers.containsKey(player)) {
            CheckScheduler.playerEXPManagers.put(player, new ExperienceManager(player));
        }
        final double currentEXP = CheckScheduler.playerEXPManagers.get(player).getCurrentExp();
        if (!CheckScheduler.playerEXPs.containsKey(player) || CheckScheduler.playerEXPs.get(player) != currentEXP) {
            final double diff = CheckScheduler.playerEXPs.get(player) != null ? currentEXP
                    - CheckScheduler.playerEXPs.get(player) : 0;
            InfoShower.getInfoShower(player).addDiff(2, diff);
            hasChange = true;
        }
        CheckScheduler.playerEXPs.put(player, currentEXP);
        return hasChange;
    }

    private boolean checkEcon(Player player) throws SQLException {
        boolean hasChange = false;
        if (Main.economy == null) {
            return false;
        }
        final double currentBalance = Main.economy.getBalance(player);
        if (!CheckScheduler.playerBalances.containsKey(player)
                || CheckScheduler.playerBalances.get(player) != currentBalance) {
            final double diff = CheckScheduler.playerBalances.get(player) != null ? currentBalance
                    - CheckScheduler.playerBalances.get(player) : 0;
            InfoShower.getInfoShower(player).addDiff(3, diff);
            hasChange = true;
        }
        CheckScheduler.playerBalances.put(player, currentBalance);
        return hasChange;
    }

    private boolean checkNotified(int index) {
        if (CheckScheduler.notified[index]) {
            CheckScheduler.notified[index] = false;
            return true;
        }
        return false;
    }

    public static void notify(int index) {
        CheckScheduler.notified[index] = true;
    }

    public static ExperienceManager getEXPManager(Player player) {
        if (!CheckScheduler.playerEXPManagers.containsKey(player)) {
            CheckScheduler.playerEXPManagers.put(player, new ExperienceManager(player));
        }
        return CheckScheduler.playerEXPManagers.get(player);
    }

    public static void reload() {
        CheckScheduler.instance = null;
    }

    public static CheckScheduler getInstance() {
        return CheckScheduler.instance;
    }

}
