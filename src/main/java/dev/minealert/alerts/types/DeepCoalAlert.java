package dev.minealert.alerts.types;

import com.google.common.collect.Maps;
import dev.minealert.alerts.AbstractAlertModule;
import dev.minealert.file.lang.Lang;
import dev.minealert.module.ModuleInfo;
import dev.minealert.module.ModuleType;
import org.bukkit.entity.Player;

import java.util.Map;

@ModuleInfo(moduleName = "Deep Coal Alert", moduleDesc = "Deep Coal Alert for Alert Components", moduleType = ModuleType.ADDON)
public class DeepCoalAlert extends AbstractAlertModule {

    private final Map<String, Integer> mineMap = Maps.newConcurrentMap();

    public DeepCoalAlert() {

    }

    @Override
    public void callInit(Player player) {
        init(mineMap, player, "alert.deepcoal-enable", "alert.deepcoal-amount", Lang.DEEPCOAL_ALERT_MESSAGE, false);
    }

    public Map<String, Integer> getMineMap() {
        return mineMap;
    }

    @Override
    public String update() {
        return "UPDATE MINEDATA SET DEEPCOAL=? WHERE UUID=?";
    }

    @Override
    public String resultID() {
        return "DEEPCOAL";
    }
}
