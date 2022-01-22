package me.koutachan.buildingwordbattle.game.system;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.game.CreateBox;
import me.koutachan.buildingwordbattle.game.GameInfo;
import me.koutachan.buildingwordbattle.game.gameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.game.gameutil.BuildingWordUtility;
import me.koutachan.buildingwordbattle.map.AreaCreator;
import me.koutachan.buildingwordbattle.playerdata.PlayerData;
import me.koutachan.buildingwordbattle.playerdata.PlayerDataUtil;
import me.koutachan.buildingwordbattle.playerdata.impl.Enum.TeamEnum;
import me.koutachan.buildingwordbattle.util.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public class GameSystem {
    public static void run() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerDataUtil.getPlayerData(player).getMapManager().handle();
        }

        GameInfo.CALCULATE_MAX_ROUND = BuildingWordUtility.calculateMaxRound();
        GameInfo.CALCULATE_MAX_ROUND_SHOW = Math.max(GameInfo.CURRENT_ROUND, GameInfo.CALCULATE_MAX_ROUND);
    }

    public static void preCreateBox(GameStateEnum state) {
        GameInfo.CURRENT_BUILD_ROUND++;

        CreateBox.start();

        final int calculateTime = PlayerDataUtil.getOnlinePlayers() * 3;

        ConfigUtil.sendMessageBroadCast("GAME.MAP-CREATE-STARTED", "%tick%|" + calculateTime);

        Bukkit.getScheduler().runTaskLater(BuildingWordBattle.INSTANCE, () -> {
            ConfigUtil.sendMessageBroadCast("GAME.MAP-CREATE-ENDED");

            switch (state) {
                case THEME:
                    Theme.receive();
                    break;
                case BUILDING:
                    Build.receive(true);
                    break;
            }
        }, calculateTime);
    }


    public static void startShuffle(GameStateEnum game) {

        Map<UUID, Integer> shuffle = shuffleWhile();

        if (shuffle != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerData data = PlayerDataUtil.getPlayerData(player);

                if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

                    final int mapID = shuffle.get(player.getUniqueId());

                    data.getMapManager().addPlayedMap(mapID);

                    AreaCreator areaCreator = GameInfo.areaCreator.get(mapID + "-" + GameInfo.CURRENT_BUILD_ROUND);

                    switch (game) {
                        case BUILDING: {

                            areaCreator.setAuthor(player.getName());
                            areaCreator.setAuthorUUID(player.getUniqueId());

                            ConfigUtil.sendChat(player, "GAME.GAME-ACTIONBAR", "%theme%|" + areaCreator.getTheme());
                            break;
                        }
                        case ANSWER: {

                            areaCreator.setAnswerPlayer(player.getName());
                            areaCreator.setAnswerUUID(player.getUniqueId());

                            AreaCreator tempAreaCreator = BuildingWordUtility.getMap(mapID);

                            data.getMapManager().setAnswerMapName(areaCreator.getMapName());

                            if (tempAreaCreator != null) {
                                areaCreator = tempAreaCreator;
                            }
                            break;
                        }
                    }

                    World world = Bukkit.getWorld("world");


                    Vector middle = areaCreator.getMiddle();

                    BuildingWordUtility.mainThreadTeleport(player, new Location(world, middle.getX(), middle.getY(), middle.getZ()));
                }
            }

        } else {

            Bukkit.getLogger().severe("通常では実行されないことが実行されたそうですよ");
            Bukkit.getLogger().severe("(多分) エラーによりスペクテイターモードに移行します");

            Spec.start();
        }
    }

    private static Map<UUID, Integer> shuffleWhile() {
        int a = 0;

        while (true) {
            int temp = 0;
            a++;

            //a > 100回行くことはあり得ない、 もし運が悪かったらあるかも。
            if (a > 100) {
                return null;
            }

            Map<UUID, Integer> hashmap = new HashMap<>();

            List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
            Collections.shuffle(onlinePlayers);

            int players = (int) (PlayerDataUtil.getOnlinePlayers() / 1.5);

            List<Integer> cloneMapList = new ArrayList<>(GameInfo.CURRENT_MAP_LIST);
            for (Player player : onlinePlayers) {

                PlayerData data = PlayerDataUtil.getPlayerData(player);

                if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

                    int foundNumber = 0;

                    List<Integer> playedMap = data.getMapManager().getMapList();

                    for (int i : cloneMapList) {
                        if (!playedMap.contains(i)) {
                            cloneMapList.remove((Integer) i);
                            foundNumber = i;
                            break;
                        }
                    }

                    if (foundNumber == 0) {
                        if (temp++ >= players) {
                            return null;
                        }
                    }

                    if (temp == 0) {
                        hashmap.put(player.getUniqueId(), foundNumber);
                    }
                }
            }
            if (temp == 0) {
                return hashmap;
            }
        }
    }
}