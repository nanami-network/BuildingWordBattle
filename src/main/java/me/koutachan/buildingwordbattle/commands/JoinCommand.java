package me.koutachan.buildingwordbattle.commands;

import me.koutachan.buildingwordbattle.playerdata.PlayerData;
import me.koutachan.buildingwordbattle.playerdata.PlayerDataUtil;
import me.koutachan.buildingwordbattle.playerdata.impl.Enum.TeamEnum;
import me.koutachan.buildingwordbattle.util.ConfigUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class JoinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;
            PlayerData data = PlayerDataUtil.getPlayerData(player);

            if (data.getTeamManager().getCurrentTeam() != TeamEnum.PLAYER) {

                if (args.length > 0) {
                    final Optional<PlayerData> found = PlayerDataUtil.getQuitPlayers().stream().filter(r -> r.getPlayer().getUniqueId().toString().equalsIgnoreCase(args[0]))
                            .findFirst();

                    if (found.isPresent()) {
                        ConfigUtil.sendChat(sender, "JOIN-COMMAND.FOUND");

                        PlayerDataUtil.set(found.get(), data);
                    } else {
                        ConfigUtil.sendChat(sender, "JOIN-COMMAND.NOT-FOUND");
                    }
                } else {
                    ConfigUtil.sendChat(sender, "JOIN-COMMAND.MISSING-USAGE");
                }
            } else {
                ConfigUtil.sendChat(sender, "JOIN-COMMAND.CANNOT-USABLE-PLAYER");
            }
        } else {
            ConfigUtil.sendChat(sender, "JOIN-COMMAND.CANNOT-USABLE");
        }

        return true;
    }
}