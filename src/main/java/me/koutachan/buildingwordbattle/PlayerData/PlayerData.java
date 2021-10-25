package me.koutachan.buildingwordbattle.PlayerData;

import lombok.Getter;
import me.koutachan.buildingwordbattle.PlayerData.impl.ScoreBoardManager;
import org.bukkit.entity.Player;

@Getter
public class PlayerData {


    public PlayerData(Player player) {
        this.player = player;
        this.scoreBoardManager = new ScoreBoardManager(this);
    }

    private final Player player;
    private final ScoreBoardManager scoreBoardManager;
}