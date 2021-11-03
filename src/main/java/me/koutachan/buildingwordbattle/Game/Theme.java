package me.koutachan.buildingwordbattle.Game;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.Utilities.ChatColorUtility;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.Map.AreaCreator;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.PlayerData.impl.Enum.TeamEnum;
import me.koutachan.buildingwordbattle.Timer.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Theme {
    public static void startShuffle() {
        GameInfo.buildRound = 1;
        CreateBox.start();

        Bukkit.broadcastMessage(ChatColorUtility.translateAlternateColorCodes("&7マップを生成中です・・・これはしばらくかかる場合があります \n予想時間: " + Bukkit.getOnlinePlayers().size() * 3 + "tick"));


        Bukkit.getScheduler().runTaskLater(BuildingWordBattle.INSTANCE, () -> {

            List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
            Collections.shuffle(onlinePlayers);

            Bukkit.broadcastMessage(ChatColorUtility.translateAlternateColorCodes("&aマップ生成が終了しました！ ゲームを開始します"));

            GameInfo.nowState = GameStateEnum.BUILDING;

            int count = 0;

            for (Player player : onlinePlayers) {

                PlayerData data = PlayerDataUtil.getPlayerData(player);

                if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

                    count++;

                    AreaCreator areaCreator = CreateBox.areaCreatorMap.get(count + "-" + 1);
                    areaCreator.setTheme(data.getThemeManager().getTheme());
                    areaCreator.setThemePlayer(player.getName());
                    areaCreator.setThemeUUID(player.getUniqueId());


                    GameInfo.mapList.add(count);
                    data.getMapManager().addMap(count);
                    data.getThemeManager().setThemeMap(count);
                }
            }

            //GameInfo.maxRound = count;
            //GameInfo.buildRound = 1;

            GameInfo.round = 1;
            GameInfo.cacheMapSize = count;
            Game.startShuffle();

            Scheduler.buildingTime = BuildingWordBattle.INSTANCE.getConfig().getInt("buldingTime");
        }, Bukkit.getOnlinePlayers().size() * 3L);
    }
}