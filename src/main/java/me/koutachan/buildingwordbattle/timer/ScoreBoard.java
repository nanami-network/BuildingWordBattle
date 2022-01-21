package me.koutachan.buildingwordbattle.timer;

import me.koutachan.buildingwordbattle.game.GameInfo;
import me.koutachan.buildingwordbattle.game.gameEnum.GameEnum;
import me.koutachan.buildingwordbattle.game.system.Answer;
import me.koutachan.buildingwordbattle.game.system.Build;
import me.koutachan.buildingwordbattle.game.system.Theme;
import me.koutachan.buildingwordbattle.map.AreaCreator;
import me.koutachan.buildingwordbattle.playerdata.PlayerData;
import me.koutachan.buildingwordbattle.playerdata.PlayerDataUtil;
import me.koutachan.buildingwordbattle.util.ConfigUtil;
import me.koutachan.buildingwordbattle.util.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

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

            translate(data, "SCOREBOARD.DEFAULT");
        }
    }

    private static void onGameTHEME() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            translate(data, "SCOREBOARD.THEME");
        }
    }

    private static void onGameBUILDING() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            translate(data, "SCOREBOARD.BUILD");
        }
    }

    private static void onGameANSWER() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            translate(data, "SCOREBOARD.ANSWER");
        }
    }

    private static String translate(GameEnum gameEnum) {
        switch (gameEnum) {
            case LOBBY:
                return MessageManager.getString("GAME-STATE.LOBBY");
            case STARTING:
                return MessageManager.getString("GAME-STATE.STARTING");
            case GAME:
                return MessageManager.getString("GAME-STATE.GAME");
            case END:
                return MessageManager.getString("GAME-STATE.END");
        }
        return null;
    }

    private static void translate(PlayerData data, String messageID) {
        AreaCreator areaCreator = GameInfo.areaCreator.get(data.getMapManager().getAnswerMapName());

        List<String> stringList = ConfigUtil.replaceList(messageID,
                "%lag%|" + Scheduler.serverLagSpike,
                "%game-state%|" + translate(GameInfo.gameInfo),
                "%team%|" + data.getTeamManager().getCurrentTeam().toString(),
                "%online-player%|" + Bukkit.getOnlinePlayers().size(),
                "%theme-time%|" + Theme.time,
                "%build-time%|" + Build.time,
                "%answer-time%|" + Answer.time,
                "%count%|" + Theme.playerCount,
                "%theme%|" + (data.getThemeManager().getTheme() != null ? data.getThemeManager().getTheme() : MessageManager.getString("GAME.NOT-SET")),
                "%theme-build%|" + (data.getMapManager().getTheme() != null ? data.getMapManager().getTheme() : MessageManager.getString("GAME.NOT-SET")),
                "%answer%|" + (areaCreator != null && areaCreator.getAnswer() != null ? areaCreator.getAnswer() : MessageManager.getString("GAME.ANSWER-NOT-SET")),
                "%round%|" + GameInfo.CURRENT_ROUND,
                "%max-round|" + GameInfo.CALCULATE_MAX_ROUND_SHOW);

        for (int i = stringList.size(); i > 0; i--) {
            data.getScoreBoardManager().getScoreboard().set(stringList.get(stringList.size() - i), i);
        }
    }
}