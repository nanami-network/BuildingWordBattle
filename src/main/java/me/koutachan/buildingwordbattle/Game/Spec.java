package me.koutachan.buildingwordbattle.Game;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.Map.AreaCreator;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.Utilities.ChatColorUtility;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;

public class Spec {

    public static int currentRound, currentMap, time, count;

    private static String theme = null, answer = null;

    public static AreaCreator areaCreator;

    private static BukkitTask task;


    //汚すぎる！！！
    public static void startSpec() {

        Build.run();

        Bukkit.broadcastMessage(ChatColorUtility.translateAlternateColorCodes("&aゲーム終了！ 観戦モードに移行します"));

        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
            } catch (NoSuchFieldError e) {
                player.playSound(player.getLocation(), Sound.valueOf("EXPLODE"), 1f, 1f);
            }
        }

        GameInfo.nowState = GameStateEnum.SPEC;
        List<Integer> currentMapList = GameInfo.mapList;

        int maxRound = GameInfo.buildRound;

        count = 0;

        currentRound = 0;
        currentMap = 0;

        task = Bukkit.getScheduler().runTaskTimer(BuildingWordBattle.INSTANCE, () -> {
            if (GameInfo.nowState != GameStateEnum.SPEC) {
                task.cancel();
                return;
            }

            //AreaCreator areaCreator;

            time++;

            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerData data = PlayerDataUtil.getPlayerData(player);

                sendActionBar(player,ChatColorUtility.translateAlternateColorCodes("現在のマップ: " + data.getMapManager().getMapName()));
            }

            if (time == 10) {
                int mapID = currentMapList.get(count);
                currentRound++;

                areaCreator = CreateBox.areaCreatorMap.get(mapID + "-" + currentRound);

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
                    currentRound = 0;
                    if (count > currentMapList.size()) {
                        task.cancel();
                        Bukkit.broadcastMessage("task end");
                        Game.resetGame();
                        return;
                    }
                }
            }
            if (time > 10) {
                //Bukkit.broadcastMessage("a");
                if (time - 11 <= theme.length()) {
                    String themeResult = addChatColor(theme, time - 11);
                    sendTitle(ChatColor.AQUA + "お題: " + themeResult, ChatColor.AQUA + "回答: " + ChatColor.MAGIC + "" + answer, 0, 20, 20, true);
                } else if (time - theme.length() - 11 <= answer.length()) {
                    String themeAnswer = addChatColor(answer, time - theme.length() - 11);
                    sendTitle(ChatColor.AQUA + "お題: " + theme, ChatColor.AQUA + "回答: " + themeAnswer, 0, 20, 20, true);
                } else if (time - theme.length() - answer.length() - 11 > 20) {
                    time = 0;
                } else {
                    sendTitle(ChatColor.AQUA + "お題: " + theme, ChatColor.AQUA + "回答: " + answer, 0, 20, 20, false);
                }
            }
        }, 0, 5);
    }

    private static String addChatColor(String str, int position) {
        return str.substring(0, position) + ChatColor.MAGIC + str.substring(position);
    }

    private static void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, boolean sound) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            if (sound) {
                try {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 1f);
                } catch (NoSuchFieldError e) {
                    player.playSound(player.getLocation(), Sound.valueOf("NOTE_PLING"), 1f, 1f);
                }
            }
        }
    }

    private static void sendActionBar(String title) {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(title));
            }
        } catch (NoSuchMethodError ignored) {
        }
    }

    private static void sendActionBar(Player player, String title) {
        try {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(title));
        } catch (NoSuchMethodError ignored) {
        }
    }
}