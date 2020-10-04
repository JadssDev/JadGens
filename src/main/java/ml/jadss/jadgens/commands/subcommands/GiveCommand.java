package ml.jadss.jadgens.commands.subcommands;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.utils.Fuel;
import ml.jadss.jadgens.utils.Machine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class GiveCommand { // /jadgens give <type> <id> <player>

    public GiveCommand(CommandSender sender, String[] args) {
        if (sender.hasPermission(lang().getString("messages.giveMessages.permission")) || sender instanceof ConsoleCommandSender) {
            if (args.length == 4 || args.length == 5) {
                if (args[1].equalsIgnoreCase("fuels") || args[1].equalsIgnoreCase("fuel") || args[1].equalsIgnoreCase("f")) {
                    if (JadGens.getInstance().getConfig().isConfigurationSection("fuels." + args[2])) {
                        Player target = Bukkit.getPlayer(args[3]);
                        if (target == null) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.playerNotFound")));
                        } else {
                            target.getInventory().addItem(new Fuel().createItem(Integer.parseInt(args[2])));
                            if (!(args.length == 5 && args[4].equalsIgnoreCase("-s"))) {
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.giveMessages.givenFuel")));
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.giveMessages.idNotFound")));
                    }
                } else if (args[1].equalsIgnoreCase("machines") || args[1].equalsIgnoreCase("machine") || args[1].equalsIgnoreCase("m")) {
                    if (JadGens.getInstance().getConfig().isConfigurationSection("machines." + args[2])) {
                        Player target = Bukkit.getPlayer(args[3]);
                        if (target == null) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.playerNotFound")));
                        } else {
                            target.getInventory().addItem(new Machine().createItem(Integer.parseInt(args[2])));
                            if (!(args.length == 5 && args[4].equalsIgnoreCase("-s"))) {
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.giveMessages.givenMachine")));
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.giveMessages.idNotFound")));
                    }
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.giveMessages.usage")));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.noPermission")));
        }
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
