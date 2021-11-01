package me.koutachan.buildingwordbattle.Game;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.ChatColorUtil;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.Map.AreaCreator;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.PlayerData.impl.TeamEnum.TeamEnum;
import me.koutachan.buildingwordbattle.Timer.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Build {
    public static void startShuffle() {

        GameInfo.buildRound++;
        GameInfo.round++;

        CreateBox.start();

        Bukkit.broadcastMessage(ChatColorUtil.translateAlternateColorCodes("&7マップを生成中です・・・これはしばらくかかる場合があります \n予想時間: " + Bukkit.getOnlinePlayers().size() * 3 + "tick"));


        Bukkit.getScheduler().runTaskLater(BuildingWordBattle.INSTANCE, () -> {

            List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
            Collections.shuffle(onlinePlayers);

            Bukkit.broadcastMessage(ChatColorUtil.translateAlternateColorCodes("&aマップ生成が終了しました！ 次のラウンドを開始します！"));

            GameInfo.nowState = GameStateEnum.BUILDING;

            for (Player player : onlinePlayers) {

                PlayerData data = PlayerDataUtil.getPlayerData(player);

                if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

                    int id = data.getThemeManager().getThemeMap();

                    AreaCreator areaCreator = CreateBox.areaCreatorMap.get(id + "-" + GameInfo.buildRound);

                    PlayerData themeData = Utilities(id);

                    if (themeData != null) {
                        areaCreator.setTheme(themeData.getAnswerManager().getAnswer());
                        areaCreator.setThemePlayer(themeData.getPlayer().getName());
                        areaCreator.setThemeUUID(themeData.getPlayer().getUniqueId());
                    } else {
                        areaCreator.setTheme(null);
                        areaCreator.setThemePlayer(null);
                        areaCreator.setThemeUUID(null);
                    }
                }
            }

            Game.startShuffle();

            Scheduler.buildingTime = BuildingWordBattle.INSTANCE.getConfig().getInt("buldingTime");
        }, Bukkit.getOnlinePlayers().size() * 3L);
    }

    private static PlayerData Utilities(int mapID) {

        int count = GameInfo.buildRound;

        while (true) {

            count--;

            if (count < 1) {
                //invalid
                return null;
            }

            AreaCreator areaCreator = CreateBox.areaCreatorMap.get(mapID + "-" + count);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (areaCreator.getAnswerUUID() == player.getUniqueId()) {
                    PlayerData data = PlayerDataUtil.getPlayerData(player);

                    if (data != null && data.getAnswerManager().getAnswer() != null) {
                        return data;
                    }
                }
            }
        }
    }
}