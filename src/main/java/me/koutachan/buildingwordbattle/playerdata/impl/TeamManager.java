package me.koutachan.buildingwordbattle.playerdata.impl;

import lombok.Getter;
import lombok.Setter;
import me.koutachan.buildingwordbattle.playerdata.PlayerData;
import me.koutachan.buildingwordbattle.playerdata.impl.Enum.TeamEnum;

@Getter
@Setter
public class TeamManager {

    private final PlayerData data;

    public TeamManager(PlayerData playerData) {
        this.data = playerData;
    }

    private TeamEnum currentTeam = TeamEnum.SPEC;
    //ストリーマー用
    private boolean isStreamer = false;
}