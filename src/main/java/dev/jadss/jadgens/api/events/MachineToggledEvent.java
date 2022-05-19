package dev.jadss.jadgens.api.events;

import dev.jadss.jadgens.api.machines.MachineInstance;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Called when a Machine is toggled off or on!
 */
@Getter
public class MachineToggledEvent extends MachineEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;

    private final boolean isEnabled;

    public MachineToggledEvent(@NonNull MachineInstance machine, Player player, boolean isEnabled) {
        super(machine);
        this.player = player;
        this.isEnabled = isEnabled;
    }

    /**
     * Gets the player who toggled the {@link MachineInstance} affected during this event!
     * @return the Player who toggled the {@link MachineInstance}.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the new state of the {@link MachineInstance} affected during this event!
     * @return if the machine got enabled, then this will be enabled, otherwise it will be disabled, so the machine doesn't produce with time.
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
