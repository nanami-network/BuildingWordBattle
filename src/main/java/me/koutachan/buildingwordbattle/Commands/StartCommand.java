package me.koutachan.buildingwordbattle.Commands;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.Utilities.ChatUtil;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameEnum;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.PlayerData.impl.Enum.TeamEnum;
import me.koutachan.buildingwordbattle.Timer.Scheduler;
import me.koutachan.buildingwordbattle.Utilities.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class StartCommand implements CommandExecutor {

    private int time = 0;

    private BukkitTask bukkitTask;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (GameInfo.gameState == GameEnum.LOBBY) {

            if (args.length == 0) time = 10;
            else time = parseInt(args[0]);

            int a = (int) Math.round((double) time / 3);
            int b = (int) Math.round((double) time / 1.5);

            GameInfo.gameState = GameEnum.STARTING;

            bukkitTask = Bukkit.getScheduler().runTaskTimer(BuildingWordBattle.INSTANCE, () -> {

                if (time <= 0) {
                    ChatUtil.sendMessageBroadCast("STARTCOMMAND.START_MESSAGE");
                    //Bukkit.broadcastMessage(MessageManager.getString("startCommand.startMessage"));
                    bukkitTask.cancel();

                    for (Player player : Bukkit.getOnlinePlayers()) {

                        PlayerData data = PlayerDataUtil.getPlayerData(player);

                        if (data.getTeamManager().getCurrentTeam() != TeamEnum.ADMIN) {
                            data.getTeamManager().setCurrentTeam(TeamEnum.PLAYER);
                        }
                    }

                    //CreateBox.start();

                    GameInfo.gameState = GameEnum.GAME;
                    GameInfo.nowState = GameStateEnum.THEME;

                    Scheduler.themeTime = BuildingWordBattle.INSTANCE.getConfig().getInt("themeTime");

                    ChatUtil.sendMessageBroadCast("STARTCOMMAND.TIP_MESSAGE");
                    //Bukkit.broadcastMessage(MessageManager.getString("startCommand.tipMessage"));

                    return;
                }

                message(time, a, b);
                time--;
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

        String colorCode = MessageManager.getString("STARTCOMMAND.COLORCODE_A");

        if (sec <= a) colorCode = MessageManager.getString("STARTCOMMAND.COLORCODE_C");
        else if (sec <= b) colorCode = MessageManager.getString("STARTCOMMAND.COLORCODE_B");


        ChatUtil.sendMessageBroadCast("STARTCOMMAND.COUNT_MESSAGE", "%colorcode%|" + colorCode, "%time%|" + time);
        //Bukkit.broadcastMessage(ChatUtil.translateAlternateColorCodes(MessageManager.getString("startCommand.countMessage").replaceAll("%colorcode%", colorCode).replaceAll("%time%", String.valueOf(sec))));
        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 1f);
            } catch (NoSuchFieldError e) {
                player.playSound(player.getLocation(), Sound.valueOf("NOTE_PLING"), 1f, 1f);
            }
        }
    }
}