package me.koutachan.buildingwordbattle;

import lombok.experimental.UtilityClass;
import me.koutachan.buildingwordbattle.Commands.debug;
import me.koutachan.buildingwordbattle.Timer.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.Utility;
import org.bukkit.plugin.java.JavaPlugin;

import javax.xml.bind.annotation.XmlType;

public final class BuildingWordBattle extends JavaPlugin {

    public static BuildingWordBattle INSTANCE;

    public Scheduler scheduler;

    @Override
    public void onEnable() {
        INSTANCE = this;
        Bukkit.getServer().getPluginManager().registerEvents(new BukkitEvent(), this);
        getCommand("debug").setExecutor(new debug());

        this.scheduler = new Scheduler();
        scheduler.start();
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}