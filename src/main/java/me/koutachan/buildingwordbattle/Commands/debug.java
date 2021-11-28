package me.koutachan.buildingwordbattle.Commands;

import me.koutachan.buildingwordbattle.Game.CreateBox;
import me.koutachan.buildingwordbattle.Game.GameInfo;
import me.koutachan.buildingwordbattle.Map.AreaCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class debug implements CommandExecutor {

    private AreaCreator mapManager;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            GameInfo.mapList.forEach(i -> sender.sendMessage(String.valueOf(CreateBox.areaCreatorMap.get(i + "-" + "1").getMapName())));
        }
        return true;
    }
}