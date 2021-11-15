package me.koutachan.buildingwordbattle.Commands;

import me.koutachan.buildingwordbattle.Utilities.ChatUtil;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.PlayerData.impl.Enum.TeamEnum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;

            PlayerData data = PlayerDataUtil.getPlayerData(player);

            data.getTeamManager().setCurrentTeam(TeamEnum.ADMIN);
            player.sendMessage(ChatUtil.translateAlternateColorCodes("&eチームを Admin に変更しました"));
        }
        return true;
    }
}