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
import org.bukkit.inventory.ItemStack;

public class GiveCommand { // /jadgens give <type> <id> <player> <amount> [-s]

    Machine machineChecker = new Machine();
    Fuel fuelChecker = new Fuel();


    public GiveCommand(CommandSender sender, String[] args) {
        if (sender.hasPermission(lang().getString("messages.giveMessages.permission")) || sender instanceof ConsoleCommandSender) {
            if (args.length == 4 || args.length == 5 || args.length == 6) {
                if (args[1].equalsIgnoreCase("fuels") || args[1].equalsIgnoreCase("fuel") || args[1].equalsIgnoreCase("f")) {
                    if (fuelChecker.typeExists(args[2])) {
                        Player target = Bukkit.getPlayer(args[3]);
                        if (target == null) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.playerNotFound")));
                        } else {
                            ItemStack fuel = new Fuel().createItem(Integer.parseInt(args[2]));
                            if (args.length == 4) {
                                target.getInventory().addItem(fuel);
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.giveMessages.givenFuel")));
                            } else if (args.length == 5) {

                                int amount = 1;
                                try {
                                    amount = Integer.parseInt(args[4]);
                                } catch(NumberFormatException ignored) { }
                                if (amount > 64) amount = 64;

                                fuel.setAmount(amount);

                                target.getInventory().addItem(fuel);
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.giveMessages.givenFuel")));
                            } else if (args.length == 6) {

                                int amount = 1;
                                try {
                                    amount = Integer.parseInt(args[4]);
                                } catch(NumberFormatException ignored) { }
                                if (amount > 64) amount = 64;

                                fuel.setAmount(amount);

                                if (args[5].toLowerCase().contains("silent")) {
                                    target.getInventory().addItem(fuel);
                                    return;
                                }
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.giveMessages.givenFuel")));
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.giveMessages.idNotFound")));
                    }
                } else if (args[1].equalsIgnoreCase("machines") || args[1].equalsIgnoreCase("machine") || args[1].equalsIgnoreCase("m")) {
                    if (machineChecker.typeExists(args[2])) {
                        Player target = Bukkit.getPlayer(args[3]);
                        if (target == null) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.playerNotFound")));
                        } else {
                            ItemStack machine = new Machine().createItem(Integer.parseInt(args[2]));
                            if (args.length == 4) {
                                target.getInventory().addItem(machine);
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.giveMessages.givenMachine")));
                            } else if (args.length == 5) {

                                int amount = 1;
                                try {
                                    amount = Integer.parseInt(args[4]);
                                } catch(NumberFormatException ignored) { }
                                if (amount > 64) amount = 64;

                                machine.setAmount(amount);

                                target.getInventory().addItem(machine);
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.giveMessages.givenMachine")));
                            } else if (args.length == 6) {

                                int amount = 1;
                                try {
                                    amount = Integer.parseInt(args[4]);
                                } catch(NumberFormatException ignored) { }
                                if (amount > 64) amount = 64;

                                machine.setAmount(amount);

                                if (args[5].toLowerCase().contains("silent")) {
                                    target.getInventory().addItem(machine);
                                    return;
                                }
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
