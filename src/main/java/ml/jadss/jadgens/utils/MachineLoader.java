package ml.jadss.jadgens.utils;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.events.MachineLoadEvent;
import ml.jadss.jadgens.events.MachineUnloadEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;

public class MachineLoader {

    public MachineLoader() {
        return;
    }

    public void loadMachines() {
        Set<String> data = machines().getConfigurationSection("machines").getKeys(false);
        for (String s : data) {
            Machine machine = new Machine(s);
            if (machine.getId() == null) continue;
            MachineLoadEvent event = new MachineLoadEvent(machine);
            Bukkit.getServer().getPluginManager().callEvent(event);
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &b&lLoaded &f" + data.size() + " &3&lMachines&e!"));
    }

    public void loadMachine(Machine machine) {
        MachineLoadEvent event = new MachineLoadEvent(machine);
        Bukkit.getServer().getPluginManager().callEvent(event);
    }

    public void unloadMachines() {
        Set<String> data = machines().getConfigurationSection("machines").getKeys(false);
        for (String s : data) {
            Machine machine = new Machine(s);
            if (machine.getId() == null) continue;
            MachineUnloadEvent event = new MachineUnloadEvent(machine);
            Bukkit.getServer().getPluginManager().callEvent(event);
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &b&lUnloaded &f" + data.size() + " &3&lMachines&e!"));
    }

    protected FileConfiguration machines() { return JadGens.getInstance().getDataFile().data(); }
}
