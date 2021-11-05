package me.koutachan.buildingwordbattle.Commands;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.Utilities.ChatColorUtility;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameEnum;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.Game.GameInfo;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.PlayerData.impl.Enum.TeamEnum;
import me.koutachan.buildingwordbattle.Timer.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class Start implements CommandExecutor {

    private int time = 0;

    private BukkitTask bukkitTask;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) time = 10;
        else time = parseInt(args[0]);

        a = (int) Math.round((double) time / 3);
        b = (int) Math.round((double) time / 1.5);

        GameInfo.gameState = GameEnum.STARTING;

        bukkitTask = Bukkit.getScheduler().runTaskTimer(BuildingWordBattle.INSTANCE, () -> {

            if (time <= 0) {
                Bukkit.broadcastMessage("ゲーム開始！");
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
                Bukkit.broadcastMessage(ChatColorUtility.translateAlternateColorCodes("&e[豆知識] チャットにお題の内容を入力することでお題を設定できます！"));

                return;
            }

            message(time);
            time--;
        }, 0, 20);
        return false;
    }

    private int parseInt(String parse) {
        try {
            return Integer.parseInt(parse);
        } catch (NumberFormatException e) {
            return 10;
        }
    }

    private int a, b;

    private void message(int sec) {

        String colorCode = "&a";

        if (sec <= a) colorCode = "&c";
        else if (sec <= b) colorCode = "&e";

        Bukkit.broadcastMessage(ChatColorUtility.translateAlternateColorCodes(String.format("%sゲームは %s 秒後に開始されます", colorCode, sec)));
        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 1f);
            } catch (NoSuchFieldError e) {
                player.playSound(player.getLocation(), Sound.valueOf("NOTE_PLING"), 1f, 1f);
            }
        }
    }
}