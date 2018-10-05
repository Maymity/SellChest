package it.maymity.sellchest.commands.subcommands;

import it.maymity.sellchest.SellChest;
import it.xquickglare.qlib.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class Info extends SubCommand {
    
    private final SellChest plugin = SellChest.getInstance();
    
    public Info() {
        super("info", "sellchest.commands.info", Collections.emptyList(), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        if(!plugin.getBoostManager().hasBoost(player)){
            plugin.getMessages().getMessage("messages.no_boost").sendMessage(player);
            return;
        }

        long time = plugin.getBoostManager().getSecondsRemaining(player);
        
        plugin.getMessages().getMessage("messages.time_remaining").setVariable("time", String.valueOf(time));
    }
}
