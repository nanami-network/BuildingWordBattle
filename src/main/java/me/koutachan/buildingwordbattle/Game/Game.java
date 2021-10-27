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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    public static void startShuffle() {

        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(onlinePlayers);

        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {
                int foundNumber = 0;

                List<Integer> cloneMapList = new ArrayList<>(GameInfo.mapList);

                List<Integer> playedMap = data.getMapManager().getMapList();

                for (int i : cloneMapList) {
                    if (!playedMap.contains(i)) {
                        playedMap.add(i);
                        foundNumber = i;
                        break;
                    }
                }

                if (foundNumber == 0) {
                    Bukkit.broadcastMessage("end the GAME!");
                    //end the game
                }

                AreaCreator areaCreator = CreateBox.areaCreatorMap.get(foundNumber + "-" + GameInfo.round);

                World world = Bukkit.getWorld("world");

                Vector vector = new Vector(areaCreator.getXMax(), areaCreator.getYMax(), areaCreator.getZMax()).getMidpoint(new Vector(areaCreator.getXMin(), areaCreator.getYMin(), areaCreator.getZMin()));

                player.teleport(new Location(world, vector.getX(), vector.getY(), vector.getZ()));
            }
        }
    }
}