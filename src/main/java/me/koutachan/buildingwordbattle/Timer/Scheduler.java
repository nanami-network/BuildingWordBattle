package me.koutachan.buildingwordbattle.Timer;

import lombok.Getter;
import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.ChatColorUtil;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@Getter
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
                    "",
                    ChatColorUtil.translateAlternateColorCodes(String.format("&c 現在のラグ: %sms", serverLagSpike)),
                    ""
            );

        }
    }

    public void stop() {
        if (bukkitTask != null) bukkitTask.cancel();
    }
}