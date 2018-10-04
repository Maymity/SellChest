package it.maymity.sellchest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

public class Utils {

    private final SellChest plugin = SellChest.getInstance();
    
    private static Utils instance;

    private HashMap<Player, Integer> cooldownTime = new HashMap<Player, Integer>();
    private HashMap<Player, BukkitRunnable> cooldownTask = new HashMap<Player, BukkitRunnable>();
    private HashMap<String, Integer> timeRemaining = new HashMap<String, Integer>();

    private List<String> signmessage;
    private boolean newupdate = false;
    private String updatelink;

    public static synchronized Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    public HashMap<Player, Integer> getCooldownTime(){ return cooldownTime;}
    public HashMap<Player, BukkitRunnable> getCooldownTask(){ return cooldownTask;}
    public HashMap<String, Integer> getTime(){return timeRemaining;}

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
        double prezzo = SellChest.getInstance().getItemManager().getItemsPrice(inv);
        double boost = SellChest.getInstance().getConfiguration().getDouble("game_sell_booster.boost%");

        if(prezzo == 0){
            plugin.getMessages().getMessage("messages.no_item").sendMessage(player);
            return;
        }
        
        if(hasBoost(player))
            prezzo *= boost;
        
        plugin.getEconomy().depositPlayer(player, prezzo);
        plugin.getMessages().getMessage("messages.sell_message").setVariable("money", String.valueOf(prezzo)); 
    }

    public Boolean hasBoost(Player player) {
        if (getInstance().getCooldownTime().containsKey(player))
                return true;
            else
                return false;
    }

    public void setPlayerBoost(Player player, int time) {
        cooldownTime.put(player, time);
        cooldownTask.put(player, new BukkitRunnable() {
            public void run() {
                cooldownTime.put(player, cooldownTime.get(player) - 1);
                if (cooldownTime.get(player) == 0) {
                    cooldownTime.remove(player);
                    cooldownTask.remove(player);

                    cancel();
                }
            }
        });
        cooldownTask.get(player).runTaskTimer(SellChest.getInstance(), 20, 20);
    }

    public Integer getTimeRemaining(Player player) {
        int a = 0;
        if (getInstance().getCooldownTime().containsKey(player)) {
            a = cooldownTime.get(player);
        }
        return a;
    }

}