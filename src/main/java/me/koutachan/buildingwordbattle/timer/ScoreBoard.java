package me.koutachan.buildingwordbattle.timer;

import me.koutachan.buildingwordbattle.game.GameInfo;
import me.koutachan.buildingwordbattle.game.gameEnum.GameEnum;
import me.koutachan.buildingwordbattle.game.system.Answer;
import me.koutachan.buildingwordbattle.game.system.Build;
import me.koutachan.buildingwordbattle.game.system.Theme;
import me.koutachan.buildingwordbattle.map.AreaCreator;
import me.koutachan.buildingwordbattle.playerdata.PlayerData;
import me.koutachan.buildingwordbattle.playerdata.PlayerDataUtil;
import me.koutachan.buildingwordbattle.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ScoreBoard {
    public static void handle() {
        switch (GameInfo.gameInfo) {
            case STARTING:
            case LOBBY:
            case END:
                onDefault();
                break;
            case GAME:
                switch (GameInfo.gameState) {
                    case NULL:
                    case SPEC:
                        onDefault();
                        break;
                    case THEME:
                        onGameTHEME();
                        break;
                    case ANSWER:
                        onGameANSWER();
                        break;
                    case BUILDING:
                        onGameBUILDING();
                        break;
                }
        }
    }

    private static void onDefault() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            data.getScoreBoardManager().getScoreboard().setAll(
                    ChatUtil.translateAlternateColorCodes("&1"),
                    ChatUtil.translateAlternateColorCodes(String.format("&c 現在のラグ: %sms", Scheduler.serverLagSpike)),
                    ChatUtil.translateAlternateColorCodes("&2"),
                    ChatUtil.translateAlternateColorCodes(String.format("&e ≫ &l%s", translate(GameInfo.gameInfo))),
                    ChatUtil.translateAlternateColorCodes("&3"),
                    ChatUtil.translateAlternateColorCodes(String.format("&a チーム: &l%s", data.getTeamManager().getCurrentTeam())),
                    ChatUtil.translateAlternateColorCodes(String.format("&b オンライン数: &l%s", Bukkit.getOnlinePlayers().size())),
                    ChatUtil.translateAlternateColorCodes("&4"),
                    ChatUtil.translateAlternateColorCodes("&6 一言お知らせ: 何もないよ"),
                    ChatUtil.translateAlternateColorCodes("&b @credit 7mi_chan")
            );
        }
    }

    private static void onGameTHEME() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            data.getScoreBoardManager().getScoreboard().setAll(
                    ChatUtil.translateAlternateColorCodes("&1"),
                    ChatUtil.translateAlternateColorCodes(String.format("&c > 現在のラグ: %sms", Scheduler.serverLagSpike)),
                    ChatUtil.translateAlternateColorCodes(String.format("&e > 残り時間: %s", Theme.time)),
                    ChatUtil.translateAlternateColorCodes(String.format("&e > 残り人数: %s", Theme.playerCount)),
                    ChatUtil.translateAlternateColorCodes("&3"),
                    ChatUtil.translateAlternateColorCodes(String.format("&a チーム: &l%s", data.getTeamManager().getCurrentTeam())),
                    ChatUtil.translateAlternateColorCodes(String.format("&b オンライン数: &l%s", Bukkit.getOnlinePlayers().size())),
                    ChatUtil.translateAlternateColorCodes("&4"),
                    ChatUtil.translateAlternateColorCodes(String.format("&6 お題: %s", data.getThemeManager().getTheme() != null ? data.getThemeManager().getTheme() : "未設定")),
                    ChatUtil.translateAlternateColorCodes("&5")
            );
        }
    }

    private static void onGameBUILDING() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            data.getScoreBoardManager().getScoreboard().setAll(
                    ChatUtil.translateAlternateColorCodes("&1"),
                    ChatUtil.translateAlternateColorCodes(String.format("&c 現在のラグ: %sms", Scheduler.serverLagSpike)),
                    ChatUtil.translateAlternateColorCodes(String.format("&6 残り時間: %s", Build.time)),
                    ChatUtil.translateAlternateColorCodes(String.format("&e ≫ &l%s", String.format("ラウンド: %s/%s", GameInfo.CURRENT_ROUND, GameInfo.CALCULATE_MAX_ROUND_SHOW))),
                    ChatUtil.translateAlternateColorCodes("&2"),
                    ChatUtil.translateAlternateColorCodes(String.format("&a チーム: &l%s", data.getTeamManager().getCurrentTeam())),
                    ChatUtil.translateAlternateColorCodes(String.format("&b オンライン数: &l%s", Bukkit.getOnlinePlayers().size())),
                    ChatUtil.translateAlternateColorCodes("&3"),
                    ChatUtil.translateAlternateColorCodes(String.format("&e お題: %s", data.getMapManager().getTheme() != null ? data.getMapManager().getTheme() : "未設定")),
                    ChatUtil.translateAlternateColorCodes("&4")
            );
        }
    }

    private static void onGameANSWER() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            AreaCreator areaCreator = GameInfo.areaCreator.get(data.getMapManager().getAnswerMapName());

            String answer = areaCreator.getAnswer() != null ? areaCreator.getAnswer() : "回答無し";

            data.getScoreBoardManager().getScoreboard().setAll(
                    ChatUtil.translateAlternateColorCodes("&1"),
                    ChatUtil.translateAlternateColorCodes(String.format("&c 現在のラグ: %sms", Scheduler.serverLagSpike)),
                    ChatUtil.translateAlternateColorCodes(String.format("&6 残り時間: %s", Answer.time)),
                    ChatUtil.translateAlternateColorCodes(String.format("&e ≫ &l%s", String.format("ラウンド: %s/%s", GameInfo.CURRENT_ROUND, GameInfo.CALCULATE_MAX_ROUND_SHOW))),
                    ChatUtil.translateAlternateColorCodes("&3"),
                    ChatUtil.translateAlternateColorCodes(String.format("&a チーム: &l%s", data.getTeamManager().getCurrentTeam())),
                    ChatUtil.translateAlternateColorCodes(String.format("&b オンライン数: &l%s", Bukkit.getOnlinePlayers().size())),
                    ChatUtil.translateAlternateColorCodes("&4"),
                    ChatUtil.translateAlternateColorCodes(String.format("&e 回答: %s", answer)),
                    ChatUtil.translateAlternateColorCodes("&5")
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
            case GAME:
                return "ゲーム中！";
        }
        return null;
    }
}