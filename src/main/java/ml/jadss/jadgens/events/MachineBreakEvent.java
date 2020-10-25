package ml.jadss.jadgens.events;

import ml.jadss.jadgens.utils.Machine;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@SuppressWarnings("unused")
public class MachineBreakEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private boolean isMachineOwner;
    private int type;
    private Block block;
    private boolean cancelled;

    public MachineBreakEvent(Player player, int machine_type, boolean is_Machine_Owner, Block blocka) {
        this.player = player;
        this.type = machine_type;
        this.isMachineOwner = is_Machine_Owner;
        this.block = blocka;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
    public boolean isCancelled() {
        return cancelled;
    }

    public boolean isMachineOwner() {
        return isMachineOwner;
    }
    public Player getPlayer() {
        return player;
    }
    public int getType() {
        return type;
    }
    public void removeMachine() { Machine mac = new Machine(block); mac.removeFromConfig(); block.setType(Material.AIR); }
    public Block getBlock() { return block; }

    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}