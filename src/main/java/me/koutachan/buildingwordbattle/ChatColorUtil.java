package me.koutachan.buildingwordbattle;

import org.bukkit.ChatColor;

public class ChatColorUtil {

    public static String translateAlternateColorCodes(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}