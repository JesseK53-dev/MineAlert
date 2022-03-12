package dev.minealert.commands.sub;

import dev.minealert.commands.SubCommand;
import dev.minealert.file.lang.Lang;
import dev.minealert.inventories.InspectionMenu;
import dev.minealert.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Inspect extends SubCommand {

    @Override
    public String getName() {
        return "inspect";
    }

    @Override
    public String getDescription() {
        return Lang.INSPECT_DESC.toConfigString();
    }

    @Override
    public String getSyntax() {
        return Lang.INSPECT_SYN.toConfigString();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length <= 1) return;
        if (!(sender instanceof final Player player)) {
            System.out.println("Only players can use this command!");
            return;
        }
        if (!player.hasPermission("minealert.inspect")) {
            MessageUtils.sendFormattedMessage(Lang.PREFIX.toConfigString() + Lang.NO_PERMISSION.toConfigString(), player);
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            MessageUtils.sendFormattedMessage(Lang.PREFIX.toConfigString() + Lang.NO_PLAYER_EXIST.toConfigString(), player);
            return;
        }

        new InspectionMenu(target).open(player);
    }
}
