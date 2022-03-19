package dev.jadss.jadgens.api;

import dev.jadss.jadgens.api.config.fuelConfig.FuelConfiguration;
import dev.jadss.jadgens.api.config.interfaces.LoadedFuelConfiguration;
import dev.jadss.jadgens.api.config.interfaces.LoadedMachineConfiguration;
import dev.jadss.jadgens.api.config.machineConfig.MachineConfiguration;

/**
 * Very careful when using these unsafe methods!
 */
public interface UnsafeMachinesAPI {

    /**
     * Loads the configuration into JadGens with a specific LoadedMachineConfiguration.
     * @param configuration the configuration to load
     * @return the loaded configuration.
     */
    LoadedMachineConfiguration loadConfiguration(MachineConfiguration configuration);

    /**
     * Loads the configuration into JadGens with a specific LoadedFuelConfiguration.
     * @param configuration the configuration to load
     * @return the loaded configuration.
     */
    LoadedFuelConfiguration loadConfiguration(FuelConfiguration configuration);
}
