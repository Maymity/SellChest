package it.maymity.sellchest;

import it.maymity.sellchest.commands.SellChestCommand;
import it.maymity.sellchest.listeners.*;
import it.maymity.sellchest.managers.ItemsManager;
import it.maymity.sellchest.newmanagers.ItemManager;
import it.xquickglare.qlib.configuration.YAMLConfiguration;
import it.xquickglare.qlib.objects.QLibPlugin;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;

public class SellChest extends QLibPlugin {

    private final double CONFIG_VERSION = 4.6;
    @Getter private static SellChest instance;
    
    @Getter private YAMLConfiguration messages;
    @Getter private YAMLConfiguration configuration;
    
    @Getter private Economy economy;
    
    @Getter private ItemManager itemManager;
    
    @Getter private ArrayList<ItemsManager> blacklist = new ArrayList<>();

    /*
     * TODO
     * 1. Usare QLib per booster menu
     * 2. Cambiare sistema dei booster
     * 3. Migliorare la blacklist
     * 4. Ottimizzare il plugin
     */
    
    public void onEnable() { instance = this;
        super.onEnable();
        logs.infoLog("SellChest > Start plugin...", true);
        
        setupDepends();
        registerConfig();
        registerEvents();
        registerExecutors();
        itemManager = new ItemManager();
        thereIsAnUpdate();
        
        logs.infoLog("SellChest > Plugin enabled!", true);
        logs.infoLog("SellChest > Plugin created by Maymity!", true);
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new InventoryClick(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
        Bukkit.getPluginManager().registerEvents(new SignChange(), this);
    }

    private void registerExecutors() {
        SellChestCommand sellChestCommand = new SellChestCommand();
        
        getCommand("sellchest").setExecutor(sellChestCommand);
        getCommand("sellchest").setTabCompleter(sellChestCommand);
    }

    private void registerConfig(){
        configuration = new YAMLConfiguration("config", this);
        messages = new YAMLConfiguration("messages", this);

        if (configuration.getString("settings.config_version") == null || configuration.getDouble("settings.config_version") < CONFIG_VERSION) {
            logs.infoLog("&cYou config is out of date!", true);
            logs.infoLog("&cPlease, regenerate you config file!", true);
            logs.infoLog("&eYour version: &a" + configuration.getDouble("settings.config_version"), true);
            logs.infoLog("&cNewest version: &a" + CONFIG_VERSION, true);
        }
    }
    
    private void setupDepends(){
        //Vault
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null)
            economy = economyProvider.getProvider();
        else {
            logs.errorLog("SellChest > This plugin requires Vault and an economy plugin for work!", true);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
    
    private void thereIsAnUpdate(){
        SpigotUpdater updater = new SpigotUpdater(this, 43857);
        try {
            if (updater.checkForUpdates()) {
                Utils.getInstance().setBoolUpdate(true);
                Utils.getInstance().setUpdateLink(updater.getResourceURL());
                logs.infoLog("========================================================", true);
                logs.infoLog("SellChest Update Checker", true);
                logs.infoLog("There is a new update available", true);
                logs.infoLog("Latest Version: " + updater.getLatestVersion(), true);
                logs.infoLog("Your Version: " + updater.getPlugin().getDescription().getVersion(), true);
                logs.infoLog("Get it here: " + updater.getResourceURL(), true);
                logs.infoLog("========================================================", true);
            } else {
                logs.infoLog("========================================================", true);
                logs.infoLog("SellChest Update Checker", true);
                logs.infoLog("You are using the latest version!", true);
                logs.infoLog("========================================================", true);
            }
        } catch (Exception e) {
            logs.errorLog("========================================================", true);
            logs.errorLog("SellChest Update Checker", true);
            logs.errorLog("Could not connect to Spigot's API!", true);
            logs.errorLog("Error: ", e.getStackTrace());
            logs.errorLog("========================================================", true);
        }
    }
}