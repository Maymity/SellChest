package it.maymity.sellchest.listeners;

import it.maymity.sellchest.SellChest;
import it.maymity.sellchest.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    
    private final SellChest plugin = SellChest.getInstance();
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (plugin.getConfiguration().getBoolean("settings.check_update_on_join")) {
            if (player.hasPermission("sellchest.checkupdate")) {
                if (Utils.getInstance().getNewUpdateCheck()) {
                    plugin.getConfiguration().getMessage("messages.update_message").sendMessage(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDownload:&e " + Utils.getInstance().getUpdateLink()));
                }
            }
        }
        if (Utils.getInstance().getTime().containsKey(player.getName())){
            Utils.getInstance().setPlayerBoost(player, Utils.getInstance().getTime().get(player.getName()));
            Utils.getInstance().getTime().remove(player.getName());
        }
    }
}