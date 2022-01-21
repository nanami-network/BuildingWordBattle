package me.koutachan.buildingwordbattle.game.gameutil;

import lombok.experimental.UtilityClass;
import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.game.GameInfo;
import me.koutachan.buildingwordbattle.game.gameEnum.GameEnum;
import me.koutachan.buildingwordbattle.game.gameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.game.system.Spec;
import me.koutachan.buildingwordbattle.map.AreaCreator;
import me.koutachan.buildingwordbattle.playerdata.PlayerData;
import me.koutachan.buildingwordbattle.playerdata.PlayerDataUtil;
import me.koutachan.buildingwordbattle.playerdata.impl.Enum.TeamEnum;
import me.koutachan.buildingwordbattle.util.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

@UtilityClass
public class BuildingWordUtility {
    public AreaCreator getAnswerArea(int mapID) {

        int count = GameInfo.CURRENT_BUILD_ROUND;
        if (count == 1) count = 2;

        while (true) {

            count--;

            if (count < 1) {
                //invalid
                return null;
            }

            AreaCreator areaCreator = GameInfo.areaCreator.get(mapID + "-" + count);

            if (areaCreator.getAnswer() != null) {
                return areaCreator;
            }
        }
    }

    public AreaCreator getMap(int mapID) {

        int count = GameInfo.CURRENT_BUILD_ROUND;

        while (true) {

            if (count < 1) {
                //invalid
                return null;
            }

            AreaCreator areaCreator = GameInfo.areaCreator.get(mapID + "-" + count);

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

    public void resetGame(boolean teleport) {
        GameInfo.gameInfo = GameEnum.LOBBY;
        GameInfo.gameState = GameStateEnum.NULL;

        //箱の生成数の制御
        GameInfo.BOX_MAX_VALUE = 0;

        //回すマップのリスト
        GameInfo.CURRENT_MAP_LIST.clear();

        //現在のラウンド
        GameInfo.CURRENT_ROUND = 0;
        GameInfo.CURRENT_BUILD_ROUND = 0;
        //ラウンドの最大を計算
        GameInfo.CALCULATE_MAX_ROUND = 0;
        //ラウンドの最大を計算 (表示用)
        GameInfo.CALCULATE_MAX_ROUND_SHOW = 0;

        //エリアの情報
        GameInfo.areaCreator.clear();

        //観戦関連のリセット
        Spec.time = 0;
        Spec.count = 0;
        Spec.round = 0;

        ConfigUtil.sendMessageBroadCast("GAME.PLAYER-DATA-RECREATE");

        PlayerDataUtil.playerDataHashMap.clear();

        World world = Bukkit.getWorld("world");

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerDataUtil.createPlayerData(player);

            mainThreadGameMode(player, GameMode.CREATIVE);

            if (teleport) {
                mainThreadTeleport(player, new Location(world, BuildingWordBattle.INSTANCE.getConfig().getInt("lobbyPosX"), BuildingWordBattle.INSTANCE.getConfig().getInt("lobbyPosY"), BuildingWordBattle.INSTANCE.getConfig().getInt("lobbyPosZ")));
            }
        }
    }

    //ここでの1ラウンドは 回答 又は 建築で１ラウンドとする
    public int calculateMaxRound() {

        int onlinePlayers = PlayerDataUtil.getOnlinePlayers();

        // onlinePlayers
        // - 1
        // 奇数 - 1
        // 奇数の場合、建築の後回答できないため減らす。

        int maxRound = onlinePlayers - 1;
        final boolean modulo = maxRound % 2 != 0;

        if (modulo) maxRound -= 1;

        return maxRound;
    }

    public boolean checkEnd() {
        return GameInfo.CURRENT_ROUND > GameInfo.CALCULATE_MAX_ROUND;
    }

    public void mainThreadGameMode(Player player, GameMode gameMode) {
        Bukkit.getScheduler().runTask(BuildingWordBattle.INSTANCE, () -> player.setGameMode(gameMode));
    }

    public void mainThreadTeleport(Player player, Location location) {
        Bukkit.getScheduler().runTask(BuildingWordBattle.INSTANCE, () -> player.teleport(location));
    }
}