package it.maymity.sellchest.items;

import it.maymity.sellchest.SellChest;
import it.xquickglare.qlib.configuration.Configuration;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    
    private final SellChest plugin = SellChest.getInstance();
    
    private List<Item> items;
    
    public ItemManager(){
        items = new ArrayList<>();
        
        reloadItems();
    }
    
    public void reloadItems(){
        items.clear();
        List<String> itemsString = plugin.getConfiguration().getStringList("itemsold");

        for (String key : itemsString) {
            String[] info = key.split(":");

            Material mat = Material.valueOf(info[0]);
            double prezzo = Double.parseDouble(info[1]);
            short data = 0;
            
            if(info.length == 3){
                data = Short.parseShort(info[1]);
                prezzo = Double.parseDouble(info[2]);
            }
            
            items.add(new Item(
                    mat,
                    data,
                    prezzo
            ));
            
            plugin.getLogs().infoLog("Loaded " + key + " item.", false);
        }
        
        plugin.getLogs().infoLog("Loaded " + items.size() + " items!", true);
    }
    
    public double getItemsPrice(Inventory inventory){
        ItemStack[] conts = inventory.getContents();
        double price = 0;

        for (ItemStack item : conts) {
            if(item == null)
                continue;

            Item sellable = getSellable(item);
            if(sellable == null)
                continue;
            
            price += sellable.getPrice();
            inventory.removeItem(item);
        }
        
        return price;
    }
    
    private Item getSellable(ItemStack item){
        for (Item sellable : items) {
            if(sellable.isItem(item))
                return sellable;
        }
        
        return null;
    }
}
