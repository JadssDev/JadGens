package dev.jadss.jadgens.implementations.config;

import dev.jadss.jadgens.api.config.interfaces.LoadedHologramConfiguration;
import dev.jadss.jadgens.api.config.interfaces.LoadedMachineConfiguration;
import dev.jadss.jadgens.api.machines.Machine;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import static dev.jadss.jadgens.utils.Utilities.replace;

public class LoadedHologramConfigurationImpl implements LoadedHologramConfiguration {

    private final LoadedMachineConfiguration machineConfiguration;

    private final boolean enabled;
    private final String[] lines;

    private final int yAxisOffset;

    private final String statusOn;
    private final String statusOff;

    public LoadedHologramConfigurationImpl(LoadedMachineConfiguration configuration) {
        this.machineConfiguration = configuration;

        this.enabled = configuration.getSuperConfiguration().hologramConfiguration.enabled;
        this.lines = configuration.getSuperConfiguration().hologramConfiguration.lines;

        this.yAxisOffset = configuration.getSuperConfiguration().hologramConfiguration.yAxisOffset;

        this.statusOn = configuration.getSuperConfiguration().hologramConfiguration.statusOn;
        this.statusOff = configuration.getSuperConfiguration().hologramConfiguration.statusOff;
    }

    @Override
    public LoadedMachineConfiguration getSuperConfiguration() {
        return machineConfiguration;
    }

    @Override
    public boolean isHologramEnabled() {
        return enabled;
    }

    @Override
    public String getStatusOnPlaceholder() {
        return statusOn;
    }

    @Override
    public String getStatusOffPlaceholder() {
        return statusOff;
    }

    @Override
    public String[] getHologramLines() {
        return lines;
    }

    @Override
    public int getHologramYAxisOffset() {
        return yAxisOffset;
    }

    private final HashMap<UUID, String> playerNames = new HashMap<>();

    @Override
    public String[] parseHologramLines(Machine machine) {
        String[] result = lines.clone();

        //placeholders:
        //%owner%
        //%max%
        //%status%
        //%fuel%

        String owner = playerNames.get(machine.getOwner());
        if (owner == null) {
            playerNames.put(machine.getOwner(), owner = Bukkit.getOfflinePlayer(machine.getOwner()).getName());
            if (owner == null)
                owner = "unidentified";
        }
        result = replace(result, "%owner%", owner);
        result = replace(result, "%max%", "" + machine.getMachineConfiguration().getMaxFuelAmount());
        result = replace(result, "%fuel%", "" + machine.getInstance().getFuelAmount());
        result = replace(result, "%status%", machine.getInstance().isEnabled() ? statusOn : statusOff);
        return result;
    }
}
