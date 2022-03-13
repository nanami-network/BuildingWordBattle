package me.koutachan.buildingwordbattle.commands;

import me.koutachan.buildingwordbattle.game.system.Spec;
import me.koutachan.buildingwordbattle.util.ConfigUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ChangeModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 1) {
            String mode = args[1].toLowerCase();

            switch (mode) {
                case "fast": {
                    Spec.fastMode = !Spec.fastMode;

                    break;
                }
                case "super-fast": {
                    Spec.superFastMode = !Spec.superFastMode;

                    break;
                }
                case "if-answer-null": {
                    Spec.skipIfAnswerIsNull = !Spec.skipIfAnswerIsNull;

                    break;
                }
                default: {
                    sender.sendMessage(ConfigUtil.message("CHANGE-MODE-COMMAND.MODE-UNKNOWN"));

                    return true;
                }
            }
        } else {
            sender.sendMessage(ConfigUtil.messageList("CHANGE-MODE-COMMAND.HELP-MESSAGE"));

            return true;
        }

        String on = ConfigUtil.message("CHANGE-MODE-COMMAND.MODE-ON");
        String off = ConfigUtil.message("CHANGE-MODE-COMMAND.MODE-ON");

        final String fast = Spec.fastMode ? on : off;
        final String superFast = Spec.superFastMode ? on : off;
        final String answer = Spec.skipIfAnswerIsNull ? on : off;

        sender.sendMessage(ConfigUtil.messageList("CHANGE-MODE-COMMAND.MODE-CHANGE-MESSAGE",
                "%fast-mode-value%|" + fast
                , "%super-fast-mode-value%|" + superFast
                , "%if-answer-null-mode-value%|" + answer));

        return true;
    }
}
