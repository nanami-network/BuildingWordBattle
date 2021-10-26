package me.koutachan.buildingwordbattle.PlayerData;

import lombok.Getter;
import me.koutachan.buildingwordbattle.PlayerData.impl.MapManager;
import me.koutachan.buildingwordbattle.PlayerData.impl.ScoreBoardManager;
import me.koutachan.buildingwordbattle.PlayerData.impl.TeamManager;
import me.koutachan.buildingwordbattle.PlayerData.impl.ThemeManager;
import org.bukkit.entity.Player;

@Getter
public class PlayerData {


    public PlayerData(Player player) {
        this.player = player;
        this.scoreBoardManager = new ScoreBoardManager(this);
        this.mapManager = new MapManager(this);
        this.teamManager = new TeamManager(this);
        this.themeManager = new ThemeManager(this);
    }

    private final Player player;
    private final ScoreBoardManager scoreBoardManager;
    private final MapManager mapManager;
    private final TeamManager teamManager;
    private final ThemeManager themeManager;
}