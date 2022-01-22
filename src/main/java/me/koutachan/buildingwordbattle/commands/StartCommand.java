package me.koutachan.buildingwordbattle.commands;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.game.GameInfo;
import me.koutachan.buildingwordbattle.game.gameEnum.GameEnum;
import me.koutachan.buildingwordbattle.game.system.Theme;
import me.koutachan.buildingwordbattle.playerdata.PlayerData;
import me.koutachan.buildingwordbattle.playerdata.PlayerDataUtil;
import me.koutachan.buildingwordbattle.playerdata.impl.Enum.TeamEnum;
import me.koutachan.buildingwordbattle.util.ConfigUtil;
import me.koutachan.buildingwordbattle.util.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class StartCommand implements CommandExecutor {

    private int time = 0;

    private static BukkitTask bukkitTask;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (GameInfo.gameInfo == GameEnum.LOBBY) {

            if (args.length == 0) time = 10;
            else time = parseInt(args[0]);

            //色が変わる時間を計算
            int a = (int) Math.round((double) time / 3);
            int b = (int) Math.round((double) time / 1.5);

            GameInfo.gameInfo = GameEnum.STARTING;

            bukkitTask = Bukkit.getScheduler().runTaskTimer(BuildingWordBattle.INSTANCE, () -> {
                if (time <= 0) {
                    bukkitTask.cancel();
                    ConfigUtil.sendMessageBroadCast("START-COMMAND.START-MESSAGE");

                    for (Player player : Bukkit.getOnlinePlayers()) {

                        PlayerData data = PlayerDataUtil.getPlayerData(player);

                        if (data.getTeamManager().getCurrentTeam() != TeamEnum.ADMIN) {
                            data.getTeamManager().setCurrentTeam(TeamEnum.PLAYER);
                        }
                    }

                    GameInfo.gameInfo = GameEnum.GAME;
                    //お題設定開始
                    Theme.start();

                    ConfigUtil.sendMessageBroadCast("START-COMMAND.TIP-MESSAGE");

                    GameInfo.BOX_MAX_VALUE = PlayerDataUtil.getOnlinePlayers();
                } else {
                    message(time, a, b);
                    time--;
                }
            }, 0, 20);
        } else {
            sender.sendMessage("現在使えません。");
        }

        return true;
    }

    private int parseInt(String parse) {
        try {
            return Integer.parseInt(parse);
        } catch (NumberFormatException e) {
            return 10;
        }
    }

    private void message(int sec, int a, int b) {

        String colorCode = MessageManager.getString("START-COMMAND.COLORCODE-A");

        if (sec <= a) colorCode = MessageManager.getString("START-COMMAND.COLORCODE-C");
        else if (sec <= b) colorCode = MessageManager.getString("START-COMMAND.COLORCODE-B");


        ConfigUtil.sendMessageBroadCast("START-COMMAND.COUNT-MESSAGE", "%colorcode%|" + colorCode, "%time%|" + time);

        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 1f);
            } catch (NoSuchFieldError e) {
                player.playSound(player.getLocation(), Sound.valueOf("NOTE_PLING"), 1f, 1f);
            }
        }
    }

    public static void stop() {
        if (bukkitTask != null) {
            bukkitTask.cancel();
            bukkitTask = null;
        }
    }
}