package it.maymity.sellchest.commands.subcommands;

import it.maymity.sellchest.SellChest;
import it.maymity.sellchest.Utils;
import it.xquickglare.qlib.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class Boost extends SubCommand {
    
    public Boost() {
        super("boost", "sellchest.commands.boost", Collections.emptyList(), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;

        if (Utils.getInstance().hasBoost(player))
            SellChest.getInstance().getMessages().getMessage("messages.already_boost").sendMessage(player);
        //else
            //TODO player.openInventory(Utils.getInstance().getBoostInventory());
    }
}
