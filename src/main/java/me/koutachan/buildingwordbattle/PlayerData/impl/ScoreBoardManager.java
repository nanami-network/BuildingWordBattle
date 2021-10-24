package me.koutachan.buildingwordbattle.PlayerData.impl;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import lombok.Getter;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Getter
public class ScoreBoardManager {

    private final PlayerData data;
    private final BPlayerBoard scoreboard;

    public ScoreBoardManager(PlayerData playerData) {
        this.data = playerData;
        this.scoreboard = Netherboard.instance().createBoard(data.getPlayer(), ChatColor.YELLOW + "BuildingWorldBattle");
    }
}
