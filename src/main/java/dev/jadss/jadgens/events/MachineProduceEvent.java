package dev.jadss.jadgens.events;

import dev.jadss.jadgens.api.machines.MachineInstance;
import lombok.Getter;
import org.bukkit.event.HandlerList;

/**
 * Called when a machine produces!
 * <p><b>Note: if this event was forcefully, then cancelling it won't WORK!</b></p>
 */
@Getter
public class MachineProduceEvent extends MachineEvent {

    private static final HandlerList handlerList = new HandlerList();

    private final boolean forcefully;

    public MachineProduceEvent(MachineInstance machine, boolean forcefully) {
        super(machine);
        this.forcefully = forcefully;
    }

    /**
     * Was the machine produced forcefully?
     * @return true if it was, false if it wasn't
     */
    public boolean isForcefully() {
        return forcefully;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
