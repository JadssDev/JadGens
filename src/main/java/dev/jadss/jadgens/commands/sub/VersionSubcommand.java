package dev.jadss.jadgens.commands.sub;

import dev.jadss.jadapi.bukkitImpl.misc.JSender;
import dev.jadss.jadgens.JadGens;
import dev.jadss.jadgens.api.MachinesAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class VersionSubcommand {

    public VersionSubcommand(CommandSender bukkitSender) {
        JSender sender = new JSender(bukkitSender);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&3&lJadGens &7>> &eThis &bserver &eis &a&lrunning &3&lJadGens &bv" + JadGens.getInstance().getDescription().getVersion() + " &eby &3Jadss"));
        if (JadGens.getInstance().getHookByName("Vault").isAvailable()) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eHooked into &3Vault&e!"));
        if (JadGens.getInstance().getHookByName("placeholderapi").isAvailable()) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eHooked into &3PlaceHolderAPI&e!"));
        if (JadGens.getInstance().getHookByName("playerpoints").isAvailable()) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eHooked into &3PlayerPoints&e!"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&3&lJadGens &7>> &eThere are &3" + MachinesAPI.getInstance().getMachineConfigurations().size() + " &b&lMachines &ecurrently &aloaded&e!"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&3&lJadGens &7>> &eThere are &3" + MachinesAPI.getInstance().getMachineConfigurations().size() + " &b&lFuels &ecurrently &aloaded&e!"));
    }
}
