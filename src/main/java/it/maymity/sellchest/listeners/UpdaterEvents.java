package it.maymity.sellchest.listeners;

import it.maymity.sellchest.SellChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdaterEvents implements Listener {
    
    private final SellChest plugin = SellChest.getInstance();
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(!player.hasPermission("sellchest.checkupdate"))
            return;

        if(!plugin.getConfiguration().getBoolean("settings.check_update_on_join"))
            return;

        if(plugin.getSpigotUpdater().isThereIsAnUpdate())
            plugin.getMessages().getMessage("messages.update_message").setVariable("url", plugin.getSpigotUpdater().getResourceURL()).sendMessage(player);
    }
}