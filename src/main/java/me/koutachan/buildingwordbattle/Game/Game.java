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

                    switch (GameInfo.nowState) {
                        case BUILDING: {
                            areaCreator.setAuthor(player.getName());
                            areaCreator.setAuthorUUID(player.getUniqueId());
                            break;
                        }
                        case ANSWER: {
                            areaCreator.setAnswerPlayer(player.getName());
                            areaCreator.setAnswerUUID(player.getUniqueId());
                        }
                    }

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
        int a = 0;

        while (true) {
            int temp = 0;
            a++;

            //Bukkit.broadcastMessage("ループ回数: " + a);

            //a > 100回行くことはあり得ない、 もし運が悪かったらあるかも、> 1000 必要？
            if(a > 100) {
                return null;
            }

            Map<UUID, Integer> hashmap = new HashMap<>();

            List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
            Collections.shuffle(onlinePlayers);

            int players = (int) (PlayerDataUtil.getOnlinePlayers() / 1.5);

            List<Integer> cloneMapList = new ArrayList<>(GameInfo.mapList);
            for (Player player : onlinePlayers) {

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
                        if (temp++ >= players) {
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