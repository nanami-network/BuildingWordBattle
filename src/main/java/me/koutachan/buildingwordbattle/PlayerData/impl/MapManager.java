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
    private String whatMap, theme = "無し";
    private boolean breakable;
    private List<Integer> MapList = new ArrayList<>();

    public MapManager(PlayerData playerData) {
        this.data = playerData;
        this.whatMap = "NONE";
    }

    public void handle() {
        Location location = data.getPlayer().getLocation();

        whatMap = "NONE";
        breakable = false;

        for (AreaCreator areaCreator : CreateBox.areaCreatorMap.values()) {
            if (areaCreator.isArea(location)) {
                whatMap = areaCreator.getMapName();
                if(areaCreator.getAuthorUUID() == data.getPlayer().getUniqueId()) {
                    breakable = true;
                    theme = areaCreator.getTheme() != null ? areaCreator.getTheme() : "無し";
                }
                break;
            }
        }
    }

    public void addMap(int mapID) {
        MapList.add(mapID);
    }
}