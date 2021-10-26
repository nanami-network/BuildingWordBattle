package me.koutachan.buildingwordbattle.PlayerData.impl;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamManager {

    private TeamEnum currentTeam = TeamEnum.SPEC;
}

enum TeamEnum {
    PLAYER,
    ADMIN,
    SPEC
}