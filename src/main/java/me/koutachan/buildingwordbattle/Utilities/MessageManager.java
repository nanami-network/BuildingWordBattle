package me.koutachan.buildingwordbattle.Utilities;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageManager {

    private static YamlConfiguration messageConfig;
    private static String prefix;

    public static String getString(String key) {
        return messageConfig.getString(key);
    }

    public static String getStringColor(String key) {
        return ChatColorUtility.translateAlternateColorCodes(messageConfig.getString(key).replaceAll("%prefix%", prefix));
    }

    public static void reload() {
        messageConfig = YamlConfiguration.loadConfiguration(new File(BuildingWordBattle.INSTANCE.getDataFolder(), "message.yml"));
    }

    public static void init() {
        if (!new File(BuildingWordBattle.INSTANCE.getDataFolder(), "message.yml").exists())
            BuildingWordBattle.INSTANCE.saveResource("message.yml", false);

        reload();

        prefix = getStringColor("prefix");
    }
}