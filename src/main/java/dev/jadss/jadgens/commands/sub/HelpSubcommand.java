package dev.jadss.jadgens.commands.sub;

import dev.jadss.jadgens.api.MachinesAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpSubcommand {

    public HelpSubcommand(CommandSender sender) {
        String[] messages = MachinesAPI.getInstance().getGeneralConfiguration().getMessages().helpCommand.message;
        for (String message : messages)
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
