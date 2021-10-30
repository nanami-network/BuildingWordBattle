package me.koutachan.buildingwordbattle.PlayerData.impl;

import lombok.Getter;
import lombok.Setter;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;

@Getter
@Setter
public class AnswerManager {
    private final PlayerData data;
    private String answer;

    public AnswerManager(PlayerData playerData) {
        this.data = playerData;
    }
}
