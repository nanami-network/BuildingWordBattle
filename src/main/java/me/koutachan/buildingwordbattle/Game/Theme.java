package me.koutachan.buildingwordbattle.Game;

import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.PlayerData.impl.TeamEnum.TeamEnum;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Theme {
    public static void startShuffle() {

        GameInfo.nowState = GameStateEnum.BUILDING;
        CreateBox.start();

        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

            }
        }
    }
}