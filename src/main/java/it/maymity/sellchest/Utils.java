package it.maymity.sellchest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class Utils {

    private final SellChest plugin = SellChest.getInstance();
    
    private static Utils instance;

    private List<String> signmessage;
    private boolean newupdate = false;
    private String updatelink;

    public static synchronized Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    public void setBoolUpdate(boolean b){
        newupdate = b;
    }
    public void setUpdateLink(String b){ updatelink = b; }
    public Boolean getNewUpdateCheck() { return newupdate;}
    public String getUpdateLink() { return updatelink; }

    public List<String> getSignmessage() {
        signmessage = Bukkit.getPluginManager().getPlugin("SellChest").getConfig().getStringList("signmessage");
        return signmessage;
    }

    /* TODO usare QLIb per menu
    public static Inventory getBoostInventory() {
        Inventory gamesell = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', Utils.getInstance().getConfig().getString("game_sell_booster.display_name")));
        ItemStack item = new ItemStack(Material.valueOf(getInstance().getConfig().getString("game_sell_booster.item").toUpperCase()));
        ItemMeta meta = item.getItemMeta();
        String name = ChatColor.translateAlternateColorCodes('&', getInstance().getConfig().getString("game_sell_booster.item_name"));
        meta.setDisplayName(name);

        int time = getInstance().getConfig().getInt("game_sell_booster.time");
        float minutes = time / 60;
        double multiplier = getInstance().getConfig().getDouble("game_sell_booster.boost%");
        ArrayList<String> lore = new ArrayList<String>();

        for (String line : getInstance().getConfig().getStringList("game_sell_booster.lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line.replaceAll("%minutes%", Float.toString(minutes)).replaceAll("%multiplier%", Double.toString(multiplier)).replaceAll("%seconds%", Integer.toString(time))));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        gamesell.setItem(4, item);

        return gamesell;
    }*/

    public Boolean checkSign(Sign signLine){
        boolean control = false;
        for (int i = 0; i < Utils.getInstance().getSignmessage().size(); i++) {
            if (signLine.getLine(i).equals(ChatColor.translateAlternateColorCodes('&', Utils.getInstance().getSignmessage().get(i))))
                control = true;
            else
                control = false;
        }
        if(control)
            return true;
        else
            return false;
    }

    public void processChest(Player player, Inventory inv) {
        double price = SellChest.getInstance().getItemManager().getItemsPrice(inv);
        double boost = SellChest.getInstance().getConfiguration().getDouble("game_sell_booster.boost%");

        if(price == 0){
            plugin.getMessages().getMessage("messages.no_item").sendMessage(player);
            return;
        }
        
        if(plugin.getBoostManager().hasBoost(player))
            price *= boost;
        
        plugin.getEconomy().depositPlayer(player, price);
        plugin.getMessages().getMessage("messages.sell_message").setVariable("money", String.valueOf(price)); 
    }
    
}