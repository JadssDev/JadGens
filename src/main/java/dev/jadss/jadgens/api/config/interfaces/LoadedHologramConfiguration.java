package dev.jadss.jadgens.api.config.interfaces;

import dev.jadss.jadgens.api.machines.Machine;

public interface LoadedHologramConfiguration {

    /**
     * Represents the Machine configuration these configurations are under.
     * @return the config under this config.
     */
    LoadedMachineConfiguration getSuperConfiguration();


    /**
     * Checks if the hologram is enabled.
     * @return true if the hologram is enabled, false otherwise.
     */
    boolean isHologramEnabled();

    String getStatusOnPlaceholder();

    String getStatusOffPlaceholder();

    /**
     * Gets the lines of the hologram!
     * @return the lines of the hologram!
     */
    String[] getHologramLines();

    /**
     * Gets the hologram lines with the machine information.
     * @param machine the machine to get the information from.
     * @return the hologram lines with the machine information.
     */
    String[] parseHologramLines(Machine machine);
}
