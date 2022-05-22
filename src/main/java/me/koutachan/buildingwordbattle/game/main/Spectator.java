package me.koutachan.buildingwordbattle.game.main;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.game.GameInfo;
import me.koutachan.buildingwordbattle.game.enums.GameStateEnum;
import me.koutachan.buildingwordbattle.util.BuildingWordUtility;
import me.koutachan.buildingwordbattle.map.AreaCreator;
import me.koutachan.buildingwordbattle.util.ConfigUtil;
import me.koutachan.buildingwordbattle.util.MessageManager;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Spectator {

    public static int time, count, round;

    public static AreaCreator areaCreator;

    public static boolean fastMode, superFastMode, skipIfAnswerIsNull;

    public static BukkitTask bukkitTask;
    public static List<Integer> CLONED_MAP_LIST;

    public static void run() {
        CLONED_MAP_LIST = new ArrayList<>(GameInfo.CURRENT_MAP_LIST);

        final World world = Bukkit.getWorld("world");

        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(BuildingWordBattle.INSTANCE, () -> {
            if (time == 1) {
                if (isNextRound() && isEnded()) {
                    next();
                } else {
                    final int mapID = CLONED_MAP_LIST.get(count);
                    areaCreator = GameInfo.areaCreator.get(mapID + "-" + round);

                    Vector middle = areaCreator.getMiddle();

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        BuildingWordUtility.mainThreadTeleport(player, new Location(world, middle.getX(), middle.getY(), middle.getZ()));
                    }

                    if (fastMode) time = 95;
                }

            } else if (time > 5) {
                if (superFastMode) onSuperFastMode();
                else onDefaultMode();
            }

            time++;
        }, 0, 5);
    }

    private static void onSuperFastMode() {
        if (time < 95) onDefaultMode();
        else {

            String theme = areaCreator.getTheme() != null ? areaCreator.getTheme() : "未回答";
            String answer = areaCreator.getAnswer() != null ? areaCreator.getAnswer() : "未回答";

            int time = Spectator.time - 95;

            String themeFormat = ConfigUtil.translateAlternateColorCodes(String.format("&bお題: %s", theme));
            String answerFormat = ConfigUtil.translateAlternateColorCodes(String.format("&b回答: %s", answer));


            if (time <= 10) {
                sendTitle(themeFormat, answerFormat, time == 0f);
            } else {
                Spectator.time = 0;
            }
        }
    }

    public static void onDefaultMode() {
        if (time < 20) {
            sendTitle("――――ステージ――――", areaCreator.getMapName(), false);
        } else if (time < 55 && time > 25) {
            sendTitle("~作成", areaCreator.getAuthorData().getPlayer().getName(), false);
        } else if (time < 85 && time > 60) {
            sendTitle("~回答", areaCreator.getAnswerData().getPlayer().getName(), false);
        }

        if (time >= 95) {

            String theme = areaCreator.getTheme() != null ? areaCreator.getTheme() : "未回答";
            String answer = areaCreator.getAnswer() != null ? areaCreator.getAnswer() : "未回答";

            int time = Spectator.time - 95;

            if (time <= theme.length()) {
                String design = addChatColor(theme, time);

                String themeFormat = ConfigUtil.translateAlternateColorCodes(String.format("&bお題: %s", design));
                String answerFormat = ConfigUtil.translateAlternateColorCodes(String.format("&b回答: &k%s", answer));

                sendTitle(themeFormat, answerFormat, true);
            } else {

                time -= theme.length();

                if (time <= answer.length()) {
                    if (skipIfAnswerIsNull && areaCreator.getAnswer() == null) {
                        Spectator.time += answer.length();

                        ConfigUtil.sendMessageBroadCast("SPEC.SPEC-SKIPPED-ANSWER-MESSAGE");
                    } else {

                        String design = addChatColor(answer, time);

                        String themeFormat = ConfigUtil.translateAlternateColorCodes(String.format("&bお題: %s", theme));
                        String answerFormat = ConfigUtil.translateAlternateColorCodes(String.format("&b回答: %s", design));

                        sendTitle(themeFormat, answerFormat, true);
                    }
                } else {

                    time -= answer.length();

                    String themeFormat = ConfigUtil.translateAlternateColorCodes(String.format("&bお題: %s", theme));
                    String answerFormat = ConfigUtil.translateAlternateColorCodes(String.format("&b回答: %s", answer));

                    if (time > 20) {
                        sendMessage(theme, answer);

                        Spectator.time = 0;
                    } else {
                        sendTitle(themeFormat, answerFormat, false);
                    }
                }
            }
        }
    }

    private static void sendMessage(String theme, String answer) {
        String not_set_message = MessageManager.getString("GAME.NOT-SET");

        String hoverMessage = ConfigUtil.messageList("SPEC.SPEC-CHAT-HOVER",
                "%theme-player%|" + (areaCreator.getThemeData() != null ? format(areaCreator.getThemeData().getPlayer().getName(), not_set_message) : not_set_message),
                "%build-player%|" + (areaCreator.getAuthorData() != null ? format(areaCreator.getAuthorData().getPlayer().getName(), not_set_message) : not_set_message),
                "%answer-player%|" + (areaCreator.getAnswerData() != null ? format(areaCreator.getAnswerData().getPlayer().getName(), not_set_message) : not_set_message),
                "%theme%|" + theme,
                "%answer%|" + answer);

        TextComponent message = new TextComponent(ConfigUtil.message("SPEC.SPEC-CHAT-MESSAGE", "%map_name%|" + areaCreator.getMapName()));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMessage).create()));

        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                player.spigot().sendMessage(message);
            } catch (Exception ex) {
                break;
            }
        }
    }

    public static String format(String theme, String nullMessage) {
        if (theme != null) {
            return theme;
        } else {
            return nullMessage;
        }

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

    public static boolean isNextRound() {
        round++;

        return round > GameInfo.CURRENT_BUILD_ROUND;
    }

    public static boolean isEnded() {
        round = 1;

        count++;

        return count >= CLONED_MAP_LIST.size();
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
        return new StringBuilder(str).insert(position, ChatColor.MAGIC).toString();
    }

    private static void sendTitle(String title, String subtitle, boolean sound) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(title, subtitle, 0, 20, 20);
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