package dev.jadss.jadgens.utils;

import dev.jadss.jadgens.JadGens;
import dev.jadss.jadgens.api.MachinesAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class MetricsHandler {

    private final Metrics metrics;

    public MetricsHandler(int pluginId) {
        this.metrics = new Metrics(JadGens.getInstance(), pluginId);

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eEnabling &3&lMetrics&e.."));

        this.metrics.addCustomChart(new Metrics.SingleLineChart("machines", () -> {
            return MachinesAPI.getInstance().getMachines().getMachines().size();
        }));
    }
}
