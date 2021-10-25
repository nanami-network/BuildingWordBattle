package me.koutachan.buildingwordbattle.Commands;

import me.koutachan.buildingwordbattle.BoxCreator;
import me.koutachan.buildingwordbattle.Map.MapManager;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class debug implements CommandExecutor {

    private MapManager mapManager;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Chunk onLoc = player.getLocation().getChunk();
            player.sendMessage(String.format("x=%s z=%s", onLoc.getX(), onLoc.getZ()));
            Location location = player.getLocation().getChunk().getBlock(8, 0, 8).getLocation(); //center is 8?8? or 7?7?
            //player.getChunk();
            player.sendMessage("center!:" + location);
            BoxCreator boxCreator = new BoxCreator(location);
            boxCreator.CreateCube(Material.BEDROCK, -30, 30, 0, 30, -30, 30, true);


            if (mapManager == null) {
                this.mapManager = new MapManager(location.getBlockX() - 29, location.getBlockX() + 29, 1, 29, location.getBlockZ() - 29, location.getBlockZ() + 29);
            }
            player.sendMessage(String.valueOf(mapManager.isArea(player.getLocation())));
        }
        return true;
    }
}