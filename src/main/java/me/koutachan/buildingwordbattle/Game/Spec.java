package me.koutachan.buildingwordbattle.Game;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.Map.AreaCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;

public class Spec {

    public static int currentRound, currentMap, time, count;

    public static AreaCreator areaCreator;

    private static BukkitTask task;


    //汚すぎる！！！
    public static void startSpec() {
        List<Integer> currentMapList = GameInfo.mapList;

        int maxRound = GameInfo.buildRound;


        count = 0;

        //currentCount = 0;
        currentRound = 0;
        currentMap = 0;

        task = Bukkit.getScheduler().runTaskTimer(BuildingWordBattle.INSTANCE, () -> {
            if (GameInfo.nowState != GameStateEnum.SPEC) {
                task.cancel();
            }

            //AreaCreator areaCreator;

            time++;
            if (time == 10) {
                int mapID = currentMapList.get(count);

                areaCreator = CreateBox.areaCreatorMap.get(mapID + "-" + currentRound++);

                World world = Bukkit.getWorld("world");

                Vector middle = areaCreator.getMiddle();

                Location location = new Location(world, middle.getX(), middle.getY(), middle.getZ());

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.teleport(location);
                }

                if (currentRound >= maxRound) {
                    count++;
                }
            }
            if (time > 10) {
                String theme = areaCreator.getTheme() != null ? areaCreator.getTheme() : "未回答";
                String answer = areaCreator.getAnswer() != null ? areaCreator.getAnswer() : "未回答";

                int length = theme.length();

                if (time - 11 <= length) {
                    theme = addChatColor(theme, time - 11);
                } else if (time - length - 11 <= answer.length()) {
                    answer = addChatColor(answer, time - length - 11);
                } else {
                    //NEXT;
                }

                
                //areaCreator.getTheme();
            }
        }, 0, 20);
    }

    private static String addChatColor(String str, int position) {
        return str.substring(0, position) + ChatColor.MAGIC + str.substring(position);
    }
}