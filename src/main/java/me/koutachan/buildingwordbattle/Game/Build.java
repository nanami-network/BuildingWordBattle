package me.koutachan.buildingwordbattle.Game;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.Map.AreaCreator;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.PlayerData.impl.Enum.TeamEnum;
import me.koutachan.buildingwordbattle.Timer.Scheduler;
import me.koutachan.buildingwordbattle.Utilities.BuildingWordUtility;
import me.koutachan.buildingwordbattle.Utilities.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Build {
    public static void startShuffle() {
        GameInfo.round++;

        if (Game.gameEndCheck()) {
            return;
        }

        GameInfo.buildRound++;

        CreateBox.start();

        ChatUtil.sendMessageBroadCast("GAME.MAP_CREATE_STARTED", "%tick%|" + Bukkit.getOnlinePlayers().size() * 3);
        //Bukkit.broadcastMessage(ChatUtil.translateAlternateColorCodes("&7マップを生成中です・・・これはしばらくかかる場合があります \n予想時間: " + Bukkit.getOnlinePlayers().size() * 3 + "tick"));


        Bukkit.getScheduler().runTaskLater(BuildingWordBattle.INSTANCE, () -> {

            List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
            Collections.shuffle(onlinePlayers);

            GameInfo.nowState = GameStateEnum.BUILDING;

            ChatUtil.sendMessageBroadCast("GAME.MAP_CREATE_ENDED");
            //Bukkit.broadcastMessage(ChatUtil.translateAlternateColorCodes("&aマップ生成が終了しました！ 次のラウンドを開始します！"));

            for (Player player : onlinePlayers) {

                PlayerData data = PlayerDataUtil.getPlayerData(player);

                if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

                    int id = data.getThemeManager().getThemeMap();

                    AreaCreator areaCreator = CreateBox.areaCreatorMap.get(id + "-" + GameInfo.buildRound);

                    PlayerData themeData = BuildingWordUtility.getAnswerArea(id);

                    if (themeData != null) {
                        areaCreator.setTheme(themeData.getAnswerManager().getAnswer());
                        areaCreator.setThemePlayer(themeData.getPlayer().getName());
                        areaCreator.setThemeUUID(themeData.getPlayer().getUniqueId());

                        int temp = GameInfo.buildRound - 1;
                        AreaCreator tempAreaCreator = CreateBox.areaCreatorMap.get(id + "-" + temp);

                        tempAreaCreator.setAnswer(themeData.getAnswerManager().getAnswer());
                    } else {
                        areaCreator.setTheme(null);
                        areaCreator.setThemePlayer(null);
                        areaCreator.setThemeUUID(null);

                        int temp = GameInfo.buildRound - 1;
                        AreaCreator tempAreaCreator = CreateBox.areaCreatorMap.get(id + "-" + temp);
                        tempAreaCreator.setAnswer(null);
                    }
                }
            }

            Game.startShuffle();

            Scheduler.buildingTime = BuildingWordBattle.INSTANCE.getConfig().getInt("buldingTime");
        }, Bukkit.getOnlinePlayers().size() * 3L);
    }

    public static void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = PlayerDataUtil.getPlayerData(player);

            if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {
                int id = data.getThemeManager().getThemeMap();

                int temp = GameInfo.buildRound - 1;
                if (temp == 0) temp = 1;
                AreaCreator tempAreaCreator = CreateBox.areaCreatorMap.get(id + "-" + temp);

                //何かがおかしい
                if (temp < 1 || tempAreaCreator == null || tempAreaCreator.getAnswer() != null) break;

                PlayerData themeData = BuildingWordUtility.getAnswerArea(id);

                if (themeData != null) {
                    tempAreaCreator.setAnswer(themeData.getAnswerManager().getAnswer());
                } else {
                    tempAreaCreator.setAnswer(null);
                }
            }
        }
    }
}