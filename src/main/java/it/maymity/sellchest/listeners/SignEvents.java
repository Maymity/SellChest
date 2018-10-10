package it.maymity.sellchest.listeners;

import it.maymity.sellchest.SellChest;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.Sign;

import java.util.List;

public class SignEvents implements Listener {
    
    private final SellChest plugin = SellChest.getInstance();

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Block block = event.getBlock();
        Sign sign = (Sign) block.getState().getData();
        Block attached = block.getRelative(sign.getAttachedFace());
        Player player = event.getPlayer();

        if(attached.getType() != Material.CHEST && attached.getType() != Material.TRAPPED_CHEST)
            return;

        if(!event.getLine(0).equalsIgnoreCase(plugin.getConfiguration().getString("settings.sign_line")))
            return;

        if(!player.hasPermission("sellchest.sign.create")){
            plugin.getMessages().getMessage("messages.no_permission").sendMessage(player);
            block.breakNaturally();
            return;
        }

        List<String> newSign = plugin.getConfiguration().getMultilineMessage("signMessage").toStringList();
        for (int i = 0; i < newSign.size(); i++)
            event.setLine(i, newSign.get(i));
    }

    @EventHandler
    public void onSignInteract(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if(block == null)
            return;

        if(block.getType() != Material.WALL_SIGN)
            return;

        org.bukkit.block.Sign signLines = (org.bukkit.block.Sign) block.getState();
        if(!isSellSign(signLines.getLines()))
            return;

        if(!player.hasPermission("sellchest.chest.sell")){
            plugin.getMessages().getMessage("messages.no_permissionsell").sendMessage(player);
            return;
        }

        Sign sign = (Sign)block.getState().getData();
        Block chest = block.getRelative(sign.getAttachedFace());
        if(!(chest.getState() instanceof InventoryHolder))
            return;

        InventoryHolder inventoryHolder = (InventoryHolder) chest.getState();
        Inventory inventory = inventoryHolder.getInventory();
        processChest(player, inventory);
    }

    private boolean isSellSign(String[] lines){
        List<String> validSign = plugin.getConfiguration().getMultilineMessage("signMessage").toStringList();
        for (int i = 0; i < lines.length; i++) {
            if(!lines[i].equalsIgnoreCase(validSign.get(i)))
                return false;
        }

        return true;
    }

    private void processChest(Player player, Inventory inv) {
        double price = SellChest.getInstance().getItemManager().getItemsPrice(inv);

        if(price == 0){
            plugin.getMessages().getMessage("messages.no_item").sendMessage(player);
            return;
        }

        if(plugin.getBoostManager().hasBoost(player))
            price *= plugin.getBoostManager().getMultiplier();

        plugin.getEconomy().depositPlayer(player, price);
        plugin.getMessages().getMessage("messages.sell_message").setVariable("money", String.valueOf(price)).sendMessage(player);
    }

}