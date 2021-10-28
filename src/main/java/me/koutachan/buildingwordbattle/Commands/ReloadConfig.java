package me.koutachan.buildingwordbattle.Commands;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.ChatColorUtil;
import me.koutachan.buildingwordbattle.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadConfig implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        BuildingWordBattle.INSTANCE.reloadConfig();
        MessageManager.reload();

        sender.sendMessage(ChatColorUtil.translateAlternateColorCodes("&bConfig / Message ファイルを再読み込みしました"));

        return true;
    }
}
