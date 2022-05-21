package me.koutachan.buildingwordbattle.playerdata.impl;

import lombok.Getter;
import lombok.Setter;
import me.koutachan.buildingwordbattle.game.GameInfo;
import me.koutachan.buildingwordbattle.game.enums.GameStateEnum;
import me.koutachan.buildingwordbattle.game.main.Spectator;
import me.koutachan.buildingwordbattle.map.AreaCreator;
import me.koutachan.buildingwordbattle.playerdata.PlayerData;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MapManager {
    private final PlayerData data;
    //MapId = 現在のid // MapRound = 現在のラウンド
    private String answerMapName, theme, mapName;
    private boolean breakable;

    private List<Integer> MapList = new ArrayList<>();
    private int lastMapID;

    public MapManager(PlayerData playerData) {
        this.data = playerData;
    }

    public void handle() {
        Location location = data.getPlayer().getLocation();

        mapName = "NONE";
        theme = "無し";
        breakable = false;

        //spec の実装待ち
        List<Integer> mapList = GameInfo.gameState == GameStateEnum.SPEC ? Spectator.CLONED_MAP_LIST : GameInfo.CURRENT_MAP_LIST;

        for (int map : mapList) {
            for (int i = GameInfo.CURRENT_BUILD_ROUND; i > 0; i--) {
                AreaCreator areaCreator = GameInfo.areaCreator.get(map + "-" + i);

                if (areaCreator != null && areaCreator.isArea(location)) {
                    mapName = areaCreator.getMapName();

                    theme = areaCreator.getTheme();

                    break;
                }
            }
        }
    }

    public void addPlayedMap(int mapID) {
        MapList.add(mapID);
        lastMapID = mapID;
    }
}