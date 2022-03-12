package dev.minealert.alerts.types;

import com.google.common.collect.Maps;
import dev.minealert.alerts.AbstractAlertModule;
import dev.minealert.file.lang.Lang;
import dev.minealert.module.ModuleInfo;
import dev.minealert.module.ModuleType;
import org.bukkit.entity.Player;

import java.util.Map;

@ModuleInfo(moduleName = "Spawner Alert", moduleDesc = "Spawner Alert for Alert Components", moduleType = ModuleType.ADDON)
public class SpawnerAlert extends AbstractAlertModule {

    private final Map<String, Integer> mineMap = Maps.newConcurrentMap();

    public SpawnerAlert() {

    }

    @Override
    public void callInit(Player player) {
        init(mineMap, player, "alert.spawner-enable", "alert.spawner-amount", Lang.SPAWNER_ALERT_MESSAGE, true);
    }

    public Map<String, Integer> getMineMap() {
        return mineMap;
    }

    @Override
    public String update() {
        return "UPDATE MINEDATA SET SPAWNER=? WHERE UUID=?";
    }

    @Override
    public String resultID() {
        return "SPAWNER";
    }
}


