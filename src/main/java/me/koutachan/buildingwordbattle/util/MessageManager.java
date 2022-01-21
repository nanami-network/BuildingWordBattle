package me.koutachan.buildingwordbattle.util;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class MessageManager {

    private static YamlConfiguration messageConfig;
    private static String prefix;

    public static Object get(String key) {
        return messageConfig.get(key);
    }

    public static boolean getBoolean(String key) {
        return messageConfig.getBoolean(key);
    }

    public static String getString(String key) {
        return ConfigUtil.translateAlternateColorCodes(messageConfig.getString(key).replaceAll("%prefix%", prefix));
    }

    public static List<String> getStringList(String key) {
        return messageConfig.getStringList(key);
    }

    public static void reload() {
        messageConfig = YamlConfiguration.loadConfiguration(new File(BuildingWordBattle.INSTANCE.getDataFolder(), "message.yml"));

        prefix = getString("prefix");
    }

    public static void init() {
        if (!new File(BuildingWordBattle.INSTANCE.getDataFolder(), "message.yml").exists())
            BuildingWordBattle.INSTANCE.saveResource("message.yml", false);
        reload();
    }
}