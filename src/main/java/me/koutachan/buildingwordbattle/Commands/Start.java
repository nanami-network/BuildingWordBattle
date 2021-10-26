package me.koutachan.buildingwordbattle.Commands;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.ChatColorUtil;
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

        int firstTime = time;

        bukkitTask = Bukkit.getScheduler().runTaskTimer(BuildingWordBattle.INSTANCE, () -> {

            if (time <= 0) {
                Bukkit.broadcastMessage("ゲーム開始！");
                bukkitTask.cancel();
                return;
            }

            message(firstTime, time);
            time--;
        }, 0, 20);
        return false;
    }

    private int parseInt(String parse) {
        try {
            return Integer.parseInt(parse);
        } catch (Exception e) {
            return 10;
        }
    }

    private void message(int firstTime, int sec) {

        String colorCode = "&a";

        if (sec <= Math.round((double) firstTime / 3)) colorCode = "&c";
        else if (sec <= Math.round((double) firstTime / 1.5)) colorCode = "&e";

        Bukkit.broadcastMessage(ChatColorUtil.translateAlternateColorCodes(String.format("%sゲームは %s 秒後に開始されます", colorCode, sec)));
        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 1f);
            } catch (NoSuchFieldError e) {
                player.playSound(player.getLocation(), Sound.valueOf("NOTE_PLING"), 1f, 1f);
            }
        }
    }
}