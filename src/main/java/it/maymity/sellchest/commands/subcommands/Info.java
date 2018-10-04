package it.maymity.sellchest.commands.subcommands;

import it.maymity.sellchest.SellChest;
import it.maymity.sellchest.Utils;
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
        if(!Utils.getInstance().hasBoost(player)){
            plugin.getMessages().getMessage("messages.no_boost").sendMessage(player);
            return;
        }
        int timeRemaining = Utils.getInstance().getTimeRemaining(player);
        
        plugin.getMessages().getMessage("messages.time_remaining").setVariable("time", String.valueOf(timeRemaining));
    }
}
