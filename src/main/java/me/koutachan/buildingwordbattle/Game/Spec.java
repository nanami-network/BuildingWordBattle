package me.koutachan.buildingwordbattle.Game;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.Map.AreaCreator;
import org.bukkit.*;
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
                return;
            }

            //AreaCreator areaCreator;

            time++;
            String theme = null, answer = null;

            if (time == 10) {
                int mapID = currentMapList.get(count);

                areaCreator = CreateBox.areaCreatorMap.get(mapID + "-" + currentRound++);

                theme = areaCreator.getTheme() != null ? areaCreator.getTheme() : "未回答";
                answer = areaCreator.getAnswer() != null ? areaCreator.getAnswer() : "未回答";

                World world = Bukkit.getWorld("world");

                Vector middle = areaCreator.getMiddle();

                Location location = new Location(world, middle.getX(), middle.getY(), middle.getZ());

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.teleport(location);
                }



                if (currentRound >= maxRound) {
                    count++;
                    if (currentMapList.size() > count) {
                        task.cancel();
                        //END!!!
                        return;
                    }
                }
            }
            if (time > 10) {

                if (time - 11 <= theme.length()) {
                    theme = addChatColor(theme, time - 11);
                    sendTitle(ChatColor.AQUA + "お題: " + theme, ChatColor.AQUA + "回答: " + ChatColor.MAGIC + answer, 0, 20, 20);
                } else if (time - theme.length() - 11 <= answer.length()) {
                    answer = addChatColor(answer, time - theme.length() - 11);
                    sendTitle(ChatColor.AQUA + "お題: " + theme, ChatColor.AQUA + "回答: " + answer, 0, 20, 20);
                } else {
                    if (time - theme.length() - answer.length() - 11 > 10) {
                        time = 0;
                    }
                }
                //areaCreator.getTheme();
            }
        }, 0, 20);
    }

    private static String addChatColor(String str, int position) {
        return str.substring(0, position) + ChatColor.MAGIC + str.substring(position);
    }

    private static void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            try {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 1f);
            } catch (NoSuchFieldError e) {
                player.playSound(player.getLocation(), Sound.valueOf("NOTE_PLING"), 1f, 1f);
            }
        }
    }
}