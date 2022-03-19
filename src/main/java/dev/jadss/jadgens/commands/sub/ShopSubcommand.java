package dev.jadss.jadgens.commands.sub;

import dev.jadss.jadgens.api.MachinesAPI;
import dev.jadss.jadgens.api.MenusManager;
import dev.jadss.jadgens.api.config.generalConfig.messages.commands.ShopCommandMessagesConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopSubcommand {

    public ShopSubcommand(CommandSender sender, String[] args) {
        ShopCommandMessagesConfiguration messages = MachinesAPI.getInstance().getGeneralConfiguration().getMessages().shopCommand;

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().globalMessages.notAPlayer));
            return;
        }
        if (!sender.hasPermission(MachinesAPI.getInstance().getGeneralConfiguration().getPermissions().shopCommandPermission)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().globalMessages.noPermission));
            return;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            MachinesAPI.getInstance().getMenuManager().openShopMenu(MenusManager.ShopType.MAIN, player,
                    null,
                    () -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.openedShop)),
                    () -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.closedShop)));
        } else if (args.length == 1) {
            MenusManager.ShopType shopType = null;

            boolean isMachine = false;
            boolean isFuel = false;

            for (String alias : messages.machineAliases)
                if (args[0].equalsIgnoreCase(alias))
                    isMachine = true;

            for (String alias : messages.fuelAliases)
                if (args[0].equalsIgnoreCase(alias))
                    isFuel = true;

            if (isMachine && isFuel)
                throw new RuntimeException("Invalid shop type!");

            if (isMachine)
                shopType = MenusManager.ShopType.MACHINES;

            if (isFuel)
                shopType = MenusManager.ShopType.FUELS;

            if (shopType == null)
                shopType = MenusManager.ShopType.MAIN;

            MachinesAPI.getInstance().getMenuManager().openShopMenu(shopType, player,
                    null,
                    () -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.openedShop)),
                    () -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.closedShop)));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.wrongSyntax));
        }
    }
}
