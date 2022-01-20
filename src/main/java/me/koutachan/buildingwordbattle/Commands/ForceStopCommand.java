package me.koutachan.buildingwordbattle.Commands;

import me.koutachan.buildingwordbattle.Game.Game;
import me.koutachan.buildingwordbattle.Game.GameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.Game.GameInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ForceStopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (GameInfo.nowState == GameStateEnum.SPEC) {
            Game.resetGame();

            sender.sendMessage("Game > 自動観戦を終了しました");
        }
    return false;
    }
}