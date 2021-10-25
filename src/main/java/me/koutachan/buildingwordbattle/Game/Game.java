package me.koutachan.buildingwordbattle.Game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Game {
    private GameEnum gameState = GameEnum.LOBBY;
}