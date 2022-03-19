package dev.jadss.jadgens.events;

import dev.jadss.jadgens.api.machines.MachineInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Get the machine that was broken!
 */
public class MachineBreakEvent extends MachineEvent {

    private static final HandlerList handlerList = new HandlerList();

    private final Player player;

    public MachineBreakEvent(MachineInstance machine, Player player) {
        super(machine);
        this.player = player;
    }

    /**
     * Get the player who broke this machine!
     * @return The player who broke this machine! can be a player that is not the owner if it was an admin or with permission to break machines!
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
