package it.maymity.sellchest.listeners;

import it.maymity.sellchest.Utils;
import it.maymity.sellchest.managers.MessagesManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (Utils.getInstance().getConfig().getBoolean("settings.check_update_on_join")) {
            if (player.hasPermission("sellchest.checkupdate")) {
                if (Utils.getInstance().getNewUpdateCheck()) {
                    MessagesManager.getInstance().sendMessage(player, Utils.getInstance().getMessages().getString("messages.update_message"));
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