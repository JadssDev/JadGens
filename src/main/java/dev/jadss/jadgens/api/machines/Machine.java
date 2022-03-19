package dev.jadss.jadgens.api.machines;

import dev.jadss.jadgens.api.config.interfaces.LoadedMachineConfiguration;
import dev.jadss.jadgens.api.config.serializers.MachineInformation;
import org.bukkit.Location;

import java.util.UUID;

/**
 * Represents a machine information in general.
 */
public interface Machine {

    /**
     * Get the instance of this machine!
     * @return the instance of the machine!
     */
    MachineInstance getInstance();

    /**
     * Get the configuration of this machine!
     * @return the configuration object!
     */
    LoadedMachineConfiguration getMachineConfiguration();

    /**
     * Get the id of the machine!
     * @return The id of the machine.
     */
    String getId();

    /**
     * Get the owner of the machine.
     * @return The owner of the machine.
     */
    UUID getOwner();

    /**
     * Get the location of the machine.
     * @return The location of the machine.
     */
    Location getLocation();

    /**
     * Checks if the machine is valid, meaning it has a valid configuration.
     * <p><b>Note</b>: if the machine is not valid, it may be removed if specified in configuration!</p>
     * <p><b>Note 2:</b> if the chunk is unloaded, JadGens won't check if the machine has the correct block.</p>
     * @return true if the machine is valid, false otherwise.
     */
    boolean isValid();

    /**
     * Checks if the machine is in a loaded chunk.
     * @return if the machine is in a loaded chunk, false otherwise.
     */
    boolean isLoaded();

    /**
     * Save the Information of this machine to a {@link MachineInformation} object.
     * @return The {@link MachineInformation} object.
     */
    MachineInformation save();

}
