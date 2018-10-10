package it.maymity.sellchest.boosts;

import it.maymity.sellchest.SellChest;
import it.xquickglare.qlib.configuration.Configuration;
import it.xquickglare.qlib.configuration.YAMLConfiguration;
import it.xquickglare.qlib.menus.objects.ConfigMenu;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BoostManager {
    
    private final SellChest plugin = SellChest.getInstance();

    private YAMLConfiguration boostMenuConfig;
    @Getter private ConfigMenu boostMenu;

    @Getter private double multiplier;
    @Getter private int defaultSeconds;

    private List<Boost> boosts;

    public BoostManager(){
        boosts = new ArrayList<>();
        
        loadFromConfig();
        setupBoostTask();
        reload();
    }

    void createBoost(Player player){
        boosts.add(new Boost(
                player.getUniqueId(),
                System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(defaultSeconds)
        ));
    }

    public boolean hasBoost(Player player){
        return getBoost(player) != null;
    }

    public long getSecondsRemaining(Player player){
        Boost boost = getBoost(player);
        if(boost == null)
            return -1;

        return TimeUnit.MILLISECONDS.toSeconds(boost.getExpireTime()-System.currentTimeMillis());
    }

    private Boost getBoost(Player player){
        for (Boost boost : boosts) {
            if(boost.getPlayer().toString().equals(player.getUniqueId().toString()))
                return boost;
        }

        return null;
    }

    public void reload(){
        multiplier = plugin.getConfiguration().getDouble("game_sell_booster.multiplier");
        defaultSeconds = plugin.getConfiguration().getInt("game_sell_booster.time");

        if(boostMenuConfig == null)
            boostMenuConfig = new YAMLConfiguration("boostMenu", plugin);
        else
            boostMenuConfig.reload();

        if(boostMenu != null)
            plugin.getQLib().getMenuManager().removeMenu(boostMenu);

        boostMenu = new ConfigMenu(plugin, "BOOST-MENU", boostMenuConfig);

        boostMenu.registerPlaceholder("multiplier", String.valueOf(multiplier));
        boostMenu.registerPlaceholder("minutes", String.valueOf(defaultSeconds/60.0));
        boostMenu.registerPlaceholder("seconds", String.valueOf(defaultSeconds));

        plugin.getQLib().getMenuManager().registerMenu(boostMenu);
    }

    public void saveBoosts(){
        for (Boost boost : boosts)
            plugin.getBoostConfig().set("boosts." + boost.getPlayer().toString(), boost.getExpireTime());
        plugin.getBoostConfig().saveConfiguration();

        plugin.getLogs().infoLog("Saved all boosts in the config.", true);
    }
    
    private void loadFromConfig(){
        Configuration section = plugin.getBoostConfig().getSection("boosts");
        if(section == null)
            return;

        if(section.getKeys() == null)
            return;

        for (String key : section.getKeys()) {
            UUID uuid = UUID.fromString(key);
            long time = section.getInt(key);
            
            if(time > System.currentTimeMillis())
                continue;
            
            boosts.add(new Boost(uuid, time));
            plugin.getLogs().infoLog("Loaded boost for UUID " + uuid, false);
        }
        
        plugin.getLogs().infoLog("Loaded " + boosts.size() + " boosts.", true);
    }
    
    private void setupBoostTask(){
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (Boost boost : boosts.toArray(new Boost[0])) {
                if(boost.getExpireTime() > System.currentTimeMillis())
                    boosts.remove(boost);
            }
        }, 0, 20*60);
    }
}
