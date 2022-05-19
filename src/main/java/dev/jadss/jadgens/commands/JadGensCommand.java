package dev.jadss.jadgens.commands;

import dev.jadss.jadgens.api.MachinesAPI;
import dev.jadss.jadgens.api.config.generalConfig.Permissions;
import dev.jadss.jadgens.api.config.interfaces.LoadedFuelConfiguration;
import dev.jadss.jadgens.api.config.interfaces.LoadedMachineConfiguration;
import dev.jadss.jadgens.commands.sub.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JadGensCommand implements CommandExecutor, TabCompleter {

    private final MachinesAPI api = MachinesAPI.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args.length == 1 && args[0].equalsIgnoreCase("help")) {
            new HelpSubcommand(sender);
        } else if (args[0].equalsIgnoreCase("shop")) {
            new ShopSubcommand(sender, remove(args, 1));
        } else if (args[0].equalsIgnoreCase("give")) {
            new GiveSubcommand(sender, remove(args, 1));
        } else if (args[0].equalsIgnoreCase("action") || args[0].equalsIgnoreCase("actions")) {
            new ActionsSubcommand(sender, remove(args, 1));
        } else if (args[0].equalsIgnoreCase("info")) {
            new InfoSubcommand(sender); //todo: maybe add a player argument, so we can query info on other players?
        } else if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("about")) {
            new VersionSubcommand(sender);
        } else if (args[0].equalsIgnoreCase("drops") || args[0].equalsIgnoreCase("drop")) {
            new DropsSubcommand(sender);
        } else {
            new HelpSubcommand(sender);
        }

        return true;
    }

    public static String[] remove(String[] args, int skipAmount) {
        return Arrays.stream(args).skip(skipAmount).toArray(String[]::new);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Permissions permissions = api.getGeneralConfiguration().getPermissions();
        List<String> tabCompleter = new ArrayList<>();
        if (args.length == 1) {
            tabCompleter.add("help");
            tabCompleter.add("info");
            tabCompleter.add("drops");
            tabCompleter.add("version");

            if (has(sender, permissions.actionsCommandPermission))
                tabCompleter.add("actions");
            if (has(sender, permissions.giveCommandPermission))
                tabCompleter.add("give");
            if (has(sender, permissions.shopCommandPermission))
                tabCompleter.add("shop");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("actions") && has(sender, permissions.actionsCommandPermission)) {
                if (has(sender, permissions.actionsCommandEnablePermission))
                    tabCompleter.add(MachinesAPI.getInstance().getGeneralConfiguration().getMessages().actionsCommand.enableActionAliases[0]);
                if (has(sender, permissions.actionsCommandDisablePermission))
                    tabCompleter.add(MachinesAPI.getInstance().getGeneralConfiguration().getMessages().actionsCommand.disableActionAliases[0]);
                if (has(sender, permissions.actionsCommandPurgePermission))
                    tabCompleter.add(MachinesAPI.getInstance().getGeneralConfiguration().getMessages().actionsCommand.purgeActionAliases[0]);

                if (has(sender, permissions.actionsCommandManageOthersPermission))
                    tabCompleter.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toCollection(ArrayList::new)));
            } else if (args[0].equalsIgnoreCase("give")) {
                tabCompleter.add(MachinesAPI.getInstance().getGeneralConfiguration().getMessages().giveCommand.machineAliases[0]);
                tabCompleter.add(MachinesAPI.getInstance().getGeneralConfiguration().getMessages().giveCommand.fuelAliases[0]);
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("give") && has(sender, permissions.giveCommandPermission)) {
                if (check(args[1], Arrays.stream(MachinesAPI.getInstance().getGeneralConfiguration().getMessages().giveCommand.machineAliases).collect(Collectors.toList()))) {
                    tabCompleter.addAll(MachinesAPI.getInstance().getMachineConfigurations().stream().map(LoadedMachineConfiguration::getConfigurationName).collect(Collectors.toCollection(ArrayList::new)));
                } else if (check(args[1], Arrays.stream(MachinesAPI.getInstance().getGeneralConfiguration().getMessages().giveCommand.fuelAliases).collect(Collectors.toList()))) {
                    tabCompleter.addAll(MachinesAPI.getInstance().getFuelConfigurations().stream().map(LoadedFuelConfiguration::getConfigurationName).collect(Collectors.toCollection(ArrayList::new)));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eCannot &3auto-complete &ethere is no &bsuch type&e!"));
                }
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("give") && has(sender, permissions.giveCommandPermission)) {
                tabCompleter.add("1");
                tabCompleter.add("2");
                tabCompleter.add("4");
                tabCompleter.add("8");
                tabCompleter.add("16");
                tabCompleter.add("32");
                tabCompleter.add("64");
                tabCompleter.add("128");
                tabCompleter.add("256");
            }
        } else if (args.length == 5) {
            if (args[0].equalsIgnoreCase("give") && has(sender, permissions.giveCommandPermission)) {
                tabCompleter.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toCollection(ArrayList::new)));
            }
        }
        return tabCompleter;
    }

    private boolean check(String line, List<String> list) {
        for (String listLine : list)
            if (listLine.equalsIgnoreCase(line))
                return true;
        ;
        return false;
    }

    private boolean has(CommandSender sender, String permission) {
        if (sender instanceof ConsoleCommandSender)
            return true;
        if (sender.hasPermission(permission))
            return true;
        else
            return false;
    }
}
