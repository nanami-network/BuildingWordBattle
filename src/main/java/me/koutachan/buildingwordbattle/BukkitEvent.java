package me.koutachan.buildingwordbattle;

import me.koutachan.buildingwordbattle.Game.CreateBox;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameEnum;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.Game.GameInfo;
import me.koutachan.buildingwordbattle.Game.Spec;
import me.koutachan.buildingwordbattle.Map.AreaCreator;
import me.koutachan.buildingwordbattle.PlayerData.PlayerData;
import me.koutachan.buildingwordbattle.PlayerData.PlayerDataUtil;
import me.koutachan.buildingwordbattle.PlayerData.impl.Enum.TeamEnum;
import me.koutachan.buildingwordbattle.Utilities.ChatUtil;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.util.Vector;

public class BukkitEvent implements Listener {

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e) {
        PlayerDataUtil.createPlayerData(e.getPlayer());

        if (GameInfo.gameState != GameEnum.LOBBY && GameInfo.gameState != GameEnum.STARTING) {
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
        } else {
            e.getPlayer().setGameMode(GameMode.CREATIVE);
        }
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent e) {
        PlayerData data = PlayerDataUtil.removePlayerData(e.getPlayer());

        if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {
            GameInfo.mapList.remove((Integer) data.getThemeManager().getThemeMap());
        }
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

                e.getPlayer().sendMessage(ChatUtil.translateAlternateColorCodes(String.format("&b[CHAT] > お題を %s に変更しました", e.getMessage())));

                try {
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                } catch (NoSuchFieldError ignored) {
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf("LEVEL_UP"), 1f, 1f);
                }

                data.getThemeManager().setTheme(e.getMessage());
            }
        } else {
            if (GameInfo.nowState == GameStateEnum.ANSWER) {

                PlayerData data = PlayerDataUtil.getPlayerData(e.getPlayer());

                if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

                    e.setCancelled(true);

                    if (e.getMessage().length() > 16) {
                        e.getPlayer().sendMessage(ChatColor.RED + "回答は16文字までです！");
                        try {
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ANVIL_PLACE, 1f, 1f);
                        } catch (NoSuchFieldError ignored) {
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf("ANVIL_LAND"), 1f, 1f);
                        }
                        return;
                    }

                    e.getPlayer().sendMessage(ChatUtil.translateAlternateColorCodes(String.format("&b[CHAT] > 回答を %s に変更しました", e.getMessage())));

                    try {
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                    } catch (NoSuchFieldError ignored) {
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf("LEVEL_UP"), 1f, 1f);
                    }

                    data.getAnswerManager().setAnswer(e.getMessage());
                }
            }
        }
    }

    @EventHandler
    public void onPlaceBlockEvent(BlockPlaceEvent e) {

        PlayerData data = PlayerDataUtil.getPlayerData(e.getPlayer());

        if (data.getTeamManager().getCurrentTeam() != TeamEnum.ADMIN) {

            e.setCancelled(true);

            if (GameInfo.nowState == GameStateEnum.BUILDING) {
                for (int mapID : data.getMapManager().getMapList()) {

                    AreaCreator areaCreator = CreateBox.areaCreatorMap.get(mapID + "-" + GameInfo.buildRound);

                    if (areaCreator.isArea(e.getBlock().getLocation()) && areaCreator.getAuthorUUID() == e.getPlayer().getUniqueId()) {
                        e.setCancelled(false);
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {

        PlayerData data = PlayerDataUtil.getPlayerData(e.getPlayer());

        if (data.getTeamManager().getCurrentTeam() != TeamEnum.ADMIN) {

            e.setCancelled(true);

            if (GameInfo.nowState == GameStateEnum.BUILDING) {
                for (int mapID : data.getMapManager().getMapList()) {

                    AreaCreator areaCreator = CreateBox.areaCreatorMap.get(mapID + "-" + GameInfo.buildRound);

                    if (areaCreator.isArea(e.getBlock().getLocation()) && areaCreator.getAuthorUUID() == e.getPlayer().getUniqueId()) {
                        e.setCancelled(false);
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCreatureSpawnEvent(CreatureSpawnEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onVehicleCreateEvent(VehicleCreateEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent e) {

        PlayerData data = PlayerDataUtil.getPlayerData(e.getPlayer());

        if (data.getTeamManager().getCurrentTeam() != TeamEnum.ADMIN) {

            e.setCancelled(true);

            if (GameInfo.nowState == GameStateEnum.BUILDING) {
                for (int mapID : data.getMapManager().getMapList()) {

                    AreaCreator areaCreator = CreateBox.areaCreatorMap.get(mapID + "-" + GameInfo.buildRound);

                    if (areaCreator.isArea(e.getBlockClicked().getRelative(e.getBlockFace()).getLocation()) && areaCreator.getAuthorUUID() == e.getPlayer().getUniqueId()) {
                        e.setCancelled(false);
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onExplodeEvent1(EntityExplodeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onExplodeEvent2(BlockExplodeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void moveEvent(PlayerMoveEvent e) {
        if (GameInfo.gameState == GameEnum.GAME) {
            if (GameInfo.nowState != GameStateEnum.SPEC) {
                PlayerData data = PlayerDataUtil.getPlayerData(e.getPlayer());

                if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

                    int mapID = data.getMapManager().getLastMapID();

                    AreaCreator areaCreator = CreateBox.areaCreatorMap.get(mapID + "-" + GameInfo.buildRound);

                    if (areaCreator != null && !areaCreator.isArea(e.getPlayer().getLocation())) {
                        Vector middle = areaCreator.getMiddle();

                        e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), middle.getX(), middle.getY(), middle.getZ()));
                        //e.getPlayer().teleport(e.getFrom());
                        e.getPlayer().sendMessage("マップ外に移動しないでください！");
                    }
                }
            } else {
                AreaCreator areaCreator = Spec.areaCreator;

                if (areaCreator != null && !areaCreator.isArea(e.getPlayer().getLocation())) {
                    Vector middle = areaCreator.getMiddle();

                    e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), middle.getX(), middle.getY(), middle.getZ()));
                    e.getPlayer().sendMessage("マップ外に移動しないでください！");
                }
            }
        }
    }
}