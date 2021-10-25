package me.koutachan.buildingwordbattle.PlayerData;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@UtilityClass
public class PlayerDataUtil {
    private final Map<UUID, PlayerData> playerDataHashMap = new HashMap<>();

    public PlayerData getPlayerData(Player player) {
        return playerDataHashMap.get(player.getUniqueId());
    }

    public void createPlayerData(Player player) {
        playerDataHashMap.put(player.getUniqueId(), new PlayerData(player));
    }

    public void removePlayerData(Player player) {
        playerDataHashMap.remove(player.getUniqueId());
    }
}