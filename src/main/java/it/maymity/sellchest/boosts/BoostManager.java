package it.maymity.sellchest.boosts;

import it.maymity.sellchest.SellChest;
import it.xquickglare.qlib.configuration.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BoostManager {
    
    private final SellChest plugin = SellChest.getInstance();
    
    private List<Boost> boosts;
    public BoostManager(){
        boosts = new ArrayList<>();
        
        loadFromConfig();
        setupBoostTask();
    }

    public boolean hasBoost(Player player){
        for (Boost boost : boosts) {
            if(boost.getPlayer() != player.getUniqueId())
                continue;

            return boost.getExpireTime() <= System.currentTimeMillis();
        }   
        
        return false;
    }
    
    public void saveBoosts(){
        for (Boost boost : boosts)
            plugin.getBoostConfig().set(boost.getPlayer().toString(), boost.getExpireTime());
        plugin.getBoostConfig().saveConfiguration();

        plugin.getLogs().infoLog("Saved all boosts in the config.", true);
    }
    
    private void loadFromConfig(){
        Configuration section = plugin.getBoostConfig().getSection("boosts");

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
