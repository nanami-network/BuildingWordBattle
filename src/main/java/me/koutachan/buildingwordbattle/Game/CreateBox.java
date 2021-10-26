package me.koutachan.buildingwordbattle.Game;

import me.koutachan.buildingwordbattle.BoxCreator;
import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.PlayerData.impl.TeamEnum.TeamEnum;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class CreateBox {

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

                BoxCreator boxCreator = new BoxCreator(new Location(world, newX, y, z));

                boxCreator.CreateCube(Material.QUARTZ_BLOCK, 0, 30, 0, 30, 0, 30, true);
            }
        }
        GameInfo.maxRound = count;
        GameInfo.round = 1;
    }
}