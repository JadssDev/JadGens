package ml.jadss.jadgens.commands.subcommands;

import ml.jadss.jadgens.JadGens;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class HelpCommand {

    public HelpCommand(CommandSender sender) {
        for (String s : lang().getStringList("messages.helpMessages")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        }
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
