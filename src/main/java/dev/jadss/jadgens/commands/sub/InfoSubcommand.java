package dev.jadss.jadgens.commands.sub;

import dev.jadss.jadgens.api.MachinesAPI;
import dev.jadss.jadgens.api.config.generalConfig.limits.MachineLimits;
import dev.jadss.jadgens.api.config.generalConfig.messages.commands.InfoCommandMessagesConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static dev.jadss.jadgens.utils.Utilities.replace;

public class InfoSubcommand {

    public InfoSubcommand(CommandSender sender) {
        InfoCommandMessagesConfiguration messages = MachinesAPI.getInstance().getGeneralConfiguration().getMessages().infoCommand;

        if (sender instanceof Player) {
            Player player = (Player) sender;
            String[] message = messages.infoMessage;
            message = replace(message, "%amount%", "" + MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getMachines().getMachines().size());

            String maxString = (MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getMaximumMachines() == -1 ? messages.infinite : "" + MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getMaximumMachines());
            message = replace(message, "%max%", maxString);


            message = replace(message, "%remaining%", (MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getMaximumMachines() == -1 ? messages.infinite :
                    "" + (MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getMaximumMachines() - MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getMachines().getMachines().size())));

            MachineLimits limits = MachinesAPI.getInstance().getGeneralConfiguration().getPermissions().machineLimitsGroups;
            String group = MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getLimitGroup() == null ? limits.defaultLimitGroupName : MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getLimitGroup().limitGroupName;

            message = replace(message, "%group%", group);

            for (String line : message)
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().globalMessages.notAPlayer));
        }
    }
}
