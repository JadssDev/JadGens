package dev.jadss.jadgens.api.config.interfaces;

import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadgens.api.config.machineConfig.MachineConfiguration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Represents a Loaded and Usable MachineConfiguration!
 */
public interface LoadedMachineConfiguration {

    //General

    /**
     * Get the configuration name of this configuration!
     * <p><b>Note:</b> This is used to identify which machines are which!</p>
     * @return the configuration name.
     */
    String getConfigurationName();

    /**
     * In how many ticks this type of machine produces.
     * @return how many ticks.
     */
    int getTicksDelay();

    /**
     * Gets what is the max fuel this machine before it's full!
     * @return the max fuel.
     */
    int getMaxFuelAmount();

    /**
     * Checks if this machine needs a specific fuel to produce!
     * @return true if it needs a specific fuel, false otherwise.
     */
    boolean IsSpecificFuelNeeded();

    /**
     * Get the specific fuel needed for this machine!
     * @return the specific fuel, or List empty if none is specific.
     */
    List<String> getSpecificFuelNeeded();

    /**
     * Get the specific fuel needed for this machine!
     * @return the specific fuel, or List empty if none is specific.
     */
    List<LoadedFuelConfiguration> getSpecificFuelConfigurationsNeeded();

    /**
     * Checks if this machine needs fuel to produce!
     * @return if the machine needs fuel to produce!
     */
    boolean needsFuelToProduce();

    /**
     * Checks if this machine is allowed to receive fuel through hoppers!
     * @return true if it can, false otherwise.
     */
    boolean isAllowedToReceiveFuelThroughHoppers();

    //Holograms.

    /**
     * Get the configuration of the holograms for the machine!
     * @return the hologram configuration.
     */
    LoadedHologramConfiguration getHologramConfiguration();

    //Particles

    /**
     * Get the configuration of the particles for the machine!
     * @return the particle configuration.
     */
    LoadedParticleConfiguration getParticleConfiguration();

    //Checkers.

    /**
     * Checks if the specified fuel can be used in this machine.
     * @param fuel the fuel to check.
     * @return true if it can be used, false otherwise.
     */
    boolean isFuelCompatible(LoadedFuelConfiguration fuel);

    //Block type

    /**
     * Get the Material that will be used for the block!
     * @return the Material!
     */
    Material getBlockMaterial();

    /**
     * Get the material that will be used for the block in JadAPI, since it contains all of them.
     * @return the Material!
     */
    JMaterial getSuperBlockMaterial();

    //Item things.

    /**
     * Gets the item that is a placeable machine!
     * @return the placeable machine item.
     */
    ItemStack getMachineItem();

    /**
     * Gets the item that is a placeable machine!
     * @return the placeable machine item.
     */
    JItemStack getSuperMachineItem();

    //Configuration things

    /**
     * Represents the production configuration.
     * @return the production configuration.
     */
    LoadedMachineProductionConfiguration getProductionConfiguration();

    /**
     * Get the Superior configuration that led to this one!
     * @return the superior configuration.
     */
    MachineConfiguration getSuperConfiguration();
}
