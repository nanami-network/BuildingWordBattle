package me.koutachan.buildingwordbattle.Game;

import me.koutachan.buildingwordbattle.Map.AreaCreator;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.PlayerData.impl.TeamEnum.TeamEnum;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public class Game {
    public static void startShuffle() {


        Map<UUID, Integer> shuffle = shuffleWhile();

        if (shuffle != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerData data = PlayerDataUtil.getPlayerData(player);

                if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

                    int mapID = shuffle.get(player.getUniqueId());

                    data.getMapManager().addMap(mapID);

                    AreaCreator areaCreator = CreateBox.areaCreatorMap.get(mapID + "-" + GameInfo.round);

                    World world = Bukkit.getWorld("world");

                    Vector vector = new Vector(areaCreator.getXMax(), areaCreator.getYMax(), areaCreator.getZMax()).getMidpoint(new Vector(areaCreator.getXMin(), areaCreator.getYMin(), areaCreator.getZMin()));

                    player.teleport(new Location(world, vector.getX(), vector.getY(), vector.getZ()));
                }
            }
        } else {
            //ゲーム終了
        }
    }

    private static Map<UUID, Integer> shuffleWhile() {
        while (true) {
            int temp = 0;

            Map<UUID, Integer> hashmap = new HashMap<>();

            List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
            Collections.shuffle(onlinePlayers);

            List<Integer> cloneMapList = new ArrayList<>(GameInfo.mapList);
            for (Player player : Bukkit.getOnlinePlayers()) {

                PlayerData data = PlayerDataUtil.getPlayerData(player);

                if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {


                    int foundNumber = 0;


                    List<Integer> playedMap = data.getMapManager().getMapList();

                    for (int i : cloneMapList) {
                        if (!playedMap.contains(i)) {
                            cloneMapList.remove((Integer) i);
                            foundNumber = i;
                            break;
                        }
                    }

                    if (foundNumber == 0) {
                        if (temp++ > 1) {
                            return null;
                        }
                    }

                    if (temp == 0) {
                        hashmap.put(player.getUniqueId(), foundNumber);
                    }
                }
            }
            if (temp == 0) {
                return hashmap;
            }
        }
    }
}