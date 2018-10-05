package it.maymity.sellchest;

import it.maymity.sellchest.boosts.BoostManager;
import it.maymity.sellchest.boosts.BuyBoostAction;
import it.maymity.sellchest.commands.SellChestCommand;
import it.maymity.sellchest.items.ItemManager;
import it.maymity.sellchest.listeners.SignEvents;
import it.maymity.sellchest.listeners.UpdaterEvents;
import it.xquickglare.qlib.actions.ActionManager;
import it.xquickglare.qlib.configuration.YAMLConfiguration;
import it.xquickglare.qlib.objects.QLibPlugin;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class SellChest extends QLibPlugin {

    private final double CONFIG_VERSION = 4.6;
    @Getter private static SellChest instance;

    @Getter private YAMLConfiguration configuration;
    @Getter private YAMLConfiguration messages;
    @Getter private YAMLConfiguration boostConfig;
    
    @Getter private Economy economy;

    @Getter private SpigotUpdater spigotUpdater;
    @Getter private ItemManager itemManager;
    @Getter private BoostManager boostManager;

    /*
     * TODO
     * 1. Usare QLib per booster menu FATTO
     * 2. Cambiare sistema dei booster FATTO
     * 3. Migliorare la blacklist
     * 4. Ottimizzare il plugin FATTO
     */
    
    public void onEnable() { instance = this;
        super.onEnable();
        logs.infoLog("SellChest > Start plugin...", true);
        
        setupDepends();
        registerConfig();
        checkForUpdates();

        itemManager = new ItemManager();
        boostManager = new BoostManager();

        registerEvents();
        registerExecutors();
        registerActions();

        logs.infoLog("SellChest > Plugin enabled!", true);
        logs.infoLog("SellChest > Plugin created by Maymity!", true);
    }

    @Override
    public void onDisable() {
        boostManager.saveBoosts();
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new SignEvents(), this);
        Bukkit.getPluginManager().registerEvents(new UpdaterEvents(), this);
    }

    private void registerExecutors() {
        SellChestCommand sellChestCommand = new SellChestCommand();
        
        getCommand("sellchest").setExecutor(sellChestCommand);
        getCommand("sellchest").setTabCompleter(sellChestCommand);
    }

    private void registerConfig(){
        configuration = new YAMLConfiguration("config", this);
        messages = new YAMLConfiguration("messages", this);
        boostConfig = new YAMLConfiguration("boosts", this);

        if (configuration.getString("settings.config_version") == null || configuration.getDouble("settings.config_version") < CONFIG_VERSION) {
            logs.infoLog("&cYou config is out of date!", true);
            logs.infoLog("&cPlease, regenerate you config file!", true);
            logs.infoLog("&eYour version: &a" + configuration.getDouble("settings.config_version"), true);
            logs.infoLog("&cNewest version: &a" + CONFIG_VERSION, true);
        }
    }

    private void registerActions(){
        ActionManager am = getQLib().getActionManager();

        am.addAction(new BuyBoostAction());
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
    
    private void checkForUpdates(){
        spigotUpdater = new SpigotUpdater(this, 43857);
        try {
            if (spigotUpdater.checkForUpdates()) {
                logs.infoLog("========================================================", true);
                logs.infoLog("SellChest Update Checker", true);
                logs.infoLog("There is a new update available", true);
                logs.infoLog("Latest Version: " + spigotUpdater.getNewVersion(), true);
                logs.infoLog("Your Version: " + getDescription().getVersion(), true);
                logs.infoLog("Get it here: " + spigotUpdater.getResourceURL(), true);
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
            logs.errorLog("For the stacktrace please check log file. ", e.getStackTrace());
            logs.errorLog("========================================================", true);
        }
    }
}