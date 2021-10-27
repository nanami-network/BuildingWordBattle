package me.koutachan.buildingwordbattle.Game;

import me.koutachan.buildingwordbattle.BoxCreator;
import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.Map.AreaCreator;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.PlayerData.impl.TeamEnum.TeamEnum;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CreateBox {

    public static Map<String, AreaCreator> areaCreatorMap = new HashMap<>();

    public static void start() {
        int x = BuildingWordBattle.INSTANCE.getConfig().getInt("startPosX");
        int y = BuildingWordBattle.INSTANCE.getConfig().getInt("startPosY");
        int z = BuildingWordBattle.INSTANCE.getConfig().getInt("startPosZ");

        int count = 0;

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = PlayerDataUtil.getPlayerData(player);


            if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {
                count++;

                World world = Bukkit.getWorld("world");

                int newX = x + (count * 31);

                Location location = new Location(world, newX, y, z);

                BoxCreator boxCreator = new BoxCreator(location);

                Bukkit.getScheduler().runTask(BuildingWordBattle.INSTANCE, () -> boxCreator.CreateCube(Material.QUARTZ_BLOCK, 0, 30, 0, 45, 0, 30, true))
                ;

                areaCreatorMap.put(String.valueOf(count), new AreaCreator(null, null, String.valueOf(count), location.clone().add(1, 1, 1), location.clone().add(29, 40, 29)));
                //YMax = 40;
            }
        }
        GameInfo.maxRound = count;
        GameInfo.round = 1;
    }
}