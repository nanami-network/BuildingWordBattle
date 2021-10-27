package me.koutachan.buildingwordbattle.Timer;

import fr.minuskube.netherboard.Netherboard;
import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.ChatColorUtil;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameEnum;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.Game.GameInfo;
import me.koutachan.buildingwordbattle.Game.Theme;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.PlayerData.impl.TeamEnum.TeamEnum;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.UnhandledException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class Scheduler {

    private long systemTime, serverLagSpike;

    public static int themeTime;

    private BukkitTask bukkitTask;

    public void start() {
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(BuildingWordBattle.INSTANCE, () -> {

            long now = System.currentTimeMillis() - systemTime - 1000;

            if (now < 0) now = 0;

            this.serverLagSpike = now;

            this.systemTime = System.currentTimeMillis();

            playerDataUpdate();

            updateBoard();
            themeActionBar();
        }, 0, 20);
    }

    private void playerDataUpdate() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            data.getMapManager().handle();

        }
    }

    private void themeActionBar() {

        int count = 0;

        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            if (GameInfo.nowState == GameStateEnum.THEME && data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {


                if (themeTime <= 0) {
                    Theme.startShuffle();
                    return;
                }

                String theme = data.getThemeManager().getTheme();

                if (theme == null) {
                    theme = "お題を設定してください";
                    count++;
                }

                if (count == 0) {
                    Theme.startShuffle();
                    return;
                }


                theme = ChatColorUtil.translateAlternateColorCodes(" &6[お題] " + theme);

                data.getScoreBoardManager().getScoreboard().set(ChatColorUtil.translateAlternateColorCodes("&e > 残り時間: " + themeTime), 9);
                data.getScoreBoardManager().getScoreboard().set(ChatColorUtil.translateAlternateColorCodes("&e > 残り人数: " + count), 8);

                try {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(theme));
                } catch (NoSuchMethodError e) {
                    data.getScoreBoardManager().getScoreboard().set(theme, 7);
                }

                themeTime--;
            }
        }
    }


    private void updateBoard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = PlayerDataUtil.getPlayerData(player);

            try {
                data.getScoreBoardManager().getScoreboard().setAll(
                        ChatColorUtil.translateAlternateColorCodes("&1"),
                        ChatColorUtil.translateAlternateColorCodes(String.format("&c 現在のラグ: %sms", serverLagSpike)),
                        ChatColorUtil.translateAlternateColorCodes("&2"),
                        ChatColorUtil.translateAlternateColorCodes(String.format("&e ≫ &l%s", translate(GameInfo.gameState))),
                        ChatColorUtil.translateAlternateColorCodes("&3"),
                        ChatColorUtil.translateAlternateColorCodes(String.format("&a チーム: &l%s", data.getTeamManager().getCurrentTeam())),
                        ChatColorUtil.translateAlternateColorCodes(String.format("&b オンライン数: &l%s", Bukkit.getOnlinePlayers().size())),
                        //ChatColorUtil.translateAlternateColorCodes("&4"),
                        ChatColorUtil.translateAlternateColorCodes("&5"),
                        ChatColorUtil.translateAlternateColorCodes(String.format("&eDEBUG: breakable? %s", data.getMapManager().isBreakable())),
                        ChatColorUtil.translateAlternateColorCodes("&6")
                );
            } catch (UnhandledException | IllegalStateException e) {
                data.getScoreBoardManager().setScoreboard(Netherboard.instance().createBoard(data.getPlayer(), ChatColor.YELLOW + "BuildingWordBattle"));
                updateBoard();
            }
        }
    }

    private String translate(GameEnum gameEnum) {
        switch (gameEnum) {
            case LOBBY:
                return "ゲーム開始待機中";
            case STARTING:
                return "ゲーム開始中";
            case GAME:
                return String.format("ラウンド: %s/%s", GameInfo.round, GameInfo.maxRound);
            case END:
                return "ゲーム終了！";
        }
        return null;
    }

    public void stop() {
        if (bukkitTask != null) bukkitTask.cancel();
    }
}