package me.koutachan.buildingwordbattle.util;

import lombok.experimental.UtilityClass;
import me.koutachan.buildingwordbattle.playerdata.PlayerData;
import me.koutachan.buildingwordbattle.playerdata.PlayerDataUtil;
import me.koutachan.buildingwordbattle.playerdata.impl.Enum.TeamEnum;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ConfigUtil {

    public String translateAlternateColorCodes(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public void sendChat(CommandSender sender, String messageID, String... replace) {
        String message = replaceString(messageID, replace).replaceAll("%player%", sender.getName());

        sender.sendMessage(message);
    }

    public void sendChat(Player player, String messageID, String... replace) {
        String message = replaceString(messageID, replace).replaceAll("%player%", player.getName());

        player.sendMessage(message);
    }

    public void sendChat(String messageID, String... replace) {
        for (Player player : Bukkit.getOnlinePlayers()) {

            String message = replaceString(messageID, replace).replaceAll("%player%", player.getName());

            player.sendMessage(message);
        }
    }

    public String message(String messageID, String... replace) {
        return replaceString(messageID, replace);
    }

    public String messageList(String messageID, String... replace) {
        return replaceStringList(messageID, replace);
    }

    public List<String> getList(String messageID, String... replace) {
        return replaceList(messageID, replace);
    }

    public void sendMessageBroadCast(String messageID, String... replace) {

        String message = replaceString(messageID, replace);

        Bukkit.broadcastMessage(message);
    }

    public void sendChatToPlayers(String messageID, String... replace) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = PlayerDataUtil.getPlayerData(player);
            if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

                String message = replaceString(messageID, replace).replaceAll("%player%", player.getName());

                player.sendMessage(message);
            }
        }
    }

    public String replaceString(String messageID, String... replace) {

        String message = MessageManager.getString(messageID);

        if (replace == null) return message;

        for (String str : replace) {
            String[] split = str.split("\\|");

            message = message.replaceAll(split[0], split[1]);
        }
        return message;
    }

    public String replaceStringList(String messageID, String... replace) {

        StringBuilder stringBuilder = new StringBuilder();

        List<String> list = MessageManager.getStringList(messageID);

        for (String message : list) {
            for (String str : replace) {
                String[] split = str.split("\\|");

                message = message.replaceAll(split[0], split[1]);
            }

            if (stringBuilder.length() == 0) stringBuilder.append(translateAlternateColorCodes(message));
            else stringBuilder.append("\n").append(translateAlternateColorCodes(message));
        }

        return stringBuilder.toString();
    }

    public List<String> replaceList(String messageID, String... replace) {

        List<String> list = MessageManager.getStringList(messageID);

        if (replace == null) return list;
        List<String> temp = new ArrayList<>();

        for (String message : list) {
            for (String str : replace) {
                String[] split = str.split("\\|");

                message = translateAlternateColorCodes(message.replaceAll(split[0], split[1]));
            }

            temp.add(message);
        }

        return temp;
    }
}