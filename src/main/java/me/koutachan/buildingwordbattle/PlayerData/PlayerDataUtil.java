package me.koutachan.buildingwordbattle.PlayerData;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.koutachan.buildingwordbattle.PlayerData.impl.TeamEnum.TeamEnum;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@UtilityClass
public class PlayerDataUtil {
    public static final Map<UUID, PlayerData> playerDataHashMap = new HashMap<>();

    public PlayerData getPlayerData(Player player) {
        return playerDataHashMap.get(player.getUniqueId());
    }

    public void createPlayerData(Player player) {
        playerDataHashMap.put(player.getUniqueId(), new PlayerData(player));
    }

    public PlayerData removePlayerData(Player player) {
       return playerDataHashMap.remove(player.getUniqueId());
    }

    public int getOnlinePlayers() {

        int result = 0;

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = getPlayerData(player);

            if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {
                result++;
            }
        }
        return result;
    }
}