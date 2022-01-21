package me.koutachan.buildingwordbattle.game.system;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.game.GameInfo;
import me.koutachan.buildingwordbattle.game.gameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.game.gameutil.BuildingWordUtility;
import me.koutachan.buildingwordbattle.map.AreaCreator;
import me.koutachan.buildingwordbattle.playerdata.PlayerData;
import me.koutachan.buildingwordbattle.playerdata.PlayerDataUtil;
import me.koutachan.buildingwordbattle.playerdata.impl.Enum.TeamEnum;
import me.koutachan.buildingwordbattle.util.ChatUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Build {

    public static int time;
    public static boolean ended;

    public static void run() {
        if (time <= 0 && !ended) {
            next();
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {

                PlayerData data = PlayerDataUtil.getPlayerData(player);

                if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {
                    try {

                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatUtil.message("GAME.GAME_ACTIONBAR", "%theme%|" + data.getMapManager().getTheme())));
                    } catch (NoSuchMethodError ignore) {
                    }
                } else {
                    //TODO: 詳細を伝える
                }
            }
            time--;
        }
    }

    public static void start(boolean createBox) {
        if (createBox) {
            GameInfo.CURRENT_ROUND++;
            if (!BuildingWordUtility.checkEnd()) {
                GameSystem.preCreateBox(GameStateEnum.BUILDING);
            } else {
                Spec.start();
            }
        } else {
            receive(false);
        }
    }

    public static void next() {
        ended = true;

        Answer.start();
    }

    public static void receive(boolean setTheme) {
        //マップ生成が終わった
        if (setTheme) {
            for (int id : GameInfo.CURRENT_MAP_LIST) {
                AreaCreator areaCreator = GameInfo.areaCreator.get(id + "-" + GameInfo.CURRENT_BUILD_ROUND);

                AreaCreator findAreaCreator = BuildingWordUtility.getAnswerArea(id);
                if (findAreaCreator != null) {
                    //v1 - 通常
                    areaCreator.setTheme(findAreaCreator.getAnswer());
                    areaCreator.setThemePlayer(findAreaCreator.getAnswerPlayer());
                    areaCreator.setThemeUUID(findAreaCreator.getAnswerUUID());
                } else {
                    //v2 - それでもダメな場合
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        PlayerData data = PlayerDataUtil.getPlayerData(player);
                        if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER && data.getThemeManager().getThemeMap() == id) {
                            areaCreator.setTheme(data.getThemeManager().getTheme());
                            areaCreator.setThemePlayer(data.getPlayer().getName());
                            areaCreator.setThemeUUID(data.getPlayer().getUniqueId());
                        }
                    }
                }
            }
        }
        GameInfo.gameState = GameStateEnum.BUILDING;

        time = BuildingWordBattle.INSTANCE.getConfig().getInt("buildingTime");
        ended = false;

        //終わり
        GameSystem.startShuffle(GameStateEnum.BUILDING);
    }
}