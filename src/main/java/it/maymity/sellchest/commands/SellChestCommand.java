package it.maymity.sellchest.commands;

import it.maymity.sellchest.SellChest;
import it.maymity.sellchest.commands.subcommands.Boost;
import it.maymity.sellchest.commands.subcommands.Info;
import it.maymity.sellchest.commands.subcommands.Reload;
import it.xquickglare.qlib.commands.AdvancedCommand;

public class SellChestCommand extends AdvancedCommand {
    
    public SellChestCommand() {
        super(SellChest.getInstance().getMessages().getMultilineMessage("messages.help-commands"));
        
        addSubCommand(
                new Boost(),
                new Info(),
                new Reload()
        );
    }
}