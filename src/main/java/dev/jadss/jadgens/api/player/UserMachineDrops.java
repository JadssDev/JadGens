package dev.jadss.jadgens.api.player;

import dev.jadss.jadgens.api.config.interfaces.LoadedMachineConfiguration;
import dev.jadss.jadgens.api.config.playerConfig.MachineDropsPlayerInformation;

public interface UserMachineDrops {

    /**
     * Checks if this instance of drops is valid, meaning it has a valid machine id and a valid amount.
     * @return true if this instance of drops is valid, false otherwise.
     */
    boolean isValid();

    /**
     * The machine id of the machine that these drops are for.
     * @return the machine id of the machine that these drops are for.
     */
    String getMachineConfigurationName();

    /**
     * Gets the configuration of the machine that this drops are for.
     * @return the {@link LoadedMachineConfiguration} of the machine that these drops are for, or null if the these drops are INVALID.
     */
    LoadedMachineConfiguration getMachineConfiguration();

    /**
     * Check if this instance of drops has at least the amount of drops specified.
     * @param amount the amount to check.
     * @return true if this instance of drops has at least the amount of drops specified, false otherwise.
     */
    boolean hasAtLeast(long amount);

    /**
     * Gets the amount of machine drops this player has.
     * @return the amount of machine drops this player has.
     */
    long getAmount();

    /**
     * Adds the amount specified to the amount of machine drops this player has.
     * @param amount the amount to add.
     * @return the amount after the addition.
     */
    long addAmount(long amount);

    /**
     * Removes the amount specified from the amount of machine drops this player has.
     * @param amount the amount to remove.
     * @return the amount after the removal.
     */
    long removeAmount(long amount);

    /**
     * Saves this information into a {@link MachineDropsPlayerInformation} object.
     * @return a {@link MachineDropsPlayerInformation} object containing the information of this instance of drops.
     */
    MachineDropsPlayerInformation save();
}
