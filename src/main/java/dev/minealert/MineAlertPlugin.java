package dev.minealert;

import dev.minealert.platform.ModuleLocatorLoader;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public abstract class MineAlertPlugin extends ModuleLocatorLoader {

    protected void registerRegistries() {
        callOnLoadUp();
        registerFiles();
        registerDatabase();
        registerCommands();
        registerListeners();
    }

    protected abstract void registerDatabase();

    protected abstract void registerFiles();

    protected abstract void registerCommands();

    protected abstract void registerListeners();

    protected void addListener(Listener... listeners) {
        for (Listener all : listeners) {
            Bukkit.getPluginManager().registerEvents(all, MineAlert.getInstance());
        }
    }
}
