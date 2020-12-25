package ml.jadss.jadgens.commands;

import ml.jadss.jadgens.JadGens;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            tab.add("help");
            if (sender instanceof ConsoleCommandSender || sender.hasPermission(lang().getString("messages.giveMessages.permission")))
                tab.add("give");
            if (sender instanceof ConsoleCommandSender) tab.add("purge");
            if (sender instanceof ConsoleCommandSender || sender.hasPermission(lang().getString("messages.reloadMessages.permission")))
                tab.add("reload");
            if (JadGens.getInstance().getConfig().getBoolean("shop.enabled")) tab.add("shop");
            tab.add("info");
            tab.add("version"); tab.add("about");
            if (lang().getBoolean("messages.actionsMessages.enabled") && sender instanceof ConsoleCommandSender || sender.hasPermission(lang().getString("messages.actionsMessages.permission"))) tab.add("actions");
            return tab;
        } else if (sender.hasPermission(lang().getString("messages.giveMessages.permission")) && args[0].equalsIgnoreCase("give")) {
            if (args.length == 2) {
                tab.add("machines");
                tab.add("machine");
                tab.add("fuels");
                tab.add("fuel");
                return tab;
            } else if (args.length == 3) {
                if (args[1].equalsIgnoreCase("machines") || args[1].equalsIgnoreCase("machine") || args[1].equalsIgnoreCase("m")) {
                    tab.addAll(JadGens.getInstance().getConfig().getConfigurationSection("machines").getKeys(false));
                } else if (args[1].equalsIgnoreCase("fuels") || args[1].equalsIgnoreCase("fuel") || args[1].equalsIgnoreCase("f")) {
                    tab.addAll(JadGens.getInstance().getConfig().getConfigurationSection("fuels").getKeys(false));
                }
                return tab;
            } else if (args.length == 4) {
                for (Player p : Bukkit.getOnlinePlayers()) tab.add(p.getName());
                return tab;
            }
        } else if (sender.hasPermission(lang().getString("messages.giveMessages.permission")) && args[0].equalsIgnoreCase("actions")) {
            if (args.length == 2) {
                if (sender.hasPermission(lang().getString("messages.actionsMessages.permission"))) tab.add(lang().getString("messages.actionsMessages.selectableArgumentMe"));
                if (sender.hasPermission(lang().getString("messages.actionsMessages.permissionManageAll"))) tab.add(lang().getString("messages.actionsMessages.selectableArgumentAll"));
                if (sender.hasPermission(lang().getString("messages.actionsMessages.permissionManageOthers"))) for(Player pl : Bukkit.getOnlinePlayers()) tab.add(pl.getName());
                return tab;
            } else if (args.length == 3) {
                if (args[1].equalsIgnoreCase(lang().getString("messages.actionsMessages.selectableArgumentAll"))) {
                    tab.add("enable");
                    tab.add("disable");
                    tab.add("purge");
                    return tab;
                } else if (args[1].equalsIgnoreCase(lang().getString("messages.actionsMessages.selectableArgumentMe"))){
                    if (sender.hasPermission(lang().getString("messages.actionsMessages.enableOwnMachinesPermission"))) tab.add("enable");
                    if (sender.hasPermission(lang().getString("messages.actionsMessages.disableOwnMachinesPermission"))) tab.add("disable");
                    if (sender.hasPermission(lang().getString("messages.actionsMessages.purgeOwnMachinesPermission"))) tab.add("purge");
                    return tab;
                } else if (sender.hasPermission(lang().getString("messages.actionsMessages.permissionManageOthers"))) {
                    tab.add("enable");
                    tab.add("disable");
                    tab.add("PuRgE");
                    return tab;
                }
            }
        }
        return null;
    }
    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
