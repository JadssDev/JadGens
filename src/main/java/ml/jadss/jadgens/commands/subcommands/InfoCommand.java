package ml.jadss.jadgens.commands.subcommands;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.utils.MachineLimiter;
import ml.jadss.jadgens.utils.MachineLookup;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class InfoCommand {

    public InfoCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player pl = (Player) sender;
            MachineLimiter limiter = new MachineLimiter();
            MachineLookup lookup = new MachineLookup();
            for (String s : lang().getStringList("messages.infoMessages.msg")) {
                if (limiter.getMaxLimit(pl) == -1) {
                    s = s.replace("%max%", lang().getString("messages.infoMessages.infinite"));
                } else {
                    s = s.replace("%max%", String.valueOf(limiter.getMaxLimit(pl)));
                }
                s = s.replace("%has%", String.valueOf(lookup.getPlayerMachineCount(pl.getUniqueId())));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.notPLayer")));
        }
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
