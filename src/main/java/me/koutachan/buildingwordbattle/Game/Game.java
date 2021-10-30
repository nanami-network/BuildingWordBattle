package me.koutachan.buildingwordbattle.Game;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.ChatColorUtil;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.Map.AreaCreator;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.PlayerData.impl.TeamEnum.TeamEnum;
import me.koutachan.buildingwordbattle.Timer.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public class Game {
    public static void startShuffle() {

        Map<UUID, Integer> shuffle = shuffleWhile();

        if (shuffle != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerData data = PlayerDataUtil.getPlayerData(player);

                if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

                    int mapID = shuffle.get(player.getUniqueId());

                    data.getMapManager().addMap(mapID);

                    AreaCreator areaCreator = CreateBox.areaCreatorMap.get(mapID + "-" + GameInfo.buildRound);

                    switch (GameInfo.nowState) {
                        case BUILDING: {
                            GameInfo.round++;

                            areaCreator.setAuthor(player.getName());
                            areaCreator.setAuthorUUID(player.getUniqueId());

                            player.sendMessage(ChatColorUtil.translateAlternateColorCodes(String.format("&6お題: %s", areaCreator.getTheme())));
                            break;
                        }
                        case ANSWER: {
                            GameInfo.round++;

                            areaCreator.setAnswerPlayer(player.getName());
                            areaCreator.setAnswerUUID(player.getUniqueId());
                        }
                    }

                    World world = Bukkit.getWorld("world");


                    Vector vector = new Vector(areaCreator.getXMax(), areaCreator.getYMax(), areaCreator.getZMax()).getMidpoint(new Vector(areaCreator.getXMin(), areaCreator.getYMin(), areaCreator.getZMin()));

                    player.teleport(new Location(world, vector.getX(), vector.getY(), vector.getZ()));
                }
            }

            //カウントダウン開始

        } else {
            //ゲーム終了
        }
    }

    private static Map<UUID, Integer> shuffleWhile() {
        int a = 0;

        while (true) {
            int temp = 0;
            a++;

            //Bukkit.broadcastMessage("ループ回数: " + a);

            //a > 100回行くことはあり得ない、 もし運が悪かったらあるかも。
            if (a > 100) {
                return null;
            }

            Map<UUID, Integer> hashmap = new HashMap<>();

            List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
            Collections.shuffle(onlinePlayers);

            int players = (int) (PlayerDataUtil.getOnlinePlayers() / 1.5);

            List<Integer> cloneMapList = new ArrayList<>(GameInfo.mapList);
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

    //ここでの1ラウンドは 回答 又は 建築で１ラウンドとする
    public static int getMaxRound() {
        int onlinePlayers = PlayerDataUtil.getOnlinePlayers();

        // onlinePlayers
        // - 1
        // 奇数 - 1
        // 奇数の場合、建築の後回答できないため減らす。

        int maxRound = onlinePlayers - 1;
        boolean i = maxRound % 2 != 0;

        if (i) maxRound -= 1;

        return maxRound;
    }

    public static void startAnswer() {
        GameInfo.nowState = GameStateEnum.ANSWER;

        startShuffle();

        Bukkit.broadcastMessage("回答タイム！");

        Scheduler.answerTime = BuildingWordBattle.INSTANCE.getConfig().getInt("answerTime");
    }
}