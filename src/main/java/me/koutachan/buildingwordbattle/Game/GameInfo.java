package me.koutachan.buildingwordbattle.Game;

import me.koutachan.buildingwordbattle.Game.GameEnum.GameEnum;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;

import java.util.ArrayList;
import java.util.List;

public class GameInfo {
    public static int round, maxRound, mapListSize = 0;

    public static GameEnum gameState = GameEnum.LOBBY;

    public static GameStateEnum nowState = GameStateEnum.NULL;

    public static List<Integer> mapList = new ArrayList<>();
}
