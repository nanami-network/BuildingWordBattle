package me.koutachan.buildingwordbattle.PlayerData.impl;

import lombok.Getter;
import lombok.Setter;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;

@Getter
@Setter
public class ThemeManager {
    private final PlayerData data;
    private String theme;

    public ThemeManager(PlayerData playerData) {
        this.data = playerData;
    }
}
