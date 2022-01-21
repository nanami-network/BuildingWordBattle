package me.koutachan.buildingwordbattle.commands;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.util.ConfigUtil;
import me.koutachan.buildingwordbattle.util.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadConfigCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        BuildingWordBattle.INSTANCE.reloadConfig();
        MessageManager.reload();

        sender.sendMessage(ConfigUtil.translateAlternateColorCodes("&bConfig / Message ファイルを再読み込みしました"));

        return true;
    }
}
