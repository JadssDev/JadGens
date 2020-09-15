package ml.jadss.jadgens.commands;

import ml.jadss.jadgens.JadGens;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new ArrayList<>();
        tab.add("help");
        if (sender instanceof ConsoleCommandSender || sender.hasPermission(lang().getString("messages.giveMessages.permission"))) tab.add("give");
        if (sender instanceof ConsoleCommandSender) tab.add("purge");
        if (sender instanceof ConsoleCommandSender || sender.hasPermission(lang().getString("messages.reloadMessages.permission"))) tab.add("reload");
        if (JadGens.getInstance().getConfig().getBoolean("shop.enabled")) tab.add("shop");
        tab.add("info");
        tab.add("version");
        return tab;
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
