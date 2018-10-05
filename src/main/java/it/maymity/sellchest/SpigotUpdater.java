package it.maymity.sellchest;

import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URL;

public class SpigotUpdater {

    private int project;
    @Getter private String newVersion;
    private JavaPlugin plugin;

    @Getter private boolean thereIsAnUpdate;

    SpigotUpdater(JavaPlugin plugin, int project) {
        this.plugin = plugin;
        this.newVersion = plugin.getDescription().getVersion();
        this.project = project;
    }

    public String getResourceURL() {
        return "https://www.spigotmc.org/resources/" + project;
    }

    boolean checkForUpdates() throws Exception {
        URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + project);
        this.newVersion = IOUtils.readLines(url.openStream()).get(0);
        this.thereIsAnUpdate = !plugin.getDescription().getVersion().equals(newVersion);

        return thereIsAnUpdate;
    }

}