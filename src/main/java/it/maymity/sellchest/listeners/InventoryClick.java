package it.maymity.sellchest.listeners;

import it.maymity.sellchest.SellChest;
import it.maymity.sellchest.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClick implements Listener {
    
    private final SellChest plugin = SellChest.getInstance();
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        /* TODO
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        Inventory inventory = event.getInventory();
        double prize = plugin.getConfiguration().getDouble("game_sell_booster.prize");
        int time = plugin.getConfiguration().getInt("game_sell_booster.time");

        if (inventory.getName() != null) {
            if (inventory.getName().equals(Utils.getBoostInventory().getName())){
                if (clicked.getType() != null){
                    if (clicked.getType() == Material.valueOf(plugin.getConfiguration().getString("game_sell_booster.item").toUpperCase())){
                        if (SellChest.getInstance().getEconomy().has(player, prize)){
                            plugin.getMessages().getMessage("messages.boost_message").setVariable("multiplier", plugin.getConfiguration().getString("game_sell_booster.boost%")).sendMessage(player);
                            SellChest.getInstance().getEconomy().withdrawPlayer(player, prize);
                            Utils.getInstance().setPlayerBoost(player, time);
                            player.closeInventory();
                        } else
                            plugin.getMessages().getMessage("messages.no_money").sendMessage(player);
                    }
                    event.setCancelled(true);
                }
            }
        }*/
    }
}