package me.koutachan.buildingwordbattle.commands;

import me.koutachan.buildingwordbattle.game.GameInfo;
import me.koutachan.buildingwordbattle.game.enums.GameEnum;
import me.koutachan.buildingwordbattle.game.enums.GameStateEnum;
import me.koutachan.buildingwordbattle.game.gameutil.BuildingWordUtility;
import me.koutachan.buildingwordbattle.game.main.Spectator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ForceStopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (GameInfo.gameInfo == GameEnum.STARTING) {
            StartCommand.stop();

            sender.sendMessage("Game > スタートを終了しました");
        } else if (GameInfo.gameState == GameStateEnum.SPEC) {
            Spectator.stop();

            sender.sendMessage("Game > 自動観戦を終了しました");
        }

        BuildingWordUtility.resetGame(true);

        sender.sendMessage("Game > ゲームをリセットしました");

        return true;
    }
}
