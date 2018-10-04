package it.maymity.sellchest.newmanagers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor @Getter
public class Item {
    private Material material;
    private short damage;
    private double prize;
    
    public boolean isItem(ItemStack item){
        return item.getType() == material && item.getDurability() == damage;
    }
}
