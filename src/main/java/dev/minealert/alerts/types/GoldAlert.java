package dev.minealert.alerts.types;

import com.google.common.collect.Maps;
import dev.minealert.alerts.AbstractAlertModule;
import dev.minealert.file.lang.Lang;
import dev.minealert.module.ModuleInfo;
import dev.minealert.module.ModuleType;
import org.bukkit.entity.Player;

import java.util.Map;

@ModuleInfo(moduleName = "Gold Alert", moduleDesc = "Gold Alert for Alert Components", moduleType = ModuleType.ADDON)
public class GoldAlert extends AbstractAlertModule {

    private final Map<String, Integer> mineMap = Maps.newConcurrentMap();

    public GoldAlert() {

    }

    @Override
    public void callInit(Player player) {
        init(mineMap, player, "alert.gold-enable", "alert.gold-amount", Lang.GOLD_ALERT_MESSAGE, false);
    }

    public Map<String, Integer> getMineMap() {
        return mineMap;
    }

    @Override
    public String update() {
        return "UPDATE MINEDATA SET GOLD=? WHERE UUID=?";
    }

    @Override
    public String resultID() {
        return "GOLD";
    }
}

