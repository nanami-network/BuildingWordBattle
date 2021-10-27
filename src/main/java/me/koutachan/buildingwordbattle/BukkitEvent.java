package me.koutachan.buildingwordbattle;

import me.koutachan.buildingwordbattle.Game.CreateBox;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameEnum;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.Game.GameInfo;
import me.koutachan.buildingwordbattle.Map.AreaCreator;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.PlayerData.impl.TeamEnum.TeamEnum;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitEvent implements Listener {

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e) {
        PlayerDataUtil.createPlayerData(e.getPlayer());

        if (GameInfo.gameState != GameEnum.LOBBY) {
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent e) {
        PlayerDataUtil.removePlayerData(e.getPlayer());
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        if (GameInfo.nowState == GameStateEnum.THEME) {

            PlayerData data = PlayerDataUtil.getPlayerData(e.getPlayer());

            if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

                e.setCancelled(true);

                if (e.getMessage().length() > 16) {
                    e.getPlayer().sendMessage(ChatColor.RED + "お題は16文字までです！");
                    try {
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ANVIL_PLACE, 1f, 1f);
                    } catch (NoSuchFieldError ignored) {
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf("ANVIL_LAND"), 1f, 1f);
                    }
                    return;
                }

                e.getPlayer().sendMessage(ChatColorUtil.translateAlternateColorCodes(String.format("&b[CHAT] > お題を %s に変更しました", e.getMessage())));

                try {
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                } catch (NoSuchFieldError ignored) {
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf("LEVEL_UP"), 1f, 1f);
                }

                data.getThemeManager().setTheme(e.getMessage());
            }
        }
    }

    @EventHandler
    public void onPlaceBlockEvent(BlockPlaceEvent e) {

        PlayerData data = PlayerDataUtil.getPlayerData(e.getPlayer());

        if (data.getTeamManager().getCurrentTeam() != TeamEnum.ADMIN) {

            e.setCancelled(true);

            for (AreaCreator areaCreator : CreateBox.areaCreatorMap.values()) {
                if (areaCreator.isArea(e.getBlock().getLocation()) && areaCreator.getAuthorUUID() == e.getPlayer().getUniqueId()) {
                    e.setCancelled(false);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {

        PlayerData data = PlayerDataUtil.getPlayerData(e.getPlayer());

        if (data.getTeamManager().getCurrentTeam() != TeamEnum.ADMIN) {

            e.setCancelled(true);

            for (AreaCreator areaCreator : CreateBox.areaCreatorMap.values()) {
                if (areaCreator.isArea(e.getBlock().getLocation()) && areaCreator.getAuthorUUID() == e.getPlayer().getUniqueId()) {
                    e.setCancelled(false);
                    break;
                }
            }
        }
    }
}