package ml.jadss.jadgens.commands.subcommands;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.tasks.ProduceRunnable;
import ml.jadss.jadgens.utils.MachineLoader;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class ReloadCommand { //messages.reloadMessages.permission | pluginReloaded

    private boolean isDebugReloadEnabled = JadGens.getInstance().getLangFile().lang().getBoolean("messages.reloadMessages.reloadDebug");

    public ReloadCommand(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender || sender.hasPermission(lang().getString("messages.reloadMessages.permission"))) {

            isDebugReloadEnabled = JadGens.getInstance().getLangFile().lang().getBoolean("messages.reloadMessages.reloadDebug");

            if (isDebugReloadEnabled) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &b&lReloading &3Machines&e.."));
            new MachineLoader().unloadMachines();
            new MachineLoader().loadMachines();
            if (isDebugReloadEnabled) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &b&lReload &3Machines &bComplete&e!"));


            JadGens.getInstance().reloadConfig();
            if (isDebugReloadEnabled) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &3Config &b&lReloaded&e!"));


            JadGens.getInstance().getLangFile().reloadLang();
            if (isDebugReloadEnabled) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &3Lang file &b&lReloaded&e!"));


            JadGens.getInstance().setupAPIDebug();
            if (isDebugReloadEnabled) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &3API Debug &b&lReloaded&e!"));


            if (isDebugReloadEnabled) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &b&lReloading &3Producer&e.."));
            JadGens.getInstance().getProducer().cancel();
            JadGens.getInstance().setProducer(new ProduceRunnable().runTaskTimer(JadGens.getInstance(), JadGens.getInstance().getConfig().getInt("machinesConfig.machinesDelay") * 20L, JadGens.getInstance().getConfig().getInt("machinesConfig.machinesDelay") * 20L));
            if (isDebugReloadEnabled) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &3Producer &b&lReloaded&e!"));


            if (isDebugReloadEnabled) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eRe-hooking into &3&lPlugins&e.."));


            if (isDebugReloadEnabled) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eKooking into &3&lVault&e.."));
            JadGens.getInstance().hookVault();
            if (isDebugReloadEnabled && JadGens.getInstance().isHookedVault())
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eHooked into &3&lVault&e.."));
             else
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &cCouldn't &ehook into &3&lVault&e.."));


            if (isDebugReloadEnabled) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eKooking into &3&lPlaceHolderAPI&e.."));
            JadGens.getInstance().hookPlaceHolderAPI();
            if (isDebugReloadEnabled && JadGens.getInstance().isHookedPlaceHolderAPI())
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eHooked into &3&lPlaceHolderAPI&e.."));
            else
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &cCouldn't &ehook into &3&lPlaceHolderAPI&e.."));


            if (isDebugReloadEnabled) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eKooking into &3&lPlayerPoints&e.."));
            JadGens.getInstance().hookPlayerPoints();
            if (isDebugReloadEnabled && JadGens.getInstance().isHookedPlaceHolderAPI())
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eHooked into &3&lPlayerPoints&e.."));
            else
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &cCouldn't &ehook into &3&lPlayerPoints&e.."));

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.reloadMessages.pluginReloaded")));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.noPermission")));
        }
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
