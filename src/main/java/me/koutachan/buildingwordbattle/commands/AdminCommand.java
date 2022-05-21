package me.koutachan.buildingwordbattle.commands;

import me.koutachan.buildingwordbattle.game.GameInfo;
import me.koutachan.buildingwordbattle.util.ConfigUtil;
import me.koutachan.buildingwordbattle.playerdata.PlayerData;
import me.koutachan.buildingwordbattle.playerdata.PlayerDataUtil;
import me.koutachan.buildingwordbattle.playerdata.impl.enums.TeamEnum;
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

            if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {
                //回すマップリストから消す
                GameInfo.CURRENT_MAP_LIST.remove((Integer) data.getThemeManager().getThemeMap());
            }

            if (data.getTeamManager().getCurrentTeam() != TeamEnum.ADMIN) {
                data.getTeamManager().setCurrentTeam(TeamEnum.ADMIN);
                player.sendMessage(ConfigUtil.translateAlternateColorCodes("&eチームを 管理者 に変更しました"));
            } else {
                data.getTeamManager().setCurrentTeam(TeamEnum.SPECTATOR);
                player.sendMessage(ConfigUtil.translateAlternateColorCodes("&eチームを 観戦 に変更しました"));
            }
        }
        return true;
    }
}