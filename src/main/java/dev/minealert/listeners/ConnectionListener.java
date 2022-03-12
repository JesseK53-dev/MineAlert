package dev.minealert.listeners;

import com.google.common.util.concurrent.Atomics;
import dev.minealert.MineAlert;
import dev.minealert.alerts.AbstractAlertModule;
import dev.minealert.database.DatabaseUUIDTool;
import dev.minealert.database.SQLUtils;
import dev.minealert.database.cache.CacheSystem;
import dev.minealert.file.DatabaseFile;
import dev.minealert.inventories.SettingsMenu;
import dev.minealert.module.AbstractModuleLoader;
import dev.minealert.utils.MineDataUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        CompletableFuture.runAsync(() -> {
            for(int i = 0; i < MineDataUtils.getModuleAlertList().getSize(); i++){
                Class<? extends AbstractAlertModule> alertClasses = MineDataUtils.getModuleAlertList().getElement(i);
                AbstractModuleLoader.getModule(alertClasses).ifPresent(alert -> alert.callInit(player));
            }
            if (AbstractModuleLoader.getModule(DatabaseFile.class).isPresent())
                if (!AbstractModuleLoader.getModule(DatabaseFile.class).get().getFileConfiguration().getBoolean("enable")) {
                    return;
                }
            AtomicReference<SQLUtils> sqlUtilsFetcher = Atomics.newReference();
            AbstractModuleLoader.getModule(SQLUtils.class).ifPresent(sqlUtilsFetcher::set);
            SQLUtils sqlUtils = sqlUtilsFetcher.get();
            sqlUtils.executeQuery("SELECT * FROM MINEDATA WHERE UUID =?", ps -> ps.setBinaryStream(1, DatabaseUUIDTool.convertUniqueId(player.getUniqueId())), rs -> {
                if (rs.next()) {
                    return rs;
                } else {
                    final String insertQuery = "INSERT INTO MINEDATA (UUID,NAME,ANCIENTDEBRIS,COAL,COPPER,DEEPCOAL,DEEPCOPPER,DEEPDIAMOND,DEEPEMERALD,"
                            + "DEEPGOLD,DEEPIRON,DEEPLAPIS,DEEPREDSTONE,DIAMOND,EMERALD,GOLD,IRON,LAPIS,NETHERGOLD,REDSTONE,SPAWNER) VALUES" +
                            " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    sqlUtils.executeUpdate(insertQuery, ps -> {
                        ps.setBinaryStream(1, DatabaseUUIDTool.convertUniqueId(player.getUniqueId()));
                        ps.setString(2, player.getName());
                        for (int i = 3; i < 22; i++)
                            ps.setInt(i, 0);
                    });
                }
                return rs.next();
            });
        });
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(MineAlert.getInstance(), () -> {
            CacheSystem.loopCacheSystem(player);
            MineDataUtils.removeMiner(player);
        });
        if (!SettingsMenu.itemPickup.contains(player.getName())) return;
        SettingsMenu.itemPickup.remove(player.getName());
    }
}


