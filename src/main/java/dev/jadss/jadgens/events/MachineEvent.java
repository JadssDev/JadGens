package dev.jadss.jadgens.events;

import dev.jadss.jadgens.api.config.interfaces.LoadedMachineConfiguration;
import dev.jadss.jadgens.api.machines.MachineInstance;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

/**
 * Represents an Event that involves a Machine!
 */
@RequiredArgsConstructor
public abstract class MachineEvent extends Event implements Cancellable {

    private final MachineInstance machine;
    @Setter
    private boolean cancelled = false;

    /**
     * Get the Machine that was affected with this MachineEvent!
     * @return the {@link MachineInstance} that got affected
     */
    public MachineInstance getMachine() {
        return machine;
    }

    /**
     * Gets the Machine's Configuration!
     * @return the {@link LoadedMachineConfiguration} object of this machine!
     */
    public LoadedMachineConfiguration getMachineConfiguration() {
        return this.machine.getMachine().getMachineConfiguration();
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
