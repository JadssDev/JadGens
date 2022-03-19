package dev.jadss.jadgens.api.config.interfaces;

import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadgens.api.config.fuelConfig.FuelConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a fuel that can be used in a machine!
 */
public interface LoadedFuelConfiguration {

    /**
     * Get the superior configuration that led to this one.
     * @return the superior configuration.
     */
    FuelConfiguration getSuperConfiguration();

    /**
     * Get the configuration name.
     * @return the name.
     */
    String getConfigurationName();

    /**
     * How much fuel this item is worth.
     * @return the fuel value.
     */
    int getFuelAmount();

    /**
     * Get the item in JadAPI format.
     * @return the item.
     */
    JItemStack getSuperItem();

    /**
     * Get the item.
     * @return the item.
     */
    ItemStack getItem();
}
