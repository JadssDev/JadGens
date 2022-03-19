package dev.jadss.jadgens.api.machines;

import org.bukkit.entity.Player;

/**
 * Represents the instance of the machine, this is, the information of the machine for it to work.!
 */
public interface MachineInstance {

    /**
     * Get the {@link Machine} object.
     * @return the {@link Machine} object.
     */
    Machine getMachine();

    /**
     * Open a machine GUI for the specified player!
     * @param player the player to open the GUI for.
     */
    void openMachineMenu(Player player);

    //Tick thingies.

    /**
     * Get the ticks that this machine needs to produce again.
     * @return the ticks that this machine needs to produce again.
     */
    int getTicksToProduce();

    /**
     * Set the ticks for this machine to produce.
     * @param ticks the ticks for it to produce in.
     */
    void setTicksToProduce(int ticks);

    /**
     * Removes 1 tick from the ticks to produce, and checks if the machine should produce again.
     */
    void tick();

    /**
     * Ticks the hologram of this machine, this may fix a hologram which wasn't working or just to update it!
     */
    void tickHologram();

    //Enabled thingies.

    /**
     * Set if this machine is enabled.
     * @param enabled true if it should be enabled, false otherwise.
     */
    void setEnabled(boolean enabled);

    /**
     * Check if this machine is enabled!
     * @return true if it is, false if not.
     */
    boolean isEnabled();

    //Fuel thingies

    /**
     * Get the fuel amount this machine has.
     * @return the amount it has.
     */
    int getFuelAmount();

    /**
     * Set the fuel amount for this machine.
     * @param fuelAmount the amount of fuel this machine has!
     */
    void setFuelAmount(int fuelAmount);

    /**
     * Add the specified quantity of fuel to this machine.
     * @param fuelAmount the amount of fuel to add.
     */
    void addFuelAmount(int fuelAmount);

    //Checkers.

    /**
     * Checks if this machine can produce!
     * @return if the machine can produce!
     */
    boolean canProduce();

    //Actions.

    /**
     * Make the machine produce.
     * @param forcefully if we should forcefully produce the machine!
     */
    void produce(boolean forcefully);

    /**
     * Checks if this machine is still valid, and is not removed.
     * @return true if it is, false if not.
     */
    boolean isValid();

    /**
     * Remove this machine.
     */
    void remove();
}
