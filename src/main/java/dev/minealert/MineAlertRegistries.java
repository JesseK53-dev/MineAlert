package dev.minealert;


import dev.minealert.alerts.StaffAlert;
import dev.minealert.commands.MineAlertCommand;
import dev.minealert.commands.SubCommandRegistry;
import dev.minealert.database.DatabaseInit;
import dev.minealert.database.cache.CacheSystem;
import dev.minealert.file.DatabaseFile;
import dev.minealert.file.OreSettingsFile;
import dev.minealert.file.lang.Lang;
import dev.minealert.file.lang.LangFile;
import dev.minealert.listeners.AbstractMenuListener;
import dev.minealert.listeners.BlockListener;
import dev.minealert.listeners.ConnectionListener;
import dev.minealert.listeners.ItemListeners;
import dev.minealert.module.AbstractModuleLoader;
import dev.minealert.platform.PlatformInfo;
import dev.minealert.utils.BlockPlacePatchUtil;
import dev.minealert.utils.MessageUtils;
import dev.minealert.utils.MineDataUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class MineAlertRegistries extends MineAlertPlugin implements PlatformInfo {

    private static int interval;

    public MineAlertRegistries() {
        registerRegistries();
        Bukkit.getLogger().log(Level.INFO, convertToString());
        AbstractModuleLoader.getModuleMap().values().forEach(module -> {
            String message = module.getName() + " {" + module.getDescription() + " | " + module.getModuleType() + "} has loaded!";
            Bukkit.getLogger().log(Level.INFO, message);
        });
        if (AbstractModuleLoader.getModule(OreSettingsFile.class).isPresent()) {
            OreSettingsFile oreSettings = AbstractModuleLoader.getModule(OreSettingsFile.class).get();
            interval = oreSettings.getFileConfiguration().getInt("interval");
            Bukkit.getScheduler().runTaskTimer(MineAlert.getInstance(), () -> {
                interval--;
                if (interval <= 0) {
                    BlockPlacePatchUtil.getInstance().getBlockLocations().clear();
                    Collection<? extends Player> playerList = Bukkit.getServer().getOnlinePlayers();
                    if (playerList.size() != 0) {
                        CompletableFuture.runAsync(() -> {
                            for (Player all : playerList) {
                                CacheSystem.loopCacheSystem(all);
                                MineDataUtils.clearMineData();
                                MineDataUtils.addAll(all);
                                if (StaffAlert.getInstance().containsStaffMember(all.getName()))
                                    MessageUtils.sendFormattedMessage(Lang.PREFIX.toConfigString() + Lang.DATA_RESET_MESSAGE.toConfigString(), all);
                            }
                        }).whenComplete((unused, throwable) -> interval = oreSettings.getFileConfiguration().getInt("interval"));
                    }
                }
            }, 20, 20);
        }
    }

    @Override
    public String getName() {
        return "Mine Alert";
    }

    @Override
    public String getVersion() {
        return "1.10";
    }


    @Override
    protected void registerFiles() {
        AbstractModuleLoader.getModule(OreSettingsFile.class).ifPresent(OreSettingsFile::registerFile);
        AbstractModuleLoader.getModule(DatabaseFile.class).ifPresent(DatabaseFile::registerFile);
        AbstractModuleLoader.getModule(LangFile.class).ifPresent(LangFile::registerFile);
    }

    @Override
    protected void registerDatabase() {
        AbstractModuleLoader.getModule(DatabaseFile.class).ifPresent(databaseFile -> {
            if (databaseFile.getFileConfiguration().getBoolean("enable")) {
                AbstractModuleLoader.getModule(DatabaseInit.class).ifPresent(DatabaseInit::initDatabase);
            }
        });
    }


    @Override
    protected void registerCommands() {
        MineAlert.getInstance().getCommand("minealert").setExecutor(new MineAlertCommand());
        SubCommandRegistry.getInstance().registerCommands();
    }

    @Override
    protected void registerListeners() {
        addListener(new AbstractMenuListener(), new BlockListener(), new ConnectionListener(), new ItemListeners());
    }

    public static int getInterval() {
        return interval;
    }
}
