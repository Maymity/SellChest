package it.maymity.sellchest.managers;

import org.bukkit.Material;

public class ItemsManager {

    private Material material;
    private short damage;
    private double prize;

    public ItemsManager(Material material, short damage, double prize) {
        this.material = material;
        this.damage = damage;
        this.prize = prize;
    }

    public ItemsManager(Material material, short damage) {
        this.material = material;
        this.damage = damage;
    }

    public Material getMaterial() { return material; }

    public short getDamage() {
        return damage;
    }

    public double getPrize() {
        return prize;
    }
}
