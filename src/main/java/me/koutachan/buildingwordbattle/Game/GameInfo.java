package me.koutachan.buildingwordbattle.Game;

import me.koutachan.buildingwordbattle.Game.GameEnum.GameEnum;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;

public class GameInfo {
    public static int round, maxRound;

    public static GameEnum gameState = GameEnum.LOBBY;

    public static GameStateEnum nowState = GameStateEnum.NULL;
}
