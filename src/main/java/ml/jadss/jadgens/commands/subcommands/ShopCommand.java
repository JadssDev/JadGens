package ml.jadss.jadgens.commands.subcommands;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.utils.Shop;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ShopCommand {

    public ShopCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player pl = (Player) sender;
            if (args.length == 1 && JadGens.getInstance().getConfig().getInt("shop.shopType") == 2) {
                pl.openInventory(new Shop().getShopInventory("MainShop"));
            } else if (args.length == 2) {
                if (args[1].equalsIgnoreCase("machines") || args[1].equalsIgnoreCase("m") || args[1].equalsIgnoreCase("machine")) {
                    pl.openInventory(new Shop().getShopInventory("machines"));
                } else if (args[1].equalsIgnoreCase("fuels") || args[1].equalsIgnoreCase("f") || args[1].equalsIgnoreCase("fuel")) {
                    pl.openInventory(new Shop().getShopInventory("fuels"));
                } else {
                    pl.openInventory(new Shop().getShopInventory("MainShop"));
                }
            } else {
                new HelpCommand(sender);
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.notPLayer")));
        }
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
