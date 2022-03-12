package dev.minealert;

import org.bukkit.plugin.java.JavaPlugin;

public class MineAlert extends JavaPlugin {

    private MineAlertRegistries registries;

    public void onEnable() {
        registries = new MineAlertRegistries();
    }

    public static MineAlert getInstance() {
        return MineAlert.getPlugin(MineAlert.class);
    }

    public MineAlertRegistries getRegistries() {
        return registries;
    }
}
