package it.maymity.sellchest.listeners;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;
import it.maymity.sellchest.managers.MessagesManager;
import it.maymity.sellchest.Main;
import it.maymity.sellchest.Utils;

public class InventoryClick implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        Inventory inventory = event.getInventory();
        double prize= Utils.getInstance().getConfig().getDouble("game_sell_booster.prize");
        int time = Utils.getInstance().getConfig().getInt("game_sell_booster.time");

        if (inventory.getName() != null) {
            if (inventory.getName().equals(Utils.getBoostInventory().getName())){
                if (clicked.getType() != null){
                    if (clicked.getType() == Material.valueOf(Utils.getInstance().getConfig().getString("game_sell_booster.item").toUpperCase())){
                        if (Main.getInstance().getEconomy().has(player, prize)){
                            MessagesManager.getInstance().sendMessage(player, Utils.getInstance().getMessages().getString("messages.boost_message").replaceAll("%multiplier%", Integer.toString(Utils.getInstance().getConfig().getInt("game_sell_booster.boost%"))));
                            Main.getInstance().getEconomy().withdrawPlayer(player, prize);
                            Utils.getInstance().setPlayerBoost(player, time);
                            player.closeInventory();
                        }
                        else
                            MessagesManager.getInstance().sendMessage(player, Utils.getInstance().getMessages().getString("messages.no_money"));
                    }
                    event.setCancelled(true);
                }
            }
        }
    }
}