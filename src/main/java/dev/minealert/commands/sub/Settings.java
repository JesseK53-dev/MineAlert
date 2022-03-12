package dev.minealert.commands.sub;

import dev.minealert.commands.SubCommand;
import dev.minealert.file.lang.Lang;
import dev.minealert.inventories.SettingsMenu;
import dev.minealert.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Settings extends SubCommand {

    @Override
    public String getName() {
        return "settings";
    }

    @Override
    public String getDescription() {
        return Lang.SETTINGS_DESC.toConfigString();
    }

    @Override
    public String getSyntax() {
        return Lang.SETTINGS_SYN.toConfigString();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof final Player player)) {
            System.out.println("Only players can use this command!");
            return;
        }
        if (!player.hasPermission("minealert.settings")) {
            MessageUtils.sendFormattedMessage(Lang.PREFIX.toConfigString() + Lang.NO_PERMISSION.toConfigString(), player);
            return;
        }
        new SettingsMenu().open(player);
    }
}
