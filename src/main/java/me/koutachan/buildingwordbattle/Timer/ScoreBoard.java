package me.koutachan.buildingwordbattle.Timer;

import me.koutachan.buildingwordbattle.Utilities.ChatColorUtility;
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
                if (GameInfo.nowState == GameStateEnum.NULL) onDefault();
                if (GameInfo.nowState == GameStateEnum.THEME) onGameTHEME();
                if (GameInfo.nowState == GameStateEnum.BUILDING) onGameBUILDING();
                if (GameInfo.nowState == GameStateEnum.ANSWER) onGameANSWER();
        }
    }

    private static void onDefault() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            data.getScoreBoardManager().getScoreboard().setAll(
                    ChatColorUtility.translateAlternateColorCodes("&1"),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&c 現在のラグ: %sms", Scheduler.serverLagSpike)),
                    ChatColorUtility.translateAlternateColorCodes("&2"),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&e ≫ &l%s", translate(GameInfo.gameState))),
                    ChatColorUtility.translateAlternateColorCodes("&3"),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&a チーム: &l%s", data.getTeamManager().getCurrentTeam())),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&b オンライン数: &l%s", Bukkit.getOnlinePlayers().size())),
                    ChatColorUtility.translateAlternateColorCodes("&4"),
                    ChatColorUtility.translateAlternateColorCodes("&6 一言お知らせ: 何もないよ"),
                    ChatColorUtility.translateAlternateColorCodes("&b @credit 7mi_chan")
            );
        }
    }

    private static void onGameTHEME() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            data.getScoreBoardManager().getScoreboard().setAll(
                    ChatColorUtility.translateAlternateColorCodes("&1"),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&c > 現在のラグ: %sms", Scheduler.serverLagSpike)),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&e > 残り時間: %s", Scheduler.themeTime)),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&e > 残り人数: %s", Scheduler.themeCount)),
                    ChatColorUtility.translateAlternateColorCodes("&3"),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&a チーム: &l%s", data.getTeamManager().getCurrentTeam())),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&b オンライン数: &l%s", Bukkit.getOnlinePlayers().size())),
                    ChatColorUtility.translateAlternateColorCodes("&4"),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&eDEBUG: breakable? %s", data.getMapManager().isBreakable())),
                    ChatColorUtility.translateAlternateColorCodes("&5")
            );
        }
    }

    private static void onGameBUILDING() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            data.getScoreBoardManager().getScoreboard().setAll(
                    ChatColorUtility.translateAlternateColorCodes("&1"),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&c 現在のラグ: %sms", Scheduler.serverLagSpike)),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&6 残り時間: %s", Scheduler.buildingTime)),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&e ≫ &l%s", String.format("ラウンド: %s/%s", GameInfo.round, GameInfo.maxRound))),
                    ChatColorUtility.translateAlternateColorCodes("&2"),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&a チーム: &l%s", data.getTeamManager().getCurrentTeam())),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&b オンライン数: &l%s", Bukkit.getOnlinePlayers().size())),
                    ChatColorUtility.translateAlternateColorCodes("&3"),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&e お題: %s", data.getMapManager().getTheme())),
                    ChatColorUtility.translateAlternateColorCodes("&4")
            );
        }
    }

    private static void onGameANSWER() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            data.getScoreBoardManager().getScoreboard().setAll(
                    ChatColorUtility.translateAlternateColorCodes("&1"),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&c 現在のラグ: %sms", Scheduler.serverLagSpike)),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&6 残り時間: %s", Scheduler.answerTime)),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&e ≫ &l%s", String.format("ラウンド: %s/%s", GameInfo.buildRound, GameInfo.maxRound))),
                    ChatColorUtility.translateAlternateColorCodes("&3"),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&a チーム: &l%s", data.getTeamManager().getCurrentTeam())),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&b オンライン数: &l%s", Bukkit.getOnlinePlayers().size())),
                    ChatColorUtility.translateAlternateColorCodes("&4"),
                    ChatColorUtility.translateAlternateColorCodes(String.format("&eDEBUG: breakable? %s", data.getMapManager().isBreakable())),
                    ChatColorUtility.translateAlternateColorCodes("&5")
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