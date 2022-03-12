package dev.minealert.database.cache;

import dev.minealert.alerts.AbstractAlertModule;
import dev.minealert.database.DatabaseUUIDTool;
import dev.minealert.database.SQLUtils;
import dev.minealert.file.DatabaseFile;
import dev.minealert.module.AbstractModuleLoader;
import dev.minealert.utils.MineDataUtils;
import org.bukkit.entity.Player;

import java.util.Map;

public class CacheSystem {

    public static void loopCacheSystem(Player player) {
        if (!AbstractModuleLoader.getModule(DatabaseFile.class).get().getFileConfiguration().getBoolean("enable")) {
            return;
        }

        StringBuilder statement = new StringBuilder();

        statement.append("UPDATE MINEDATA SET ");
        int maxAlerts = MineDataUtils.getModuleAlertList().getSize();

        for (int i = 0; i < MineDataUtils.getModuleAlertList().getSize(); i++) {
            Class<? extends AbstractAlertModule> alertClasses = MineDataUtils.getModuleAlertList().getElement(i);
            if (AbstractModuleLoader.getModule(alertClasses).isPresent()) {
                Map<String, Integer> mineMap = AbstractModuleLoader.getModule(alertClasses).get().getMineMap();
                if (mineMap.isEmpty()) return;
                if (!mineMap.containsKey(player.getName()) || mineMap.get(player.getName()) < 1) return;
                int finalI = i;
                AbstractModuleLoader.getModule(alertClasses).ifPresent(alert -> statement.append(String.format("%s=%s+%s%s",
                        alert.resultID(), alert.resultID(), mineMap.get(player.getName()) - 1, finalI < maxAlerts - 1 ? ", " : " ")));
            }
        }

        statement.append(" WHERE UUID=?");
        AbstractModuleLoader.getModule(SQLUtils.class).ifPresent((action) -> {
            action.executeUpdate(statement.toString(), ps -> ps.setBinaryStream(1, DatabaseUUIDTool.convertUniqueId(player.getUniqueId())));
        });
    }
}
