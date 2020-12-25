package ml.jadss.jadgens.commands;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.commands.subcommands.*;
import ml.jadss.jadgens.events.CustomJadGensCommandArg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.stream.Collectors;

public class JadGensCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args.length == 1 && args[0].equalsIgnoreCase("help")) {
            new HelpCommand(sender);
        } else if (args[0].equalsIgnoreCase("give")) {
            new GiveCommand(sender, args);
        } else if (args[0].equalsIgnoreCase("purge")) {
            new PurgeCommand(sender);
        } else if (args[0].equalsIgnoreCase("reload")) {
            new ReloadCommand(sender);
        } else if (JadGens.getInstance().getConfig().getBoolean("shop.enabled") && args[0].equalsIgnoreCase("shop")) {
            new ShopCommand(sender, args);
        } else if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("about")) {
            new VersionCommand(sender);
        } else if (args[0].equalsIgnoreCase("info")) {
            new InfoCommand(sender);
        } else if (args[0].equalsIgnoreCase("debug")) {
            new DebugCommand(sender);
        } else if (JadGens.getInstance().getLangFile().lang().getBoolean("messages.actionsMessages.enabled") && args[0].equalsIgnoreCase("actions")) {
            new ActionsCommand(sender, args);
        } else {
            try {
                CustomJadGensCommandArg event = new CustomJadGensCommandArg(sender, args[0], Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1).split(" "));
                JadGens.getInstance().getServer().getPluginManager().callEvent(event);
                if (event.isUsed()) return true;
                new HelpCommand(sender);
            } catch (StringIndexOutOfBoundsException ex) {
                CustomJadGensCommandArg event = new CustomJadGensCommandArg(sender, args[0], null);
                JadGens.getInstance().getServer().getPluginManager().callEvent(event);
                if (event.isUsed()) return true;
                new HelpCommand(sender);
            }
        }
        return true;
    }
}
