package ml.jadss.jadgens.commands.subcommands;

import ml.jadss.jadgens.JadGens;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class DebugCommand {

    public DebugCommand(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eDebug info:"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m----------------------------"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eInfo about &b&lRemoval Queue&e."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &3Blocks &ain queue&e: &b&l" + JadGens.getInstance().getBlocksRemover().getBlocks().size()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &3Speed&e: &b&l" + JadGens.getInstance().getBlocksRemover().getSpeed()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m----------------------------"));
        sender.sendMessage("End.");
        JadGens.getInstance().getBlocksRemover().updateStatus(null, true);
    }
}
