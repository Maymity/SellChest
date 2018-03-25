package it.maymity.sellchest.listeners;

import it.maymity.sellchest.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.Sign;
import it.maymity.sellchest.managers.MessagesManager;

public class SignChange implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Sign s = (Sign) event.getBlock().getState().getData();
        Block attached = event.getBlock().getRelative(s.getAttachedFace());
        Player player = event.getPlayer();
        if (attached.getType() == Material.CHEST || attached.getType() == Material.TRAPPED_CHEST ) {
            if (event.getLine(0).equals(Utils.getInstance().getConfig().getString("settings.sign_line"))) {
                if (event.getPlayer().hasPermission("sellchest.sign.create")) {
                    for (int i = 0; i < Utils.getInstance().getSignmessage().size(); i++) {
                        if (i < 4)
                            event.setLine(i, ChatColor.translateAlternateColorCodes('&', Utils.getInstance().getSignmessage().get(i)));
                        else {
                            MessagesManager.getInstance().sendMessage(player, Utils.getInstance().getMessages().getString("messages.error"));
                            event.setCancelled(true);
                            event.getBlock().breakNaturally();
                        }
                    }
                } else {
                    MessagesManager.getInstance().sendMessage(player, Utils.getInstance().getMessages().getString("messages.no_permission"));
                    event.setCancelled(true);
                    event.getBlock().breakNaturally();
                }
            }
        }
    }
}