package me.koutachan.buildingwordbattle.game.main;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.game.GameInfo;
import me.koutachan.buildingwordbattle.game.enums.GameStateEnum;
import me.koutachan.buildingwordbattle.game.gameutil.BuildingWordUtility;
import me.koutachan.buildingwordbattle.map.AreaCreator;
import me.koutachan.buildingwordbattle.playerdata.PlayerData;
import me.koutachan.buildingwordbattle.playerdata.PlayerDataUtil;
import me.koutachan.buildingwordbattle.playerdata.impl.enums.TeamEnum;
import me.koutachan.buildingwordbattle.util.ConfigUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Theme {

    public static int time, playerCount;
    public static boolean ended;

    public static void run() {
        if (time <= 0 && !ended) {
            next();
        } else {
            playerCount = 0;

            for (Player player : Bukkit.getOnlinePlayers()) {

                PlayerData data = PlayerDataUtil.getPlayerData(player);

                if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

                    String theme = data.getThemeManager().getTheme();

                    if (theme == null) {
                        theme = "お題を設定してください";

                        playerCount++;
                    }

                    try {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ConfigUtil.message("GAME.GAME-ACTIONBAR", "%theme%|" + theme)));
                    } catch (NoSuchMethodError ignore) {
                    }
                }
            }

            if (playerCount == 0 && !ended) {
                next();
            } else {
                time--;
            }
        }
    }

    public static void start() {
        time = BuildingWordBattle.INSTANCE.getConfig().getInt("themeTime");
        ended = false;

        GameInfo.gameState = GameStateEnum.THEME;
        GameInfo.CURRENT_ROUND++;
    }

    public static void next() {
        ended = true;
        //終わったことを通知
        ConfigUtil.sendMessageBroadCast("GAME.THEME-TIME-ENDED");

        PlayerDataUtil.clearQuitPlayers();
        BuildingWordUtility.preCreateBox(GameStateEnum.THEME);
    }

    public static void receive() {
        //マップ生成が終わった

        int count = 0;

        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

                count++;

                AreaCreator areaCreator = GameInfo.areaCreator.get(count + "-" + 1);

                String theme = data.getThemeManager().getTheme();

                //ぬるぽだったらつばように変更する
                if (theme == null) {
                    theme = "つばよう";

                    data.getThemeManager().setTheme("つばよう"); //ここでもつばように変更
                }

                areaCreator.setTheme(theme);
                areaCreator.setThemeData(data);

                //回していいマップに入れる
                GameInfo.CURRENT_MAP_LIST.add(count);
                //プレイ済みのマップに入れる
                data.getMapManager().addPlayedMap(count);
                //自分が出したお題のマップを入れる (途中抜けの場合に回さないために)
                data.getThemeManager().setThemeMap(count);
            }
        }

        Build.start(false);
    }
}