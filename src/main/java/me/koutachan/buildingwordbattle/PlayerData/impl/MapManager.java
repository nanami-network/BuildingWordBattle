package me.koutachan.buildingwordbattle.PlayerData.impl;

import lombok.Getter;
import lombok.Setter;
import me.koutachan.buildingwordbattle.Game.CreateBox;
import me.koutachan.buildingwordbattle.Map.AreaCreator;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MapManager {
    private final PlayerData data;
    private String whatMap;
    private boolean breakable;
    private List<Integer> MapList = new ArrayList<>();

    public MapManager(PlayerData playerData) {
        this.data = playerData;
        this.whatMap = "NONE";
    }

    public void handle() {
        Location location = data.getPlayer().getLocation();

        for (AreaCreator areaCreator : CreateBox.areaCreatorMap.values()) {
            if (areaCreator.isArea(location)) {
                whatMap = areaCreator.getMapName();
                breakable = areaCreator.getAuthorUUID() == data.getPlayer().getUniqueId();
                break;
            } else {
                whatMap = "NONE";
                breakable = false;
            }
        }
    }
}