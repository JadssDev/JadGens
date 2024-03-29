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

    /**
     * Get the status on placeholder.
     * @return the status on placeholder.
     */
    String getStatusOnPlaceholder();

    /**
     * Get the status off placeholder.
     * @return the status off placeholder.
     */
    String getStatusOffPlaceholder();

    /**
     * Gets the lines of the hologram!
     * @return the lines of the hologram!
     */
    String[] getHologramLines();

    /**
     * Gets the hologram Y axis offset. This is the Y axis offset of the hologram.
     * @return the hologram Y axis offset.
     */
    int getHologramYAxisOffset();

    /**
     * Gets the hologram lines with the machine information.
     * @param machine the machine to get the information from.
     * @return the hologram lines with the machine information.
     */
    String[] parseHologramLines(Machine machine);
}
