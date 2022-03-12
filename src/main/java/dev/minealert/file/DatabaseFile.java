package dev.minealert.file;

import dev.minealert.module.ModuleInfo;
import dev.minealert.module.ModuleType;
import org.bukkit.configuration.file.FileConfiguration;

@ModuleInfo(moduleName = "Database File", moduleDesc = "File that allows to edit database information", moduleType = ModuleType.INSTANCE)
public class DatabaseFile extends AbstractFile {

    @Override
    public void registerFile() {
        createFile("database.yml");
        setData();
        saveFile();
    }

    @Override
    public void setData() {
        if (isFileNotEmpty()) return;
        final FileConfiguration config = getFileConfiguration();
        config.set("enable", false);
        config.set("host", "localhost");
        config.set("port", 3306);
        config.set("database", "minealertdb");
        config.set("user", "username");
        config.set("password", "admin");
        config.set("driver", "MYSQL");
        config.set("timeout", 5000);
        config.set("maxpool", 10);
    }
}
