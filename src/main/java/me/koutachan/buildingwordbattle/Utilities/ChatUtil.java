package me.koutachan.buildingwordbattle.Utilities;

import lombok.experimental.UtilityClass;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.PlayerData.impl.Enum.TeamEnum;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@UtilityClass
public class ChatUtil {

    public String translateAlternateColorCodes(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public void sendChat(Player player, String messageID, String... replace) {
        String message = replace(MessageManager.getString(messageID).replaceAll("%player%", player.getName()), replace);

        player.sendMessage(message);
    }

    public void sendChat(String messageID, String... replace) {
        for (Player player : Bukkit.getOnlinePlayers()) {

            String message = replace(MessageManager.getString(messageID).replaceAll("%player%", player.getName()), replace);

            player.sendMessage(message);
        }
    }

    public void sendMessageBroadCast(String messageID, String... replace) {

        String message = replace(MessageManager.getString(messageID), replace);

        Bukkit.broadcastMessage(message);
    }

    public void sendChatToPlayers(String messageID, String... replace) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = PlayerDataUtil.getPlayerData(player);
            if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

                String message = replace(MessageManager.getString(messageID).replaceAll("%player%", player.getName()), replace);

                player.sendMessage(message);
            }
        }
    }

    public String replace(String messageID, String... replace) {

        String message = MessageManager.getString(messageID).replaceAll("\n", "\n");

        if (replace == null) return message;

        for (String str : replace) {
            String[] split = str.split("\\|");

            message = message.replaceAll(split[0], split[1]);
        }
        return message;
    }
}