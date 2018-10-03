package it.maymity.sellchest;

import it.maymity.sellchest.managers.ItemsManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import it.maymity.sellchest.listeners.PlayerInteract;
import it.maymity.sellchest.listeners.SignChange;
import it.maymity.sellchest.listeners.InventoryClick;
import it.maymity.sellchest.listeners.PlayerJoin;
import it.maymity.sellchest.listeners.PlayerQuit;
import it.maymity.sellchest.commands.SellChest;
import it.maymity.sellchest.managers.Configuration;
import org.bukkit.Material;
import java.util.ArrayList;

public class Main extends JavaPlugin implements Listener {

    static double config_version = 4.6;
    private static Main instance;
    private Economy economy;
    private Configuration messages;
    private ArrayList<ItemsManager> items = new ArrayList<ItemsManager>();
    private ArrayList<ItemsManager> blacklist = new ArrayList<ItemsManager>();

    public static Main getInstance(){
        return instance;
    }
    public Economy getEconomy() {
        return economy;
    }
    public ArrayList<ItemsManager> getItems() {return items;}

    public ArrayList<ItemsManager> getBlacklist() { return blacklist;}

    public Configuration getMessages(){ return messages;}

    public void onEnable() {
        instance = this;
        System.out.println("SellChest > Start plugin...");

        System.out.println("SellChest > Loading Depencences...");
        setupDepends();
        System.out.println("SellChest > Dependences loaded!");

        System.out.println("SellChest > Registring the event...");
        registerListeners();
        System.out.println("SellChest > Event registred!");

        System.out.println("SellChest > Registing the command...");
        registerExecutors();
        System.out.println("SellChest > Command registred!");

        System.out.println("SellChest > Loading items...");
        loadItems();

        SpigotUpdater updater = new SpigotUpdater(this, 43857);
        try {
            if (updater.checkForUpdates()) {
                Utils.getInstance().setBoolUpdate(true);
                Utils.getInstance().setUpdateLink(updater.getResourceURL());
                System.out.println("========================================================");
                System.out.println("SellChest Update Checker");
                System.out.println("There is a new update available");
                System.out.println("Latest Version: " + updater.getLatestVersion());
                System.out.println("Your Version: " + updater.getPlugin().getDescription().getVersion());
                System.out.println("Get it here: " + updater.getResourceURL());
                System.out.println("========================================================");
            }
            else{
                System.out.println("========================================================");
                System.out.println("SellChest Update Checker");
                System.out.println("You are using the latest version!");
                System.out.println("========================================================");
            }
        } catch (Exception e) {
            System.out.println("========================================================");
            System.out.println("SellChest Update Checker");
            System.out.println("Could not connect to Spigot's API!");
            System.out.println("Error: ");
            e.printStackTrace();
            System.out.println("========================================================");
        }

        System.out.println("SellChest > Plugin enabled!");
        System.out.println("SellChest > Plugin created by Maymity!");


        if ((!Utils.getInstance().getConfig().contains("settings.config_version")) || (Utils.getInstance().getConfig().getDouble("settings.config_version") < config_version)) {
            System.out.println("&cYou config is out of date!");
            System.out.println("&cPlease, regenerate you config file!");
            System.out.println("&eYour version: &a" + Utils.getInstance().getConfig().getDouble("settings.config_version"));
            System.out.println("&cNewest version: &a" + config_version);
        }

        messages = new Configuration("messages.yml", this, true);
        saveDefaultConfig();
    }

    public void onDisable() {
        System.out.println("SellChest > Plugin disabled!");
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new InventoryClick(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
        Bukkit.getPluginManager().registerEvents(new SignChange(), this);
    }

    private void registerExecutors() {
        Bukkit.getPluginCommand("sellchest").setExecutor(new SellChest());
    }

    private void setupDepends(){
        //Vault
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null)
            economy = economyProvider.getProvider();
        else {
            System.out.println("SellChest > This plugin requires Vault and an economy plugin for work!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public void loadItems(){
        int item = 0;
        int black = 0;
        for (String s : getConfig().getStringList("itemsold")) {
            String[] split = s.split(":");
            double prezzo;
            Material material = Material.getMaterial(split[0].toUpperCase());
            short damage = 0;
            if (split.length == 3) {
                damage = Short.parseShort(split[1]);
                prezzo = Double.parseDouble(split[2]);
            }
            else
                prezzo = Double.parseDouble(split[1]);
            item++;
            items.add(new ItemsManager(material, damage, prezzo));
        }
        System.out.println("SellChest > " + item + " items loaded!");
        for (String s : getConfig().getStringList("blacklist")) {
            String[] split = s.split(":");
            Material material = Material.getMaterial(split[0].toUpperCase());
            short damage = 0;
            if (split.length == 2)
                damage = Short.parseShort(split[1]);
            black++;
            blacklist.add(new ItemsManager(material, damage));
        }
        System.out.println("SellChest > " + black + " blacklisted items loaded!");
    }
}