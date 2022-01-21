package me.koutachan.buildingwordbattle.game;

import me.koutachan.buildingwordbattle.game.gameEnum.GameEnum;
import me.koutachan.buildingwordbattle.game.gameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.map.AreaCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * プレイヤーデータ
 * @see me.koutachan.buildingwordbattle.playerdata.PlayerDataUtil
 * @see me.koutachan.buildingwordbattle.playerdata.PlayerData
 *
 * ゲームの管理情報はここです
 */
public class GameInfo {
    public static GameEnum gameInfo = GameEnum.LOBBY;
    public static GameStateEnum gameState = GameStateEnum.NULL;

    //箱の生成数の制御
    public static int BOX_MAX_VALUE;

    //回すマップのリスト
    public static List<Integer> CURRENT_MAP_LIST = new ArrayList<>();

    //現在のラウンド
    public static int CURRENT_ROUND;
    public static int CURRENT_BUILD_ROUND;
    //ラウンドの最大を計算
    public static int CALCULATE_MAX_ROUND;
    //ラウンドの最大を計算 (表示用)
    public static int CALCULATE_MAX_ROUND_SHOW;

    //エリアの情報
    public static Map<String, AreaCreator> areaCreator = new HashMap<>();
}