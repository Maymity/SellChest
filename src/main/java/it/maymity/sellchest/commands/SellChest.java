package it.maymity.sellchest.commands;

import it.maymity.sellchest.Main;
import it.maymity.sellchest.Utils;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import it.maymity.sellchest.managers.MessagesManager;

public class SellChest implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            MessagesManager.getInstance().sendMessage(sender, Utils.getInstance().getMessages().getString("messages.only_player"));
            return false;
        }

        Player player = (Player) sender;
        int cas = Utils.getInstance().getTimeRemaining(player);

        if (!(args.length > 0)) {
            Utils.getInstance().gethelp(player);
            return false;
        }

        switch (args[0]) {
            case "boost":
                if (Utils.getInstance().hasBoost(player))
                    MessagesManager.getInstance().sendMessage(player, Utils.getInstance().getMessages().getString("messages.already_boost"));
                else
                    player.openInventory(Utils.getInstance().getBoostInventory());
                break;
            case "info":
                if (Utils.getInstance().hasBoost(player)) {
                    if ((cas != 0) && (cas > 0))
                        MessagesManager.getInstance().sendMessage(player, Utils.getInstance().getMessages().getString("messages.time_remaining").replaceAll("%time%", Long.toString(cas)));
                } else
                    MessagesManager.getInstance().sendMessage(player, Utils.getInstance().getMessages().getString("messages.no_boost"));
                break;
            case "reload":
                if (player.hasPermission("sellchest.reload")) {
                    Main.getInstance().reloadConfig();
                    Main.getInstance().getMessages().reload();
                    Main.getInstance().getItems().clear();
                    Main.getInstance().loadItems();
                    MessagesManager.getInstance().sendMessage(player, Utils.getInstance().getMessages().getString("messages.reload"));
                } else
                    MessagesManager.getInstance().sendMessage(player, Utils.getInstance().getMessages().getString("messages.no_permission"));
                break;
        }
        return true;
    }
}