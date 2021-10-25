package me.koutachan.buildingwordbattle.ConfigCache;

import me.koutachan.buildingwordbattle.BuildingWordBattle;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigCache {
    private static final Map<String, Object> configCache = new HashMap<>();

    public static Object getObject(File file, String key) {
        Object cache = configCache.get(key);

        if (cache != null) {
            return cache;
        }

        fileExists(file);

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        Object obj = yaml.get(key);

        configCache.put(key, obj);

        return obj;
    }

    public static Object getString(File file, String key) {
        Object cache = configCache.get(key);

        if (cache != null) {
            return cache;
        }

        fileExists(file);

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        String str = yaml.getString(key);

        configCache.put(key, str);

        return str;
    }


    public static void clearCache() {
        configCache.clear();
    }

    private static void fileExists(final File file) {
        BuildingWordBattle.INSTANCE.saveResource(file.getName(), false);
    }
}