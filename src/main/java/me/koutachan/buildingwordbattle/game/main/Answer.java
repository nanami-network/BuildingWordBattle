package me.koutachan.buildingwordbattle.game.main;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.game.GameInfo;
import me.koutachan.buildingwordbattle.game.enums.GameStateEnum;
import me.koutachan.buildingwordbattle.game.gameutil.BuildingWordUtility;
import me.koutachan.buildingwordbattle.map.AreaCreator;
import me.koutachan.buildingwordbattle.playerdata.PlayerData;
import me.koutachan.buildingwordbattle.playerdata.PlayerDataUtil;
import me.koutachan.buildingwordbattle.playerdata.impl.enums.TeamEnum;
import me.koutachan.buildingwordbattle.util.ConfigUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Answer {

    public static int time;
    public static boolean ended;

    public static void run() {
        if (time <= 0 && !ended) {
            next();
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerData data = PlayerDataUtil.getPlayerData(player);

                if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

                    AreaCreator areaCreator = GameInfo.areaCreator.get(data.getMapManager().getAnswerMapName());

                    String answer = areaCreator.getAnswer() != null ? areaCreator.getAnswer() : "回答無し";

                    try {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ConfigUtil.message("GAME.ANSWER-ACTIONBAR", "%answer%|" + answer)));
                    } catch (NoSuchFieldError ignore) {
                    }
                } else {
                    //TODO: 詳細を表示
                }
            }
            time--;
        }
    }

    public static void start() {
        GameInfo.CURRENT_ROUND++;

        if (!BuildingWordUtility.checkEnd()) {
            time = BuildingWordBattle.INSTANCE.getConfig().getInt("answerTime");

            ended = false;

            ConfigUtil.sendMessageBroadCast("GAME.ANSWER-STARTED");

            GameInfo.gameState = GameStateEnum.ANSWER;

            BuildingWordUtility.startShuffle(GameStateEnum.ANSWER);
        } else {
            Spectator.start();
        }
    }

    public static void next() {
        ended = true;

        PlayerDataUtil.clearQuitPlayers();
        PlayerDataUtil.deleteOfflinePlayerData();

        Build.start(true);
    }
}