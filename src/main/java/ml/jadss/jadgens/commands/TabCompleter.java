package ml.jadss.jadgens.commands;

import ml.jadss.jadgens.JadGens;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        System.out.println(Arrays.toString(args));
        System.out.println(command.getName());
        System.out.println(args.length);
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
            return tab;
        } else if (sender.hasPermission(lang().getString("messages.giveMessages.permission")) && args[0].equalsIgnoreCase("give")) {
            if (args.length == 2) {
                tab.add("machines");
                tab.add("machine");
                tab.add("fuels");
                tab.add("fuel");
                return tab;
            } else if (args.length == 3) {
                List<String> list = new ArrayList<>();
                if (args[1].equalsIgnoreCase("machines") || args[1].equalsIgnoreCase("machine") || args[1].equalsIgnoreCase("m")) {
                    list.addAll(JadGens.getInstance().getConfig().getConfigurationSection("machines").getKeys(false));
                } else if (args[1].equalsIgnoreCase("fuels") || args[1].equalsIgnoreCase("fuel") || args[1].equalsIgnoreCase("f")) {
                    list.addAll(JadGens.getInstance().getConfig().getConfigurationSection("fuels").getKeys(false));
                }
                return list;
            } else if (args.length == 4) {
                List<String> list = new ArrayList<>();
                for (Player p : Bukkit.getOnlinePlayers()) list.add(p.getName());
                return list;
            }
        }
        return null;
    }
    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
