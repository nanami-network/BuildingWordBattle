package me.koutachan.buildingwordbattle;

import me.koutachan.buildingwordbattle.Commands.AdminCommand;
import me.koutachan.buildingwordbattle.Commands.ReloadConfig;
import me.koutachan.buildingwordbattle.Commands.Start;
import me.koutachan.buildingwordbattle.Commands.debug;
import me.koutachan.buildingwordbattle.Timer.Scheduler;
import me.koutachan.buildingwordbattle.Utilities.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.plugin.java.JavaPlugin;

public final class BuildingWordBattle extends JavaPlugin {

    public static BuildingWordBattle INSTANCE;

    public Scheduler scheduler;

    @Override
    public void onEnable() {

        INSTANCE = this;

        saveDefaultConfig();

        MessageManager.init();

        Bukkit.getConsoleSender().sendMessage(MessageManager.getStringColor("firstMessage"));

        Bukkit.getServer().getPluginManager().registerEvents(new BukkitEvent(), this);
        getCommand("debug").setExecutor(new debug());
        getCommand("start").setExecutor(new Start());
        getCommand("admin").setExecutor(new AdminCommand());
        getCommand("reloadConfig").setExecutor(new ReloadConfig());

        Bukkit.getWorld("world").setDifficulty(Difficulty.PEACEFUL);

        this.scheduler = new Scheduler();
        scheduler.start();
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}