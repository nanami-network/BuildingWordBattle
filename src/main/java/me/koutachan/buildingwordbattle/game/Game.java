package me.koutachan.buildingwordbattle.game;

import me.koutachan.buildingwordbattle.game.system.*;

public class Game {
    public static void run() {
        //1秒に1回実行される

        //ゲームシステム
        GameSystem.run();

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