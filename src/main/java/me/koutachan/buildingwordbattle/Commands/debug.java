package me.koutachan.buildingwordbattle.Commands;

import me.koutachan.buildingwordbattle.BoxCreator;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class debug implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            new BoxCreator(player.getLocation()).CreateCube(Material.BEDROCK, 0, 30, 0, 30, 0, 30);
        }
        return true;
    }
}