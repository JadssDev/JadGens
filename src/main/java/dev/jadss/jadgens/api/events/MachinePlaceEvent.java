package dev.jadss.jadgens.api.events;

import dev.jadss.jadgens.api.machines.MachineInstance;
import org.bukkit.event.HandlerList;

/**
 * Called when a machine is placed.
 */
public class MachinePlaceEvent extends MachineEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public MachinePlaceEvent(MachineInstance machine) {
        super(machine);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
