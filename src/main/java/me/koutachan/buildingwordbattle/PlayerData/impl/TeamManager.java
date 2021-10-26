package me.koutachan.buildingwordbattle.PlayerData.impl;

import lombok.Getter;
import lombok.Setter;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.impl.TeamEnum.TeamEnum;

@Getter
@Setter
public class TeamManager {

    private PlayerData data;

    public TeamManager(PlayerData playerData) {
        this.data = playerData;
    }

    private TeamEnum currentTeam = TeamEnum.SPEC;
}

