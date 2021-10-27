package me.koutachan.buildingwordbattle.Commands;

import me.koutachan.buildingwordbattle.BoxCreator;
import me.koutachan.buildingwordbattle.Map.AreaCreator;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class debug implements CommandExecutor {

    private AreaCreator mapManager;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Chunk onLoc = player.getLocation().getChunk();
            player.sendMessage(String.format("x=%s z=%s", onLoc.getX(), onLoc.getZ()));
            Location location = player.getLocation(); //center is 8?8? or 7?7?
            //player.getChunk();
            player.sendMessage("center!:" + location);
            BoxCreator boxCreator = new BoxCreator(location);


            boxCreator.CreateCube(Material.BEDROCK, -100, 100, 0, 100, -100, 100, true);
            //boxCreator.CreateCube(Material.BEDROCK, -30, 30, 0, 30, -30, 30, true);


            if (mapManager == null) {

                Location l1 = location.clone().add(-299, 299, -299);
                Location l2 = location.clone().add(299, 1, 299);

                this.mapManager = new AreaCreator(null, null, "0", l1, l2);
            }
            player.sendMessage(String.valueOf(mapManager.isArea(player.getLocation())));
        }
        return true;
    }
}