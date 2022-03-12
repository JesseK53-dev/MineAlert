package dev.minealert.alerts;

import dev.minealert.database.cache.CacheRequest;
import dev.minealert.file.OreSettingsFile;
import dev.minealert.file.lang.Lang;
import dev.minealert.module.AbstractModuleLoader;
import dev.minealert.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public abstract class AbstractAlertModule extends AbstractModuleLoader implements CacheRequest<String, String> {

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    private String type;

    public AbstractAlertModule() {

    }

    public void setType(String type) {
        this.type = type;
    }

    protected void addToMap(Map<String, Integer> minedMap, Player miner, String enablePath) {
        if (!isEnabled(enablePath)) return;
        final String name = miner.getName();
        if (!minedMap.containsKey(name)) {
            minedMap.put(name, 1);
        } else {
            minedMap.put(name, minedMap.get(name) + 1);
        }
    }

    protected void notifyStaff(Map<String, Integer> minedMap, Player miner, String pathAmount, Lang value, boolean isSpawner) {
        final String name = miner.getName();
        OreSettingsFile oreSettings = AbstractModuleLoader.getModule(OreSettingsFile.class).get();
        if (minedMap.get(name) >= oreSettings.getFileConfiguration().getInt(pathAmount)) {
            Bukkit.getOnlinePlayers().forEach(staff -> {
                if (!staff.hasPermission("minealert.notify")) {
                    return;
                }

                final StaffAlert instance = StaffAlert.getInstance();
                if (!instance.containsStaffMember(staff.getName())) {
                    return;
                }
                String prefix = Lang.PREFIX.toConfigString();
                String message = value.toConfigString();

                message = message.replace("%time%", dtf.format(LocalDateTime.now()));
                message = message.replace("%player%", name);
                message = message.replace("%amount%", minedMap.get(name).toString());
                if (isSpawner) {
                    message = message.replace("%mobtype%", type);
                }
                MessageUtils.sendFormattedMessage(prefix + message, staff);
            });
        }
    }

    protected void init(Map<String, Integer> minedMap, Player miner, String enablePath, String pathAmount, Lang value, boolean isSpawner) {
        addToMap(minedMap, miner, enablePath);
        notifyStaff(minedMap, miner, pathAmount, value, isSpawner);
    }

    public boolean isEnabled(String path) {
        return AbstractModuleLoader.getModule(OreSettingsFile.class).get().getFileConfiguration().getBoolean(path);
    }

    public void clearData(Map<String, Integer> map) {
        if (map.isEmpty()) return;
        map.clear();
    }


    public abstract Map<String, Integer> getMineMap();

    public abstract void callInit(Player player);

}
