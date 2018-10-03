package it.maymity.sellchest.managers;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import it.maymity.sellchest.Utils;

public class MessagesManager
{
    private static MessagesManager instance;

    public static synchronized MessagesManager getInstance()
    {
        if (instance == null) {
            instance = new MessagesManager();
        }
        return instance;
    }

    String prefix = Utils.getInstance().getConfig().getString("settings.prefix");

    public void sendMessage(Player p, String s)
    {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + s));
    }

    public void sendMessage(CommandSender p, String s)
    {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + s));
    }

}
