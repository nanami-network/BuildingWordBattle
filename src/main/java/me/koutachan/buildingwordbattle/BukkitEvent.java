package me.koutachan.buildingwordbattle;

import me.koutachan.buildingwordbattle.game.GameInfo;
import me.koutachan.buildingwordbattle.game.enums.GameEnum;
import me.koutachan.buildingwordbattle.game.enums.GameStateEnum;
import me.koutachan.buildingwordbattle.game.gameutil.BuildingWordUtility;
import me.koutachan.buildingwordbattle.game.main.Spectator;
import me.koutachan.buildingwordbattle.map.AreaCreator;
import me.koutachan.buildingwordbattle.playerdata.PlayerData;
import me.koutachan.buildingwordbattle.playerdata.PlayerDataUtil;
import me.koutachan.buildingwordbattle.playerdata.impl.enums.TeamEnum;
import me.koutachan.buildingwordbattle.util.ConfigUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.util.Vector;

public class BukkitEvent implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        PlayerData data = PlayerDataUtil.getPlayerData(e.getPlayer());

        if (data == null) {
            PlayerDataUtil.createPlayerData(e.getPlayer());

            if (GameInfo.gameInfo != GameEnum.LOBBY && GameInfo.gameInfo != GameEnum.STARTING) {
                for (PlayerData playerData : PlayerDataUtil.getQuitPlayers()) {

                    TextComponent message = new TextComponent(ConfigUtil.message("GAME.PARTWAY-THROUGH",
                            "%time%|" + BuildingWordUtility.getTime()
                            , "%player%|" + playerData.getPlayer().getName()));
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/join %s", playerData.getPlayer().getUniqueId())));

                    e.getPlayer().spigot().sendMessage(message);
                }

                e.getPlayer().setGameMode(GameMode.SPECTATOR);

                World world = Bukkit.getWorld("world");

                e.getPlayer().teleport(new Location(world, BuildingWordBattle.INSTANCE.getConfig().getInt("startPosX"), BuildingWordBattle.INSTANCE.getConfig().getInt("startPosY"), BuildingWordBattle.INSTANCE.getConfig().getInt("startPosZ")));
            } else {
                e.getPlayer().setGameMode(GameMode.CREATIVE);

                World world = Bukkit.getWorld("world");

                e.getPlayer().teleport(new Location(world, BuildingWordBattle.INSTANCE.getConfig().getInt("lobbyPosX"), BuildingWordBattle.INSTANCE.getConfig().getInt("lobbyPosY"), BuildingWordBattle.INSTANCE.getConfig().getInt("lobbyPosZ")));
            }
        } else {
            PlayerDataUtil.set(data, data);
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        PlayerData data = PlayerDataUtil.getPlayerData(e.getPlayer());

        if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER
                && GameInfo.gameInfo == GameEnum.GAME
                && GameInfo.gameState != GameStateEnum.SPEC) {
            data.getQuitManager().apply();
        } else {
            PlayerDataUtil.removePlayerData(data.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerPortalEvent(PlayerPortalEvent e) {
        if (PlayerDataUtil.getPlayerData(e.getPlayer()).getTeamManager().getCurrentTeam() != TeamEnum.ADMIN) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        if (GameInfo.gameState == GameStateEnum.THEME) {

            PlayerData data = PlayerDataUtil.getPlayerData(e.getPlayer());

            if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

                e.setCancelled(true);

                if (e.getMessage().length() > 16) {
                    e.getPlayer().sendMessage(ChatColor.RED + "お題は16文字までです！");
                    try {
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ANVIL_PLACE, 1f, 1f);
                    } catch (NoSuchFieldError ex) {
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf("ANVIL_LAND"), 1f, 1f);
                    }
                } else {
                    e.getPlayer().sendMessage(ConfigUtil.translateAlternateColorCodes(String.format("&b[CHAT] > お題を %s に変更しました", e.getMessage())));

                    try {
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                    } catch (NoSuchFieldError ignored) {
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf("LEVEL_UP"), 1f, 1f);
                    }

                    data.getThemeManager().setTheme(e.getMessage());
                }
            }
        } else {
            if (GameInfo.gameState == GameStateEnum.ANSWER) {

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

                    e.getPlayer().sendMessage(ConfigUtil.translateAlternateColorCodes(String.format("&b[CHAT] > 回答を %s に変更しました", e.getMessage())));
                    GameInfo.areaCreator.get(data.getMapManager().getAnswerMapName()).setAnswer(e.getMessage());

                    try {
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                    } catch (NoSuchFieldError ignored) {
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf("LEVEL_UP"), 1f, 1f);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlaceBlockEvent(BlockPlaceEvent e) {

        PlayerData data = PlayerDataUtil.getPlayerData(e.getPlayer());

        if (data.getTeamManager().getCurrentTeam() != TeamEnum.ADMIN) {

            e.setCancelled(true);

            if (GameInfo.gameState == GameStateEnum.BUILDING) {
                for (int mapID : data.getMapManager().getMapList()) {

                    AreaCreator areaCreator = GameInfo.areaCreator.get(mapID + "-" + GameInfo.CURRENT_BUILD_ROUND);

                    if (e.getBlock().getType() != Material.WALL_SIGN
                            && e.getBlock().getType() != Material.SIGN
                            && e.getBlock().getType() != Material.SIGN_POST
                            && areaCreator.isArea(e.getBlock().getLocation())
                            && areaCreator.getAuthorData() == data) {
                        e.setCancelled(false);
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent e) {
        if (PlayerDataUtil.getPlayerData(e.getPlayer().getUniqueId()).getTeamManager().getCurrentTeam() != TeamEnum.ADMIN
                && e.getInventory().getType() != InventoryType.PLAYER
                && e.getInventory().getType() != InventoryType.CREATIVE) {
            e.setCancelled(true);

            e.getPlayer().sendMessage(ConfigUtil.message("GAME.CANNOT-OPEN-GUI"));
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e) {
        if (e.getBlock() instanceof InventoryHolder) {
            ((InventoryHolder) e.getBlock()).getInventory().clear();
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {

        PlayerData data = PlayerDataUtil.getPlayerData(e.getPlayer());

        if (data.getTeamManager().getCurrentTeam() != TeamEnum.ADMIN) {

            e.setCancelled(true);

            if (GameInfo.gameState == GameStateEnum.BUILDING) {
                for (int mapID : data.getMapManager().getMapList()) {

                    AreaCreator areaCreator = GameInfo.areaCreator.get(mapID + "-" + GameInfo.CURRENT_BUILD_ROUND);

                    if (areaCreator.isArea(e.getBlock().getLocation())
                            && areaCreator.getAuthorData() == data) {
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
    public void onHangingPlaceEvent(HangingPlaceEvent e) {
        if (PlayerDataUtil.getPlayerData(e.getPlayer()).getTeamManager().getCurrentTeam() != TeamEnum.ADMIN) {
            e.setCancelled(true);
        }
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

            if (GameInfo.gameState == GameStateEnum.BUILDING) {
                for (int mapID : data.getMapManager().getMapList()) {

                    AreaCreator areaCreator = GameInfo.areaCreator.get(mapID + "-" + GameInfo.CURRENT_BUILD_ROUND);

                    if (areaCreator.isArea(e.getBlockClicked().getRelative(e.getBlockFace()).getLocation())
                            && areaCreator.getAuthorData() == data) {
                        e.setCancelled(false);
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSplashPotionEvent(ProjectileLaunchEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplodeEvent(EntityExplodeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockExplodeEvent(BlockExplodeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemSpawnEvent(ItemSpawnEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void moveEvent(PlayerMoveEvent e) {
        if (GameInfo.gameInfo == GameEnum.GAME) {
            PlayerData data = PlayerDataUtil.getPlayerData(e.getPlayer());

            if (GameInfo.gameState != GameStateEnum.SPEC) {

                if (data.getTeamManager().getCurrentTeam() == TeamEnum.PLAYER) {

                    int mapID = data.getMapManager().getLastMapID();

                    AreaCreator areaCreator = GameInfo.areaCreator.get(mapID + "-" + GameInfo.CURRENT_BUILD_ROUND);

                    if (areaCreator != null && !areaCreator.isArea(e.getPlayer().getLocation())) {
                        Vector middle = areaCreator.getMiddle();

                        e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), middle.getX(), middle.getY(), middle.getZ()));
                        //e.getPlayer().teleport(e.getFrom());
                        e.getPlayer().sendMessage("マップ外に移動しないでください！");
                    }
                }
            } else {
                AreaCreator areaCreator = Spectator.areaCreator;

                if (areaCreator != null && !areaCreator.isArea(e.getPlayer().getLocation()) && data.getTeamManager().getCurrentTeam() != TeamEnum.ADMIN) {
                    Vector middle = areaCreator.getMiddle();

                    e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), middle.getX(), middle.getY(), middle.getZ()));
                    e.getPlayer().sendMessage("マップ外に移動しないでください！");
                }
            }
        }
    }
}