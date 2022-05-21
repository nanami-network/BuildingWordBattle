package me.koutachan.buildingwordbattle.playerdata;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.koutachan.buildingwordbattle.game.GameInfo;
import me.koutachan.buildingwordbattle.game.enums.GameStateEnum;
import me.koutachan.buildingwordbattle.game.gameutil.BuildingWordUtility;
import me.koutachan.buildingwordbattle.map.AreaCreator;
import me.koutachan.buildingwordbattle.playerdata.impl.enums.TeamEnum;
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
        for (PlayerData data : quitPlayers) {
            if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {
                //回すマップリストから消す
                GameInfo.CURRENT_MAP_LIST.remove((Integer) data.getThemeManager().getThemeMap());
            }
        }

        quitPlayers.clear();
    }

    public void deleteOfflinePlayerData() {
        for (Iterator<PlayerData> it = playerDataHashMap.values().iterator(); it.hasNext();) {

            PlayerData data = it.next();

            if (!data.getPlayer().isOnline()) {
                data.getQuitManager().stop();

                it.remove();
            }
        }
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
            target.getQuitManager().stop();
        } else {
            target.setMapManager(data.getMapManager());
            target.setThemeManager(data.getThemeManager());
            target.setTeamManager(data.getTeamManager());

            target.getScoreBoardManager().setData(target);
            target.getQuitManager().setData(target);
            target.getTeamManager().setData(target);
            target.getMapManager().setData(target);
            target.getThemeManager().setData(target);

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