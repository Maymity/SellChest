package it.maymity.sellchest.listeners;

import it.maymity.sellchest.SellChest;
import it.maymity.sellchest.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.Sign;

public class PlayerInteract implements Listener {
    
    private final SellChest plugin = SellChest.getInstance();
    
    @EventHandler
    public void onSignInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Block b = event.getClickedBlock();

        if (b != null) {
            if (b.getType() != null) {
                if (b.getType() != Material.AIR) {
                    if (b.getType() == Material.WALL_SIGN) {
                        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                            org.bukkit.block.Sign signLine = (org.bukkit.block.Sign) event.getClickedBlock().getState();
                            if (Utils.getInstance().checkSign(signLine)) {
                                Sign s = (Sign) event.getClickedBlock().getState().getData();
                                Block attached = event.getClickedBlock().getRelative(s.getAttachedFace());
                                if (event.getPlayer().hasPermission("sellchest.chest.sell")) {
                                    if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                                        if (attached.getState() instanceof InventoryHolder) {
                                            InventoryHolder i = ((InventoryHolder) attached.getState());
                                            Inventory inv = i.getInventory();
                                            if (((InventoryHolder) attached.getState()).getInventory().getType().equals(InventoryType.CHEST)) {
                                                Utils.getInstance().processChest(p, inv);
                                            }
                                        }
                                    } else
                                        plugin.getMessages().getMessage("messages.only_survival").sendMessage(p);
                                } else
                                    plugin.getMessages().getMessage("messages.no_permissionsell").sendMessage(p);
                            }
                        }
                    }
                }
            }
        }
    }
}