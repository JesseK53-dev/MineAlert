package dev.minealert.file.lang;

import dev.minealert.file.AbstractFile;
import dev.minealert.module.ModuleInfo;
import dev.minealert.module.ModuleType;
import org.bukkit.configuration.Configuration;

@ModuleInfo(moduleName = "Lang Settings File", moduleDesc = "Allows you to edit messages", moduleType = ModuleType.INSTANCE)
public class LangFile extends AbstractFile {

    @Override
    public void registerFile() {
        createFile("messages.yml");
        setData();
        saveFile();
    }

    @Override
    public void setData() {
        if (isFileNotEmpty()) return;
        final Configuration configuration = getFileConfiguration();
        for (Lang item : Lang.CACHE) {
            configuration.set(item.getPath(), item.getValue());
        }
    }
}
