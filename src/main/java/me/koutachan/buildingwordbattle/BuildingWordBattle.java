package me.koutachan.buildingwordbattle;

import me.koutachan.buildingwordbattle.commands.*;
import me.koutachan.buildingwordbattle.timer.Scheduler;
import me.koutachan.buildingwordbattle.util.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public final class BuildingWordBattle extends JavaPlugin {

    public static BuildingWordBattle INSTANCE;

    public Scheduler scheduler = new Scheduler();

    @Override
    public void onEnable() {

        INSTANCE = this;

        saveDefaultConfig();

        MessageManager.init();

        Bukkit.getConsoleSender().sendMessage(MessageManager.getString("firstMessage"));

        Bukkit.getServer().getPluginManager().registerEvents(new BukkitEvent(), this);
        getCommand("start").setExecutor(new StartCommand());
        getCommand("admin").setExecutor(new AdminCommand());
        getCommand("reloadConfig").setExecutor(new ReloadConfigCommand());
        getCommand("change-mode").setExecutor(new ChangeModeCommand());
        getCommand("forcestop").setExecutor(new ForceStopCommand());

        World world = Bukkit.getWorld("world");
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setGameRuleValue("doWeatherCycle", "false");
        world.setStorm(false);
        scheduler.start();
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}