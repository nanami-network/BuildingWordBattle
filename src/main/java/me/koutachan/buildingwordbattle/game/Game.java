package me.koutachan.buildingwordbattle.game;

import me.koutachan.buildingwordbattle.util.BuildingWordUtility;
import me.koutachan.buildingwordbattle.game.main.*;
import me.koutachan.buildingwordbattle.playerdata.PlayerDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Game {
    public static void run() {
        //1秒に1回実行される

        //ゲームシステム
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerDataUtil.getPlayerData(player).getMapManager().handle();
        }

        GameInfo.CALCULATE_MAX_ROUND = BuildingWordUtility.calculateMaxRound();
        GameInfo.CALCULATE_MAX_ROUND_SHOW = Math.max(GameInfo.CURRENT_ROUND, GameInfo.CALCULATE_MAX_ROUND);

        switch (GameInfo.gameState) {
            case THEME:
                Theme.run();
                break;
            case BUILDING:
                Build.run();
                break;
            case ANSWER:
                Answer.run();
                break;
        }
    }
}