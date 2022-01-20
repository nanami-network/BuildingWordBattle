package me.koutachan.buildingwordbattle.Utilities;

import lombok.experimental.UtilityClass;
import me.koutachan.buildingwordbattle.Game.CreateBox;
import me.koutachan.buildingwordbattle.Map.AreaCreator;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.PlayerData.impl.Enum.TeamEnum;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@UtilityClass
public class BuildingWordUtility {
    public PlayerData getAnswerArea(int mapID) {

        int count = GameInfo.buildRound;
        if (count == 1) count = 2;

        while (true) {

            count--;

            if (count < 1) {
                //invalid
                return null;
            }

            AreaCreator areaCreator = CreateBox.areaCreatorMap.get(mapID + "-" + count);

            Player player = Bukkit.getPlayer(areaCreator.getAnswerUUID());

            if (player != null) {
                PlayerData data = PlayerDataUtil.getPlayerData(player);

                if (data != null && data.getAnswerManager().getAnswer() != null) {
                    return data;
                }
            }
        }
    }

    public AreaCreator getMap(int mapID) {

        int count = GameInfo.buildRound;

        while (true) {

            if (count < 1) {
                //invalid
                return null;
            }

            AreaCreator areaCreator = CreateBox.areaCreatorMap.get(mapID + "-" + count);

            Player player = Bukkit.getPlayer(areaCreator.getAuthorUUID());


            if (player != null) {
                PlayerData data = PlayerDataUtil.getPlayerData(player);

                if (data != null && data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {
                    return areaCreator;
                }
            }
            count--;
        }
    }
}