package me.koutachan.buildingwordbattle.playerdata.impl;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import lombok.Getter;
import lombok.Setter;
import me.koutachan.buildingwordbattle.playerdata.PlayerData;
import org.bukkit.ChatColor;

@Getter
@Setter
public class ScoreBoardManager {

    private PlayerData data;
    private BPlayerBoard scoreboard;

    public ScoreBoardManager(PlayerData playerData) {
        this.data = playerData;
        this.scoreboard = Netherboard.instance().createBoard(data.getPlayer(), ChatColor.YELLOW + "BuildingWordBattle");
    }
}