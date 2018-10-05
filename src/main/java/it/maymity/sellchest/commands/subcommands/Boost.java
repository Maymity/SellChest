package it.maymity.sellchest.commands.subcommands;

import it.maymity.sellchest.SellChest;
import it.xquickglare.qlib.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class Boost extends SubCommand {

    private final SellChest plugin = SellChest.getInstance();

    public Boost() {
        super("boost", "sellchest.commands.boost", Collections.emptyList(), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;

        if (plugin.getBoostManager().hasBoost(player))
            SellChest.getInstance().getMessages().getMessage("messages.already_boost").sendMessage(player);
        else
            plugin.getBoostManager().getBoostMenu().openInventory(player);
    }
}
