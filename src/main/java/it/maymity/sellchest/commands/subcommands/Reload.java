package it.maymity.sellchest.commands.subcommands;

import it.maymity.sellchest.SellChest;
import it.xquickglare.qlib.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class Reload extends SubCommand {
    
    private final SellChest plugin = SellChest.getInstance();
    
    public Reload() {
        super("reload", "sellchest.commands.reload", Collections.emptyList(), false);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.getConfiguration().reload();
        plugin.getMessages().reload();
        plugin.getItemManager().reloadItems();
        
        plugin.getMessages().getMessage("messages.reload").sendMessage(sender);
    }
}
