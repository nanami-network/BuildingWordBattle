package me.koutachan.buildingwordbattle.playerdata;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.koutachan.buildingwordbattle.game.GameInfo;
import me.koutachan.buildingwordbattle.game.enums.GameStateEnum;
import me.koutachan.buildingwordbattle.game.gameutil.BuildingWordUtility;
import me.koutachan.buildingwordbattle.map.AreaCreator;
import me.koutachan.buildingwordbattle.playerdata.impl.enums.TeamEnum;
import me.koutachan.buildingwordbattle.playerdata.impl.ScoreBoardManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

@Getter
@UtilityClass
public class PlayerDataUtil {
    private static final Map<UUID, PlayerData> playerDataHashMap = new HashMap<>();

    private static final List<PlayerData> quitPlayers = new ArrayList<>();

    public List<PlayerData> getQuitPlayers() {
        return quitPlayers;
    }

    public Map<UUID, PlayerData> getPlayerDataMap() {
        return playerDataHashMap;
    }

    public PlayerData getPlayerData(Player player) {
        return playerDataHashMap.get(player.getUniqueId());
    }

    public PlayerData getPlayerData(UUID UUID) {
        return playerDataHashMap.get(UUID);
    }

    public void createPlayerData(Player player) {
        playerDataHashMap.put(player.getUniqueId(), new PlayerData(player));
    }

    public PlayerData removePlayerData(Player player) {
        return playerDataHashMap.remove(player.getUniqueId());
    }

    public void clear() {
        playerDataHashMap.clear();
    }

    public void clearQuitPlayers() {
        quitPlayers.clear();
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

    public List<PlayerData> getOnlinePlayersData() {
        List<PlayerData> playerData = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = getPlayerData(player);

            if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {
                playerData.add(data);
            }
        }
        return playerData;
    }

    public void set(PlayerData data, PlayerData target) {
        if (data == target) {
            removePlayerData(data.getPlayer());

            target.setScoreBoardManager(new ScoreBoardManager(target));
            target.getQuitManager().stop();
        } else {
            target.setMapManager(data.getMapManager());
            target.setThemeManager(data.getThemeManager());
            target.setTeamManager(data.getTeamManager());

            if (GameInfo.gameState != GameStateEnum.SPEC) {
                BuildingWordUtility.mainThreadGameMode(target.getPlayer(), GameMode.CREATIVE);

                if (GameInfo.gameState != GameStateEnum.THEME) {
                    AreaCreator areaCreator = GameInfo.areaCreator.get(target.getMapManager().getLastMapID() + "-" + GameInfo.CURRENT_BUILD_ROUND);

                    Vector middle = areaCreator.getMiddle();

                    BuildingWordUtility.mainThreadTeleport(target.getPlayer(), new Location(target.getPlayer().getWorld(), middle.getX(), middle.getY(), middle.getZ()));

                    if (GameInfo.gameState == GameStateEnum.BUILDING) areaCreator.setAuthorData(target);
                    else areaCreator.setAnswerData(target);
                }
            }

            PlayerDataUtil.getQuitPlayers().remove(data);
        }
    }
}