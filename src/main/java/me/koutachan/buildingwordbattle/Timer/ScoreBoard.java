package me.koutachan.buildingwordbattle.Timer;

import me.koutachan.buildingwordbattle.ChatColorUtil;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameEnum;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.Game.GameInfo;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ScoreBoard {
    public static void handle() {
        switch (GameInfo.gameState) {
            case STARTING:
            case LOBBY:
            case END:
                onDefault();
                break;
            case GAME:
                if (GameInfo.nowState == GameStateEnum.THEME) onGameTHEME();
                if (GameInfo.nowState == GameStateEnum.BUILDING) onGameBUILDING();
                if (GameInfo.nowState == GameStateEnum.ANSWER) onGameANSWER();
        }
    }


    private static void onDefault() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            data.getScoreBoardManager().getScoreboard().setAll(
                    ChatColorUtil.translateAlternateColorCodes("&1"),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&c 現在のラグ: %sms", Scheduler.serverLagSpike)),
                    ChatColorUtil.translateAlternateColorCodes("&2"),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&e ≫ &l%s", translate(GameInfo.gameState))),
                    ChatColorUtil.translateAlternateColorCodes("&3"),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&a チーム: &l%s", data.getTeamManager().getCurrentTeam())),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&b オンライン数: &l%s", Bukkit.getOnlinePlayers().size())),
                    ChatColorUtil.translateAlternateColorCodes("&4"),
                    ChatColorUtil.translateAlternateColorCodes("&6 一言お知らせ: 何もないよ"),
                    ChatColorUtil.translateAlternateColorCodes("&b @credit 7mi_chan")
            );
        }
    }

    private static void onGameTHEME() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            data.getScoreBoardManager().getScoreboard().setAll(
                    ChatColorUtil.translateAlternateColorCodes("&1"),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&c > 現在のラグ: %sms", Scheduler.serverLagSpike)),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&e > 残り時間: %s", Scheduler.themeTime)),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&e > 残り人数: %s", Scheduler.themeCount)),
                    ChatColorUtil.translateAlternateColorCodes("&3"),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&a チーム: &l%s", data.getTeamManager().getCurrentTeam())),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&b オンライン数: &l%s", Bukkit.getOnlinePlayers().size())),
                    ChatColorUtil.translateAlternateColorCodes("&4"),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&eDEBUG: breakable? %s", data.getMapManager().isBreakable())),
                    ChatColorUtil.translateAlternateColorCodes("&5")
            );
        }
    }

    private static void onGameBUILDING() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            data.getScoreBoardManager().getScoreboard().setAll(
                    ChatColorUtil.translateAlternateColorCodes("&1"),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&c 現在のラグ: %sms", Scheduler.serverLagSpike)),
                    ChatColorUtil.translateAlternateColorCodes("&2"),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&e ≫ &l%s", String.format("ラウンド: %s/%s", GameInfo.round, GameInfo.maxRound))),
                    ChatColorUtil.translateAlternateColorCodes("&3"),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&a チーム: &l%s", data.getTeamManager().getCurrentTeam())),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&b オンライン数: &l%s", Bukkit.getOnlinePlayers().size())),
                    ChatColorUtil.translateAlternateColorCodes("&4"),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&eDEBUG: breakable? %s", data.getMapManager().isBreakable())),
                    ChatColorUtil.translateAlternateColorCodes("&5")
            );
        }
    }


    private static void onGameANSWER() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            data.getScoreBoardManager().getScoreboard().setAll(
                    ChatColorUtil.translateAlternateColorCodes("&1"),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&c 現在のラグ: %sms", Scheduler.serverLagSpike)),
                    ChatColorUtil.translateAlternateColorCodes("&2"),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&e ≫ &l%s", String.format("ラウンド: %s/%s", GameInfo.round, GameInfo.maxRound))),
                    ChatColorUtil.translateAlternateColorCodes("&3"),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&a チーム: &l%s", data.getTeamManager().getCurrentTeam())),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&b オンライン数: &l%s", Bukkit.getOnlinePlayers().size())),
                    ChatColorUtil.translateAlternateColorCodes("&4"),
                    ChatColorUtil.translateAlternateColorCodes(String.format("&eDEBUG: breakable? %s", data.getMapManager().isBreakable())),
                    ChatColorUtil.translateAlternateColorCodes("&5")
            );
        }
    }

    private static String translate(GameEnum gameEnum) {
        switch (gameEnum) {
            case LOBBY:
                return "ゲーム開始待機中";
            case STARTING:
                return "ゲーム開始中";
            case END:
                return "ゲーム終了！";
        }
        return null;
    }
}