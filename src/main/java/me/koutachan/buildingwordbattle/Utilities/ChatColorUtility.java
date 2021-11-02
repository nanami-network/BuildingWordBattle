package me.koutachan.buildingwordbattle.Utilities;

import org.bukkit.ChatColor;

public class ChatColorUtility {

    public static String translateAlternateColorCodes(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}