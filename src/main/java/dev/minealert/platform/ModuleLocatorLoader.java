package dev.minealert.platform;

import dev.minealert.alerts.AbstractAlertModule;
import dev.minealert.database.DatabaseInit;
import dev.minealert.database.SQLUtils;
import dev.minealert.file.DatabaseFile;
import dev.minealert.file.OreSettingsFile;
import dev.minealert.file.lang.LangFile;
import dev.minealert.module.AbstractModuleLoader;
import dev.minealert.utils.MineDataUtils;
import dev.minealert.utils.WeakList;

public abstract class ModuleLocatorLoader {

    private static final WeakList<Class<? extends AbstractModuleLoader>> instanceModuleList = new WeakList<>(
            OreSettingsFile.class, DatabaseFile.class, LangFile.class, DatabaseInit.class, SQLUtils.class);


    public void callOnLoadUp() {
        initInstances();
        initAddons();
    }

    public void initInstances() {
        for (int i = 0; i < instanceModuleList.getSize(); i++) {
            AbstractModuleLoader.loadModule(instanceModuleList.getElement(i));
        }
    }

    public void initAddons() {
        for(int i = 0; i < MineDataUtils.getModuleAlertList().getSize(); i++){
            AbstractModuleLoader.loadModule(MineDataUtils.getModuleAlertList().getElement(i));
        }
    }

    public WeakList<Class<? extends AbstractModuleLoader>> getInstanceModuleList() {
        return instanceModuleList;
    }

}
