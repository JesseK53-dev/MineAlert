package dev.minealert.commands.sub;

import dev.minealert.MineAlert;
import dev.minealert.commands.SubCommand;
import dev.minealert.file.DatabaseFile;
import dev.minealert.file.OreSettingsFile;
import dev.minealert.file.lang.Lang;
import dev.minealert.file.lang.LangFile;
import dev.minealert.module.AbstractModuleLoader;
import dev.minealert.utils.MessageUtils;
import org.bukkit.command.CommandSender;

public class ReloadConfig extends SubCommand {

        @Override
        public String getName() {
            return "reload";
        }


        @Override
        public String getDescription() {
            return Lang.RELOAD_DESC.toConfigString();
        }

        @Override
        public String getSyntax() {
            return Lang.RELOAD_SYN.toConfigString();
        }

        @Override
        public void perform(CommandSender sender, String[] args) {
            if (!sender.hasPermission("minealert.admin")) {
                MessageUtils.sendFormattedMessage(Lang.PREFIX.toConfigString() + Lang.NO_PERMISSION.toConfigString(), sender);
                return;
            }
            AbstractModuleLoader.getModule(LangFile.class).ifPresent(langFile -> langFile.reloadFile("messages.yml"));
            AbstractModuleLoader.getModule(DatabaseFile.class).ifPresent(databaseFile -> databaseFile.reloadFile("database.yml"));
            AbstractModuleLoader.getModule(OreSettingsFile.class).ifPresent(oreSettingsFile -> oreSettingsFile.reloadFile("oresettings.yml"));
            MessageUtils.sendFormattedMessage(Lang.PREFIX.toConfigString() + Lang.RELOAD_MESSAGE.toConfigString(), sender);
        }
    }
