package dev.jadss.jadgens.api.events;

import dev.jadss.jadgens.api.config.interfaces.LoadedFuelConfiguration;
import dev.jadss.jadgens.api.machines.MachineInstance;
import org.bukkit.event.HandlerList;

/**
 * Called when a machine is fueled by a Hopper right beside it.
 */
public class MachineFuelByHopperEvent extends MachineEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private int fuelAmount;
    private LoadedFuelConfiguration fuel;

    public MachineFuelByHopperEvent(MachineInstance machine, int fuelAmount, LoadedFuelConfiguration fuel) {
        super(machine);
        this.fuelAmount = fuelAmount;
        this.fuel = fuel;
    }

    /**
     * Get the fuel amount that this machine has been fueled for!
     * @return the amount of fuel that this machine will receive when this event ends!
     */
    public int getFuelAmount() {
        return fuelAmount;
    }

    /**
     * Set the fuel amount that this machine has been fueled for!
     * @param fuelAmount the amount of fuel that this machine will receive when this event ends!
     */
    public void setFuelAmount(int fuelAmount) {
        this.fuelAmount = fuelAmount;
    }

    /**
     * Get the fuel that was used to fuel the machine!
     * @return the fuel that was used to fuel the machine!
     */
    public LoadedFuelConfiguration getFuel() {
        return fuel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
