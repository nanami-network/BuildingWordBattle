package me.koutachan.buildingwordbattle;

import me.koutachan.buildingwordbattle.Commands.debug;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.xml.bind.annotation.XmlType;

public final class BuildingWordBattle extends JavaPlugin {

    public static BuildingWordBattle INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        Bukkit.getServer().getPluginManager().registerEvents(new BukkitEvent(),this);
        getCommand("debug").setExecutor(new debug());
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}