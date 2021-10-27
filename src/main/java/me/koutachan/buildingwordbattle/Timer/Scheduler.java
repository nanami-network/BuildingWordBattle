package me.koutachan.buildingwordbattle.Timer;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.ChatColorUtil;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.Game.GameInfo;
import me.koutachan.buildingwordbattle.Game.Theme;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.PlayerData.impl.TeamEnum.TeamEnum;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class Scheduler {

    private long systemTime;

    public static long serverLagSpike;

    public static int themeTime, themeCount;

    private BukkitTask bukkitTask;

    public void start() {
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(BuildingWordBattle.INSTANCE, () -> {

            long now = System.currentTimeMillis() - systemTime - 1000;

            if (now < 0) now = 0;

            serverLagSpike = now;

            this.systemTime = System.currentTimeMillis();

            playerDataUpdate();
            themeActionBar();
            updateBoard();
        }, 0, 20);
    }

    private void playerDataUpdate() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            data.getMapManager().handle();

        }
    }

    private void themeActionBar() {
        if (GameInfo.nowState == GameStateEnum.THEME) {

            themeCount = 0;

            if (themeTime <= 0) {
                Theme.startShuffle();
                return;
            }

            for (Player player : Bukkit.getOnlinePlayers()) {

                PlayerData data = PlayerDataUtil.getPlayerData(player);

                if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

                    String theme = data.getThemeManager().getTheme();

                    if (theme == null) {
                        theme = "お題を設定してください";
                        themeCount++;
                    }

                    theme = ChatColorUtil.translateAlternateColorCodes(" &6[お題] " + theme);

                    try {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(theme));
                    } catch (NoSuchMethodError e) {
                        data.getScoreBoardManager().getScoreboard().set(theme, 7);
                    }
                }
            }

            if (themeCount == 0) {
                Theme.startShuffle();
                return;
            }

            themeTime--;
        }
    }

    private void updateBoard() {
        ScoreBoard.handle();
    }

    public void stop() {
        if (bukkitTask != null) bukkitTask.cancel();
    }
}