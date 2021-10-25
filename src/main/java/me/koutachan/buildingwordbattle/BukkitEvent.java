package me.koutachan.buildingwordbattle;

import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitEvent implements Listener {
    @EventHandler
    public void JoinEvent(PlayerJoinEvent e) {
        PlayerDataUtil.createPlayerData(e.getPlayer());
    }

    @EventHandler
    public void QuitEvent(PlayerQuitEvent e) {
        PlayerDataUtil.removePlayerData(e.getPlayer());
    }
}