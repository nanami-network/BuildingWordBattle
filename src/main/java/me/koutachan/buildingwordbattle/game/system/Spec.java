package me.koutachan.buildingwordbattle.game.system;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.game.GameInfo;
import me.koutachan.buildingwordbattle.game.gameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.game.gameutil.BuildingWordUtility;
import me.koutachan.buildingwordbattle.map.AreaCreator;
import me.koutachan.buildingwordbattle.playerdata.PlayerData;
import me.koutachan.buildingwordbattle.playerdata.PlayerDataUtil;
import me.koutachan.buildingwordbattle.util.ConfigUtil;
import me.koutachan.buildingwordbattle.util.MessageManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * 余裕があったら再コード予定
 * 唯一再コードされてないコード
 */

public class Spec {

    public static int time, count, round, MAX_TITLE = 95;
    public static AreaCreator areaCreator;
    private static String theme, answer;
    public static BukkitTask bukkitTask;
    public static List<Integer> CLONED_MAP_LIST;

    public static void run() {
        final int MAX_ROUND = GameInfo.CURRENT_BUILD_ROUND;

        CLONED_MAP_LIST = new ArrayList<>(GameInfo.CURRENT_MAP_LIST);

        final World world = Bukkit.getWorld("world");

        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(BuildingWordBattle.INSTANCE, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerData data = PlayerDataUtil.getPlayerData(player);

                String chat = ConfigUtil.message("SPEC.SPEC-ACTIONBAR", "%map%|" + data.getMapManager().getMapName());
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(chat));
            }

            if (time == 1) {
                round++;

                if (round > MAX_ROUND) {
                    count++;
                    round = 1;

                    if (count > (CLONED_MAP_LIST.size() - 1)) {
                        //終わり
                        next();

                        return;
                    }
                }

                int mapID = CLONED_MAP_LIST.get(count);
                areaCreator = GameInfo.areaCreator.get(mapID + "-" + round);
                Vector vector = areaCreator.getMiddle();

                theme = areaCreator.getTheme() != null ? areaCreator.getTheme() : "未回答";
                answer = areaCreator.getAnswer() != null ? areaCreator.getAnswer() : "未回答";

                Location location = new Location(world, vector.getX(), vector.getY(), vector.getZ());
                for (Player player : Bukkit.getOnlinePlayers()) {
                    BuildingWordUtility.mainThreadTeleport(player, location);
                }

            } else {
                if (time > 5) {
                    if (time < 20) {
                        sendTitle("――――ステージ――――", areaCreator.getMapName(), 0, 20, 20, false);
                    } else if (time < 55 && time > 25) {
                        sendTitle("~作成", areaCreator.getAuthor(), 0, 20, 20, false);
                    } else if (time < 85 && time > 60) {
                        sendTitle("~回答", areaCreator.getAnswerPlayer(), 0, 20, 20, false);
                    }

                    if (time >= MAX_TITLE) {
                        if (time - MAX_TITLE <= theme.length()) {
                            String themeResult = addChatColor(theme, time - MAX_TITLE);
                            sendTitle(ChatColor.AQUA + "お題: " + themeResult, ChatColor.AQUA + "回答: " + ChatColor.MAGIC + answer, 0, 20, 20, true);
                        } else if (time - theme.length() - MAX_TITLE <= answer.length()) {
                            String themeAnswer = addChatColor(answer, time - theme.length() - MAX_TITLE);
                            sendTitle(ChatColor.AQUA + "お題: " + theme, ChatColor.AQUA + "回答: " + themeAnswer, 0, 20, 20, true);
                        } else if (time - theme.length() - answer.length() - MAX_TITLE > 20) {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                try {
                                    String hoverMessage = ConfigUtil.messageList("SPEC.SPEC-CHAT-HOVER",
                                            "%theme-player%|" + (areaCreator.getThemePlayer() != null ? areaCreator.getThemePlayer() : MessageManager.getString("GAME.NOT-SET")),
                                            "%build-player%|" + (areaCreator.getAuthor() != null ? areaCreator.getAuthor() : MessageManager.getString("GAME.NOT-SET")),
                                            "%answer-player%|" + (areaCreator.getAnswerPlayer() != null ? areaCreator.getAnswerPlayer() : MessageManager.getString("GAME.NOT-SET")),
                                            "%theme%|" + theme,
                                            "%answer%|" + answer);

                                    TextComponent message = new TextComponent(ConfigUtil.message("SPEC.SPEC-CHAT-MESSAGE", "%map_name%|" + areaCreator.getMapName()));
                                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMessage).create()));

                                    player.spigot().sendMessage(message);
                                } catch (Exception e) {
                                    break;
                                }
                            }

                            time = 0;
                        } else {
                            sendTitle(ChatColor.AQUA + "お題: " + theme, ChatColor.AQUA + "回答: " + answer, 0, 20, 20, false);
                        }
                    }
                }
            }

            time++;
        }, 0, 5);
    }

    public static void start() {

        GameInfo.gameState = GameStateEnum.SPEC;

        for (Player player : Bukkit.getOnlinePlayers()) {
            BuildingWordUtility.mainThreadGameMode(player, GameMode.SPECTATOR);
            try {
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
            } catch (NoSuchFieldError e) {
                player.playSound(player.getLocation(), Sound.valueOf("EXPLODE"), 1f, 1f);
            }
        }

        run();
    }

    public static void next() {
        stop();

        BuildingWordUtility.resetGame(true);
    }

    public static void stop() {
        if (bukkitTask != null) {
            bukkitTask.cancel();
            bukkitTask = null;
        }
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
}