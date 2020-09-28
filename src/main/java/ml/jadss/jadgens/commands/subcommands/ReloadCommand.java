package ml.jadss.jadgens.commands.subcommands;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.tasks.ProduceRunnable;
import ml.jadss.jadgens.utils.MachineLoader;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class ReloadCommand { //messages.reloadMessages.permission | pluginReloaded

    public ReloadCommand(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender || sender.hasPermission(lang().getString("messages.reloadMessages.permission"))) {
            new MachineLoader().unloadMachines();
            new MachineLoader().loadMachines();
            JadGens.getInstance().reloadConfig();
            JadGens.getInstance().getDataFile().reloadData();
            JadGens.getInstance().setupAPIDebug();
            JadGens.getInstance().getProducer().cancel();
            JadGens.getInstance().setProducer(new ProduceRunnable().runTaskTimer(JadGens.getInstance(), 0L, JadGens.getInstance().getConfig().getInt("machinesConfig.machinesDelay") * 20));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.reloadMessages.pluginReloaded")));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.noPermission")));
        }
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
