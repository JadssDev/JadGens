package dev.jadss.jadgens.events;

import dev.jadss.jadgens.api.machines.MachineInstance;
import org.bukkit.event.HandlerList;

/**
 * Get the machine that was placed!
 */
public class MachinePlaceEvent extends MachineEvent {

    private static final HandlerList handlerList = new HandlerList();

    public MachinePlaceEvent(MachineInstance machine) {
        super(machine);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
