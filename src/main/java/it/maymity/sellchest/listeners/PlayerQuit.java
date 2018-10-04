package it.maymity.sellchest.listeners;

import it.maymity.sellchest.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        int time = Utils.getInstance().getTimeRemaining(p);

        if (Utils.getInstance().getCooldownTime().containsKey(p)) {
            if (time != 0 && time > 0) {
                Utils.getInstance().getTime().put(p.getName(), Utils.getInstance().getTimeRemaining(p));
                Utils.getInstance().getCooldownTask().get(p).cancel();
                Utils.getInstance().getCooldownTime().remove(p);
            }
        }
    }
}