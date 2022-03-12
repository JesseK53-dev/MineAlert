package dev.minealert.utils;

import dev.minealert.alerts.AbstractAlertModule;
import dev.minealert.alerts.types.*;
import dev.minealert.module.AbstractModuleLoader;
import org.bukkit.entity.Player;

import java.util.Map;

public class MineDataUtils {

    private static final WeakList<Class<? extends AbstractAlertModule>> moduleAlertList = new WeakList<>(
            AncientDebrisAlert.class, CoalAlert.class, CopperAlert.class, DeepCoalAlert.class, DeepCopperAlert.class, DeepDiamondAlert.class,
            DeepEmeraldAlert.class, DeepGoldAlert.class, DeepIronAlert.class, DeepLapisAlert.class, DeepRedstoneAlert.class,
            DiamondAlert.class, EmeraldAlert.class, GoldAlert.class, IronAlert.class, LapisAlert.class, NetherGoldAlert.class, RedstoneAlert.class,
            SpawnerAlert.class);

    public static void clearMineData() {
        for (int i = 0; i < moduleAlertList.getSize(); i++) {
            Class<? extends AbstractAlertModule> alertClasses = moduleAlertList.getElement(i);
            AbstractModuleLoader.getModule(alertClasses).ifPresent(alerts -> alerts.clearData(alerts.getMineMap()));
        }
    }

    public static void removeMiner(Player miner) {
        for (int i = 0; i < moduleAlertList.getSize(); i++) {
            Class<? extends AbstractAlertModule> alertClasses = moduleAlertList.getElement(i);
            if (AbstractModuleLoader.getModule(alertClasses).isPresent()) {
                Map<String, Integer> mineMap = AbstractModuleLoader.getModule(alertClasses).get().getMineMap();
                AbstractModuleLoader.getModule(alertClasses).ifPresent(alert -> mineMap.remove(miner.getName()));
            }
        }
    }

    public static void addAll(Player allInstance) {
        for (int i = 0; i < moduleAlertList.getSize(); i++) {
            Class<? extends AbstractAlertModule> alertClasses = moduleAlertList.getElement(i);
            AbstractModuleLoader.getModule(alertClasses).ifPresent(alert -> alert.callInit(allInstance));
        }
    }


    public static WeakList<Class<? extends AbstractAlertModule>> getModuleAlertList() {
        return moduleAlertList;
    }
}
