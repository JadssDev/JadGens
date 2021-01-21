package ml.jadss.jadgens.commands.subcommands;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.JadGensAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class DebugCommand {

    public DebugCommand(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eDebug info:"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m----------------------------"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eInfo about &b&lRemoval Queue&e."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Blocks &ain queue&e: &b&l" + JadGens.getInstance().getBlocksRemover().getBlocks().size()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Speed&e: &b&l" + JadGens.getInstance().getBlocksRemover().getSpeed()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m----------------------------"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eInfo about &b&lJadGensAPI&e."));
        List<String> apis = JadGensAPI.getAPIsList();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eCreated " + apis.size()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eAPI's plugins created:"));
        for(String name : apis) { sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3" + name));}
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m----------------------------"));
        sender.sendMessage("End.");
        JadGens.getInstance().getBlocksRemover().updateStatus(null, true);
    }
}
