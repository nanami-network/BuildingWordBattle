package me.koutachan.buildingwordbattle;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BuildingWordBattle extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(new BukkitEvent(),this);
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
