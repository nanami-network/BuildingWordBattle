package me.koutachan.buildingwordbattle.game.gameutil;

import lombok.experimental.UtilityClass;
import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.game.CreateBox;
import me.koutachan.buildingwordbattle.game.GameInfo;
import me.koutachan.buildingwordbattle.game.enums.GameEnum;
import me.koutachan.buildingwordbattle.game.enums.GameStateEnum;
import me.koutachan.buildingwordbattle.game.main.Answer;
import me.koutachan.buildingwordbattle.game.main.Build;
import me.koutachan.buildingwordbattle.game.main.Spectator;
import me.koutachan.buildingwordbattle.game.main.Theme;
import me.koutachan.buildingwordbattle.map.AreaCreator;
import me.koutachan.buildingwordbattle.playerdata.PlayerData;
import me.koutachan.buildingwordbattle.playerdata.PlayerDataUtil;
import me.koutachan.buildingwordbattle.playerdata.impl.enums.TeamEnum;
import me.koutachan.buildingwordbattle.util.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

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

            PlayerData data = areaCreator.getAuthorData();

            if (data != null && data.getPlayer().isOnline() && data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {
                return areaCreator;

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
        Spectator.time = 0;
        Spectator.count = 0;
        Spectator.round = 0;

        ConfigUtil.sendMessageBroadCast("GAME.PLAYER-DATA-RECREATE");

        PlayerDataUtil.clear();

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
        try {
            final Map<PlayerData, Integer> shuffle = cleanWhile();

            for (Map.Entry<PlayerData, Integer> entry : shuffle.entrySet()) {

                PlayerData data = entry.getKey();

                if (data != null
                        && data.getPlayer().isOnline()
                        && data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {
                    final int mapID = entry.getValue();

                    data.getMapManager().addPlayedMap(mapID);

                    AreaCreator areaCreator = GameInfo.areaCreator.get(mapID + "-" + GameInfo.CURRENT_BUILD_ROUND);

                    switch (game) {
                        case BUILDING: {
                            areaCreator.setAuthorData(data);

                            ConfigUtil.sendChat(data.getPlayer(), "GAME.GAME-ACTIONBAR", "%theme%|" + areaCreator.getTheme());

                            break;
                        }
                        case ANSWER: {
                            areaCreator.setAnswerData(data);

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

                    BuildingWordUtility.mainThreadTeleport(data.getPlayer(), new Location(world, middle.getX(), middle.getY(), middle.getZ()));
                }
            }

        } catch (Exception ex) {
            Bukkit.getLogger().severe("通常では実行されないことが実行されたそうですよ");
            Bukkit.getLogger().severe("(多分) エラーによりスペクテイターモードに移行します");

            Bukkit.getLogger().severe("---開発者に報告してください！---");
            ex.printStackTrace();
            Bukkit.getLogger().severe("---開発者に報告してください！---");

            Spectator.start();
        }
    }

    public Map<PlayerData, Integer> cleanWhile() {
        Map<PlayerData, Pair<PlayerData, List<Integer>>> availableMap = new HashMap<>();

        for (PlayerData data : PlayerDataUtil.getOnlinePlayersData()) {
            final ArrayList<Integer> integers = new ArrayList<>(GameInfo.CURRENT_MAP_LIST);
            integers.removeAll(data.getMapManager().getMapList());

            availableMap.put(data, new Pair<>(data, integers));
        }

        Map<PlayerData, Integer> finalResults = new HashMap<>();

        Map<Long, List<Pair<PlayerData, List<Integer>>>> doubleLists = new HashMap<>();

        for (Map.Entry<PlayerData, Pair<PlayerData, List<Integer>>> integers1 : availableMap.entrySet()) {

            long totals = 0;

            for (Pair<PlayerData, List<Integer>> integers2 : availableMap.values()) {

                if (integers1.getValue() != integers2) {
                    totals += isContains(integers1, integers2);
                }
            }

            final List<Pair<PlayerData, List<Integer>>> values = doubleLists.get(totals);

            integers1.getValue().getValue().sort(Integer::compare);

            if (values != null) {
                values.add(integers1.getValue());
            } else {
                doubleLists.put(totals, new ArrayList<>(Collections.singletonList(integers1.getValue())));
            }
        }

        //fast
        final boolean isKeyMatched = doubleLists.size() == 1;

        if (isKeyMatched) {
            final long key = (Long) doubleLists.keySet().toArray()[0];

            for (int i = 0; i < availableMap.size(); i++) {
                final Pair<PlayerData, List<Integer>> finals = doubleLists.get(key).get(i);

                if (!put(finals.getKey(), finals.getValue(), finalResults)) throw new IllegalStateException("invalid List.");
            }
        } else {
            List<Pair<PlayerData, List<Integer>>> temporary = new ArrayList<>();

            for (List<Pair<PlayerData, List<Integer>>> possibly : doubleLists.values()) {
                for (int i = 0; i < possibly.size(); i++) {

                    Pair<PlayerData, List<Integer>> it = possibly.get(i);

                    possibly.forEach(list -> {
                        if (it != list) {
                            it.getValue().removeAll(list.getValue());
                        }
                    });

                    if (it.getValue().isEmpty()) {
                        throw new IllegalStateException("empty List.");
                    } else {
                        temporary.add(it);
                    }
                }
            }

            temporary.sort(Comparator.comparingInt(o -> o.getValue().size()));

            for (Pair<PlayerData, List<Integer>> list : temporary) {
                if (!put(list.getKey(), list.getValue(), finalResults)) throw new IllegalStateException("invalid List.");
            }
        }

        return finalResults;
    }

    public static boolean put(PlayerData key, final List<Integer> list, final Map<PlayerData, Integer> target) {
        for (int value : list) {
            if (!target.containsValue(value)) {
                target.put(key, value);

                return true;
            }
        }
        return false;
    }

    private static long isContains(Map.Entry<PlayerData, Pair<PlayerData, List<Integer>>> entry, Pair<PlayerData, List<Integer>> integers) {
        return integers.getValue().stream().filter(integer -> entry.getValue().getValue().contains(integer)).count();
    }

    /**
     * 新しいシステム用
     * @since - 1.3
     */
    public int getTime() {
        switch (GameInfo.gameState) {
            case BUILDING: {
                return Build.time;
            }
            case ANSWER: {
                return Answer.time;
            }
            case THEME: {
                return Theme.time;
            }
            default:
            case SPEC:
            case NULL: {
                return 0;
            }
        }
    }
}