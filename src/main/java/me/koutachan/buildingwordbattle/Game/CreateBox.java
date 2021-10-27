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
    public static int count;

    public static void start() {
        int x = BuildingWordBattle.INSTANCE.getConfig().getInt("startPosX");
        int y = BuildingWordBattle.INSTANCE.getConfig().getInt("startPosY");
        int z = BuildingWordBattle.INSTANCE.getConfig().getInt("startPosZ");

        int temp = GameInfo.mapListSize != 0 ? GameInfo.mapListSize : tempGen();

        for (int i = 1; i <= temp; i++) {
            run(i, x, y, z);
        }
    }

    private static int tempGen() {
        int temp = 0;

        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {
                temp++;
            }
        }
        return temp;
    }

    private static void run(int i, int x, int y, int z) {

        Bukkit.getScheduler().runTaskLater(BuildingWordBattle.INSTANCE, () -> {

            World world = Bukkit.getWorld("world");

            int round = GameInfo.round != 0 ? GameInfo.round : 1;

            int newX = x + (i * 31);
            int newZ = z + ((round - 1) * 31);

            Location location = new Location(world, newX, y, newZ);

            BoxCreator boxCreator = new BoxCreator(location);

            boxCreator.CreateCube(Material.QUARTZ_BLOCK, 0, 30, 0, 45, 0, 30, true);

            areaCreatorMap.put(i + "-" + round, new AreaCreator(null, null, i + "-" + round, location.clone().add(1, 1, 1), location.clone().add(29, 40, 29)));
        }, count * 2L);
    }
}