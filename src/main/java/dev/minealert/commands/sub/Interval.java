package dev.minealert.commands.sub;

import dev.minealert.MineAlert;
import dev.minealert.MineAlertRegistries;
import dev.minealert.commands.SubCommand;
import dev.minealert.file.lang.Lang;
import dev.minealert.utils.MessageUtils;
import org.bukkit.command.CommandSender;

public class Interval extends SubCommand {

    @Override
    public String getName() {
        return "interval";
    }

    @Override
    public String getDescription() {
        return Lang.INTERVAL_DESC.toConfigString();
    }

    @Override
    public String getSyntax() {
        return Lang.INTERVAL_SYN.toConfigString();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!sender.hasPermission("minealert.notify")) {
            MessageUtils.sendFormattedMessage(Lang.PREFIX.toConfigString() + Lang.NO_PERMISSION.toConfigString(), sender);
            return;
        }
        String message = Lang.INTERVAL_MESSAGE.toConfigString();
        message = message.replace("%amount%", String.valueOf(MineAlertRegistries.getInterval()));
        MessageUtils.sendFormattedMessage(Lang.PREFIX.toConfigString() + message, sender);
    }
}
