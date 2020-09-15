package ml.jadss.jadgens.commands.subcommands;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.utils.PurgeMachines;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PurgeCommand {

    public PurgeCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player pl = (Player) sender;
            pl.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.onlyConsole")));
        } else if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.purgeMessages.purgingMachines")));
            new PurgeMachines().purgeMachines();
        }
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
