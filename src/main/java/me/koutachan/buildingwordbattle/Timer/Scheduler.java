package me.koutachan.buildingwordbattle.Timer;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.ChatColorUtil;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class Scheduler {

    private long systemTime, serverLagSpike;

    private BukkitTask bukkitTask;

    public void start() {
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(BuildingWordBattle.INSTANCE, () -> {

            long now = System.currentTimeMillis() - systemTime - 1000;

            Bukkit.broadcastMessage(String.valueOf(now));

            if (now < 0) now = 0;

            this.serverLagSpike = now;

            this.systemTime = System.currentTimeMillis();

            updateBoard();
        }, 0, 20);
    }

    private void updateBoard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = PlayerDataUtil.getPlayerData(player);

            data.getScoreBoardManager().getScoreboard().setAll(
                    ChatColorUtil.translateAlternateColorCodes("&1"),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&c 現在のラグ: %sms", serverLagSpike)),
                    ChatColorUtil.translateAlternateColorCodes("&2"),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&a チーム: &l%s", data.getTeamManager().getCurrentTeam())),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&b マップ: &l%s", data.getMapManager().getWhatMap()))
            );

        }
    }

    public void stop() {
        if (bukkitTask != null) bukkitTask.cancel();
    }
}