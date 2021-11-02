package me.koutachan.buildingwordbattle.Game;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.Utilities.BuildingWordUtility;
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

public class Build {
    public static void startShuffle() {

        GameInfo.buildRound++;
        GameInfo.round++;

        CreateBox.start();

        Bukkit.broadcastMessage(ChatColorUtility.translateAlternateColorCodes("&7マップを生成中です・・・これはしばらくかかる場合があります \n予想時間: " + Bukkit.getOnlinePlayers().size() * 3 + "tick"));


        Bukkit.getScheduler().runTaskLater(BuildingWordBattle.INSTANCE, () -> {

            List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
            Collections.shuffle(onlinePlayers);

            Bukkit.broadcastMessage(ChatColorUtility.translateAlternateColorCodes("&aマップ生成が終了しました！ 次のラウンドを開始します！"));

            GameInfo.nowState = GameStateEnum.BUILDING;

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
}