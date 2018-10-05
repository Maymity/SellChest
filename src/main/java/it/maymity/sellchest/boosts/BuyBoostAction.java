package it.maymity.sellchest.boosts;

import it.maymity.sellchest.SellChest;
import it.xquickglare.qlib.actions.Action;
import org.bukkit.entity.Player;

public class BuyBoostAction extends Action {

    private final SellChest plugin = SellChest.getInstance();

    public BuyBoostAction() {
        super("BUY-BOOSTER");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if(plugin.getBoostManager().hasBoost(player)){
            plugin.getMessages().getMessage("messages.already_boost").sendMessage(player);
            return true;
        }

        double price = plugin.getConfiguration().getInt("game_sell_booster.price");
        if(!plugin.getEconomy().has(player, price)){
            plugin.getMessages().getMessage("messages.no_money").sendMessage(player);
            return true;
        }

        plugin.getEconomy().withdrawPlayer(player, price);
        plugin.getBoostManager().createBoost(player);

        plugin.getMessages().getMessage("messages.boost_message").setVariable("multiplier", String.valueOf(plugin.getBoostManager().getMultiplier())).sendMessage(player);
        return true;
    }
}
