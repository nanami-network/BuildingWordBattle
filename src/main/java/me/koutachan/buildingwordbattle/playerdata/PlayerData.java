package me.koutachan.buildingwordbattle.playerdata;

import lombok.Getter;
import lombok.Setter;
import me.koutachan.buildingwordbattle.playerdata.impl.*;
import org.bukkit.entity.Player;

@Getter @Setter
public class PlayerData {

    public PlayerData(Player player) {
        this.player = player;
        this.scoreBoardManager = new ScoreBoardManager(this);
        this.mapManager = new MapManager(this);
        this.teamManager = new TeamManager(this);
        this.themeManager = new ThemeManager(this);
        this.quitManager = new QuitManager(this);
    }

    private final Player player;
    private ScoreBoardManager scoreBoardManager;
    private MapManager mapManager;
    private TeamManager teamManager;
    private ThemeManager themeManager;
    private QuitManager quitManager;
}