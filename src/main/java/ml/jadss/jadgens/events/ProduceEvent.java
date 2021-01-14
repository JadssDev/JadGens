package ml.jadss.jadgens.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event called at the time that all the machines produce.<p>
 * Don't forget to cancel to duck everyone up!<p>
 * That's important btw.
 */
@SuppressWarnings("unused")
public class ProduceEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;

    public ProduceEvent() { }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
    public boolean isCancelled() {
        return cancelled;
    }
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

}