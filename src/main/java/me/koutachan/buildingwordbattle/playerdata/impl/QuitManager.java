package me.koutachan.buildingwordbattle.playerdata.impl;

import lombok.Getter;
import lombok.Setter;
import me.koutachan.buildingwordbattle.BuildingWordBattle;
import me.koutachan.buildingwordbattle.game.gameutil.BuildingWordUtility;
import me.koutachan.buildingwordbattle.playerdata.PlayerData;
import me.koutachan.buildingwordbattle.playerdata.PlayerDataUtil;
import me.koutachan.buildingwordbattle.util.ConfigUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@Getter @Setter
public class QuitManager {

    private PlayerData data;

    private BukkitTask task;

    public QuitManager(PlayerData playerData) {
        this.data = playerData;
    }

    public void apply() {
        int time = BuildingWordUtility.getTime();

        if (time != 0) {
            time = time / 2 * 20;

            task = Bukkit.getScheduler().runTaskLater(BuildingWordBattle.INSTANCE, () -> {
                PlayerDataUtil.getQuitPlayers().add(data);

                PlayerDataUtil.removePlayerData(data.getPlayer());

                TextComponent message = new TextComponent(ConfigUtil.message("GAME.PARTWAY-THROUGH", "%time%|" + BuildingWordUtility.getTime()));
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/join %s", data.getPlayer().getUniqueId())));

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.spigot().sendMessage(message);
                }
            }, time);
        }
    }

    public void stop() {
        if (task != null && !task.isCancelled()) {
            task.cancel();

            data.setScoreBoardManager(new ScoreBoardManager(data));

            task = null;
        }
    }
}