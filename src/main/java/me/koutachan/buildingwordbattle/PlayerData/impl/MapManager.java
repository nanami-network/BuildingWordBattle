package me.koutachan.buildingwordbattle.PlayerData.impl;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import lombok.Getter;
import lombok.Setter;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MapManager {
    private final PlayerData data;
    private String whatMap;
    private List<Integer> MapList = new ArrayList<>();

    public MapManager(PlayerData playerData) {
        this.data = playerData;
        this.whatMap = "NONE";
    }
}