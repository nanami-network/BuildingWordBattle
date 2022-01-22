package me.koutachan.buildingwordbattle.commands;

import me.koutachan.buildingwordbattle.game.GameInfo;
import me.koutachan.buildingwordbattle.game.gameEnum.GameStateEnum;
import me.koutachan.buildingwordbattle.game.gameutil.BuildingWordUtility;
import me.koutachan.buildingwordbattle.game.system.Spec;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ForceStopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (GameInfo.gameState == GameStateEnum.SPEC) {
            Spec.stop();

            BuildingWordUtility.resetGame(true);

            sender.sendMessage("Game > 自動観戦を終了しました");
        } else {

            BuildingWordUtility.resetGame(true);
        }
        return true;
    }
}