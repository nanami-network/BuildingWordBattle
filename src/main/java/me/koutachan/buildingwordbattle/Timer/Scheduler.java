package me.koutachan.buildingwordbattle.Timer;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.PlayerData.impl.Enum.TeamEnum;
import me.koutachan.buildingwordbattle.Utilities.ChatUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class Scheduler {

    private long systemTime;

    public static long serverLagSpike;

    public static int themeTime, themeCount, buildingTime, answerTime;

    private BukkitTask bukkitTask;

    public void start() {
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(BuildingWordBattle.INSTANCE, () -> {

            long now = System.currentTimeMillis() - systemTime - 1000;

            if (now < 0) now = 0;

            serverLagSpike = now;

            this.systemTime = System.currentTimeMillis();

            GameInfo.maxRound = Game.getMaxRound();

            playerDataUpdate();
            updateBoard();
        }, 0, 20);
    }

    private void playerDataUpdate() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            data.getMapManager().handle();

        }
    }


    private void updateBoard() {
        ScoreBoard.handle();
    }

    public void stop() {
        if (bukkitTask != null) bukkitTask.cancel();
    }
}