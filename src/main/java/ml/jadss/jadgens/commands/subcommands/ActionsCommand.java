package ml.jadss.jadgens.commands.subcommands;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.utils.MachineAction;
import ml.jadss.jadgens.utils.MachineActions;
import ml.jadss.jadgens.utils.MachineLookup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ActionsCommand {

    public ActionsCommand(CommandSender sender, String[] args) { //done to other player: messages.actionsMessages.managedPlayer
        // /jadgens actions <player/all/me> <action>
        if (sender instanceof ConsoleCommandSender || sender.hasPermission(lang().getString("messages.actionsMessages.permission"))) {

            if (args.length == 3) {

                MachineAction action;
                try { action = MachineAction.valueOf(args[2].toUpperCase()); } catch(IllegalArgumentException ignored) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.actionsMessages.actionNotFound")));
                    return;
                }

                if (args[1].equalsIgnoreCase(lang().getString("messages.actionsMessages.selectableArgumentMe"))) {
                    if (sender instanceof ConsoleCommandSender) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.notPLayer")));
                        return;
                    }

                    if (action == MachineAction.ENABLE) {
                        if (sender.hasPermission(lang().getString("messages.actionsMessages.enableOwnMachinesPermission"))) {
                            MachineActions acts = new MachineActions();
                            acts.toMachinesFrom(action, ((Player) sender).getUniqueId(), false);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.actionsMessages.enabledOwnMachines")));
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.noPermission")));
                        }
                    } else if (action == MachineAction.DISABLE) {
                        if (sender.hasPermission(lang().getString("messages.actionsMessages.disableOwnMachinesPermission"))) {
                            MachineActions acts = new MachineActions();
                            acts.toMachinesFrom(action, ((Player) sender).getUniqueId(), false);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.actionsMessages.disabledOwnMachines")));
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.noPermission")));
                        }
                    } else if (action == MachineAction.PURGE) {
                        if (sender.hasPermission(lang().getString("messages.actionsMessages.purgeOwnMachinesPermission"))) {
                            MachineActions acts = new MachineActions();
                            acts.toMachinesFrom(action, ((Player) sender).getUniqueId(), true);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.actionsMessages.purgePutInQueue")));
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.noPermission")));
                        }
                    }
                } else if (args[1].equalsIgnoreCase(lang().getString("messages.actionsMessages.selectableArgumentAll"))) {
                    if (sender.hasPermission(lang().getString("messages.actionsMessages.permissionManageAll"))) {
                        if (action == MachineAction.ENABLE) {
                            MachineActions acts = new MachineActions();
                            acts.toAllMachines(action);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.actionsMessages.managedAllPlayers").replace("%count%", String.valueOf(new MachineLookup().getAllMachines().size()))));
                        } else if (action == MachineAction.DISABLE) {
                            MachineActions acts = new MachineActions();
                            acts.toAllMachines(action);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.actionsMessages.managedAllPlayers").replace("%count%", String.valueOf(new MachineLookup().getAllMachines().size()))));
                        } else if (action == MachineAction.PURGE) {
                            MachineActions acts = new MachineActions();
                            acts.toAllMachines(action);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.actionsMessages.managedAllPlayers").replace("%count%", String.valueOf(new MachineLookup().getAllMachines().size()))));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.noPermission")));
                    }
                } else { //messages.actionsMessages.managedPlayer
                    if (sender.hasPermission(lang().getString("messages.actionsMessages.permissionManageOthers"))) {
                        OfflinePlayer player = getPlayerOffline(args[1]);
                        if (player == null || !player.hasPlayedBefore()) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.playerNotFound")));
                            return;
                        }

                        if (action == MachineAction.ENABLE) {
                            MachineActions acts = new MachineActions();
                            acts.toMachinesFrom(action, player.getUniqueId(), false);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.actionsMessages.managedPlayer").replace("%count%", String.valueOf(acts.getCount())).replace("%player%", player.getName())));

                        } else if (action == MachineAction.DISABLE) {
                            MachineActions acts = new MachineActions();
                            acts.toMachinesFrom(action, player.getUniqueId(), false);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.actionsMessages.managedPlayer").replace("%count%", String.valueOf(acts.getCount())).replace("%player%", player.getName())));

                        } else if (action == MachineAction.PURGE) {
                            MachineActions acts = new MachineActions();
                            acts.toMachinesFrom(action, player.getUniqueId(), false);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.actionsMessages.managedPlayer").replace("%count%", String.valueOf(acts.getCount())).replace("%player%", player.getName())));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.noPermission")));
                    }
                }
            } else {
                new HelpCommand(sender);
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.noPermission")));
        }
    }

    private OfflinePlayer getPlayerOffline(String nick) {
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if (player.getName().equalsIgnoreCase(nick)) return player;
        }
        return null;
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
