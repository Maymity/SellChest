package it.maymity.sellchest;

import it.maymity.sellchest.managers.MessagesManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.HashMap;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.ArrayList;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

public class Utils {

    private static Utils instance;

    private HashMap<Player, Integer> cooldownTime = new HashMap<Player, Integer>();
    private HashMap<Player, BukkitRunnable> cooldownTask = new HashMap<Player, BukkitRunnable>();
    private HashMap<String, Integer> timeRemaining = new HashMap<String, Integer>();

    private FileConfiguration config;
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
    public FileConfiguration getMessages() { return Main.getInstance().getMessages().getConfig(); }

    public FileConfiguration getConfig() {
        config = Bukkit.getServer().getPluginManager().getPlugin("SellChest").getConfig();
        return config;
    }

    public List<String> getSignmessage() {
        signmessage = Bukkit.getPluginManager().getPlugin("SellChest").getConfig().getStringList("signmessage");
        return signmessage;
    }

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
    }

    public void gethelp(Player player) {
        for (String c : getMessages().getStringList("messages.help-commands"))
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', c));
    }

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

    public void processChest(ItemStack[] chestinv, Player p, Inventory attached) {
        double prezzo = 0;
        double boost = Utils.getInstance().getConfig().getDouble("game_sell_booster.boost%");

        for (ItemStack item : chestinv) {
            if (item != null) {
                if (item.getType() != Material.AIR) {
                    for (int i = 0; i < Main.getInstance().getItems().size(); i++) {
                        if (item.getType() == Main.getInstance().getItems().get(i).getMaterial() && item.getDurability() == Main.getInstance().getItems().get(i).getDamage()) {
                            double valore = Main.getInstance().getItems().get(i).getPrize();
                            prezzo += valore * item.getAmount();
                            if (Utils.getInstance().hasBoost(p))
                                prezzo = prezzo * boost;
                            Main.getInstance().getEconomy().depositPlayer(p, prezzo);
                            attached.removeItem(item, new ItemStack(Material.AIR));
                        }
                    }
                }
            }
        }
        if (prezzo == 0)
            MessagesManager.getInstance().sendMessage(p, Utils.getInstance().getMessages().getString("messages.no_item"));
        else
            MessagesManager.getInstance().sendMessage(p, Utils.getInstance().getMessages().getString("messages.sell_message").replaceAll("%money%", Double.toString(prezzo)));
    }

    public Boolean hasBoost(Player player)
    {
        if (getInstance().getCooldownTime().containsKey(player))
                return true;
            else
                return false;
    }

    public void setPlayerBoost(Player player, int time)
    {
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
        cooldownTask.get(player).runTaskTimer(Main.getInstance(), 20, 20);
    }

    public Integer getTimeRemaining(Player player) {
        int a = 0;
        if (getInstance().getCooldownTime().containsKey(player)) {
            a = cooldownTime.get(player);
        }
        return a;
    }

}