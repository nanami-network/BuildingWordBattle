package me.koutachan.buildingwordbattle.playerdata.impl;

import lombok.Getter;
import lombok.Setter;
import me.koutachan.buildingwordbattle.playerdata.PlayerData;

@Getter
@Setter
public class ThemeManager {
    private PlayerData data;
    private String theme;
    private int themeMap;

    public ThemeManager(PlayerData playerData) {
        this.data = playerData;
    }
}
