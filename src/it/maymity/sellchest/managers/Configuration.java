package it.maymity.sellchest.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;

public class Configuration {

    private File file;
    private FileConfiguration config;

    public Configuration(String fileName, JavaPlugin plugin, boolean saveDefault){
        file = new File(plugin.getDataFolder(), fileName);
        file.getParentFile().mkdirs();

        if(!file.exists()){
            if(saveDefault)
                plugin.saveResource(fileName, true);
            else try {
                file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload(){
        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return config;
    }
}

