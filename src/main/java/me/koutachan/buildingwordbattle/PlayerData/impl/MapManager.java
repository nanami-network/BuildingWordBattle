package me.koutachan.buildingwordbattle.PlayerData.impl;

import lombok.Getter;
import lombok.Setter;
import me.koutachan.buildingwordbattle.Game.CreateBox;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.Map.AreaCreator;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MapManager {
    private final PlayerData data;
    private String MapName, theme;
    private boolean breakable;
    private List<Integer> MapList = new ArrayList<>();
    private int lastMapID;

    public MapManager(PlayerData playerData) {
        this.data = playerData;
    }

    public void handle() {
        Location location = data.getPlayer().getLocation();

        MapName = "NONE";
        theme = "無し";
        breakable = false;

        List<Integer> mapList = GameInfo.nowState == GameStateEnum.SPEC ? Spec.clonedMapList : GameInfo.mapList;

        for (int map : mapList) {
            AreaCreator areaCreator = CreateBox.areaCreatorMap.get(map + "-" + GameInfo.buildRound);

            if (areaCreator != null && areaCreator.isArea(location)) {
                MapName = areaCreator.getMapName();

                if (areaCreator.getAuthorUUID() == data.getPlayer().getUniqueId()) {
                    breakable = true;
                    theme = areaCreator.getTheme();
                }
                break;
            }
        }
    }

    public void addMap(int mapID) {
        MapList.add(mapID);
        lastMapID = mapID;
    }
}