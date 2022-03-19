package dev.jadss.jadgens.events;

import dev.jadss.jadgens.api.machines.MachineInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Called when a Machine is fueled by a player!
 */
public class MachineFuelEvent extends MachineEvent {

    private static HandlerList handlerList = new HandlerList();

    private final Player player;
    private final boolean isFuelMultiple;
    private int fuelAmount;

    public MachineFuelEvent(MachineInstance machine, Player player, int fuelAmount, boolean isFuelMultiple) {
        super(machine);
        this.player = player;
        this.fuelAmount = fuelAmount;
        this.isFuelMultiple = isFuelMultiple;
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
     * Get whether this event is a multiple fueling method! This is, left-clicking a machine with a fuel in your hand!
     * @return whether this event is a multiple fueling method!
     */
    public boolean isFuelMultiple() {
        return isFuelMultiple;
    }

    /**
     * Get the player who fueled the machine!
     * @return the player who fueled the machine!
     */
    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
