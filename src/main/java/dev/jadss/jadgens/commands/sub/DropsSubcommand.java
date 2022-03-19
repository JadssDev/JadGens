package dev.jadss.jadgens.commands.sub;

import dev.jadss.jadgens.api.MachinesAPI;
import dev.jadss.jadgens.api.config.generalConfig.messages.DropsMessageConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.messages.MessagesConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DropsSubcommand {

    public DropsSubcommand(CommandSender sender) {
        MessagesConfiguration globalMessages = MachinesAPI.getInstance().getGeneralConfiguration().getMessages();
        if (sender instanceof Player) {
            DropsMessageConfiguration dropsCommand = globalMessages.dropsMessages;
            MachinesAPI.getInstance().getMenuManager().openDropsMenu((Player) sender, null, () ->
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', dropsCommand.menuOpened)), null);
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', globalMessages.globalMessages.notAPlayer));
        }
    }

}
