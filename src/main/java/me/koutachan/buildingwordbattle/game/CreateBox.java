package me.koutachan.buildingwordbattle.game;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.map.AreaCreator;
import me.koutachan.buildingwordbattle.util.BoxCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class CreateBox {

    public static void start() {
        int x = BuildingWordBattle.INSTANCE.getConfig().getInt("startPosX");
        int y = BuildingWordBattle.INSTANCE.getConfig().getInt("startPosY");
        int z = BuildingWordBattle.INSTANCE.getConfig().getInt("startPosZ");

        int temp = GameInfo.BOX_MAX_VALUE;

        for (int i = 1; i <= temp; i++) {
            run(i, x, y, z);
        }
    }

    private static void run(int i, int x, int y, int z) {

        Bukkit.getScheduler().runTaskLater(BuildingWordBattle.INSTANCE, () -> {

            World world = Bukkit.getWorld("world");

            int round = GameInfo.CURRENT_BUILD_ROUND;

            int newX = x + (i * 31);
            int newZ = z + ((round - 1) * 31);

            Location location = new Location(world, newX, y, newZ);

            BoxCreator boxCreator = new BoxCreator(location);

            boxCreator.CreateCube(Material.QUARTZ_BLOCK, 0, 30, 0, 45, 0, 30, true);

            GameInfo.areaCreator.put(i + "-" + round, new AreaCreator(null, i + "-" + round, location.clone().add(1, 1, 1), location.clone().add(29, 40, 29)));
        }, i * 2L);
    }
}