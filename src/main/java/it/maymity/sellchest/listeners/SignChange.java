package it.maymity.sellchest.listeners;

import it.maymity.sellchest.SellChest;
import it.maymity.sellchest.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.Sign;

public class SignChange implements Listener {

    private final SellChest plugin = SellChest.getInstance();
    
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Sign s = (Sign) event.getBlock().getState().getData();
        Block attached = event.getBlock().getRelative(s.getAttachedFace());
        Player player = event.getPlayer();
        if (attached.getType() == Material.CHEST || attached.getType() == Material.TRAPPED_CHEST ) {
            if (event.getLine(0).equals(SellChest.getInstance().getConfiguration().getString("settings.sign_line"))) {
                if (event.getPlayer().hasPermission("sellchest.sign.create")) {
                    //TODO Trovare metodo migliore plzzz
                    for (int i = 0; i < Utils.getInstance().getSignmessage().size(); i++) {
                        if (i < 4)
                            event.setLine(i, ChatColor.translateAlternateColorCodes('&', Utils.getInstance().getSignmessage().get(i)));
                        else {
                            plugin.getMessages().getMessage("messages.error").sendMessage(player);
                            event.setCancelled(true);
                            event.getBlock().breakNaturally();
                        }
                    }
                } else {
                    plugin.getMessages().getMessage("messages.no_permission").sendMessage(player);
                    event.setCancelled(true);
                    event.getBlock().breakNaturally();
                }
            }
        }
    }
}