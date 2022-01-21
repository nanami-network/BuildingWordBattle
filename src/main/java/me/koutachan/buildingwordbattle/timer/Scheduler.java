package me.koutachan.buildingwordbattle.timer;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class Scheduler {

    private long systemTime;

    public static long serverLagSpike;
    private BukkitTask bukkitTask;

    public void start() {
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(BuildingWordBattle.INSTANCE, () -> {

            long now = System.currentTimeMillis() - systemTime - 1000;

            if (now < 0) now = 0;

            serverLagSpike = now;

            this.systemTime = System.currentTimeMillis();

            Game.run();
            updateBoard();
        }, 0, 20);
    }

    private void updateBoard() {
        ScoreBoard.handle();
    }

    public void stop() {
        if (bukkitTask != null) bukkitTask.cancel();
    }
}