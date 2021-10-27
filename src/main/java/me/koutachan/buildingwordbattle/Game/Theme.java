package me.koutachan.buildingwordbattle.Game;

import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.Map.AreaCreator;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.PlayerData.impl.TeamEnum.TeamEnum;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Theme {
    public static void startShuffle() {

        GameInfo.nowState = GameStateEnum.BUILDING;
        CreateBox.start();

        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(onlinePlayers);

        int count = 0;

        for (Player player : onlinePlayers) {

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

                count++;

                AreaCreator areaCreator = CreateBox.areaCreatorMap.get(String.valueOf(count));
                areaCreator.setAuthor(data.getPlayer().getName());
                areaCreator.setAuthorUUID(data.getPlayer().getUniqueId());
            }
        }
    }
}