package ml.jadss.jadgens.events;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.JadGensAPI;
import ml.jadss.jadgens.utils.Machine;
import ml.jadss.jadgens.utils.MachinePurger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


/**
 * Event called when a Machine produces, not when all machines are produced, when a specific machine produces.<p>
 * Please note that.
 */
@SuppressWarnings("unused")
public class MachineProduceEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Machine machine;
    private int dropsRemaining;
    private String id;
    private Location location;
    private String owner;
    private int machineType;

    private boolean cancelled;

    public MachineProduceEvent(Machine mac) {
        this.machine = mac;
        this.dropsRemaining = mac.getDropsRemaining();
        this.id = mac.getId();
        this.location = mac.getLocation();
        this.owner = mac.getOwner();
        this.machineType = mac.getType();
    }

    public void setCancelled(boolean cancel) { cancelled = cancel; }
    public boolean isCancelled() { return cancelled; }

    public Machine getMachine() { return machine; }
    public int getDropsRemaining() { return dropsRemaining; }
    public void setDropsRemaining(int dropsRemaining) { machine.setDropsRemaining(dropsRemaining); }
    public String getID() { return id; }
    public Location getLocation() { return location; }

    /**
     * PLEASE NOTE THIS EVENT RETURNS THE UUID OF THE OWNER IN A STRING!
     * @return uuid of the owner in a string
     */
    public String getOwner() { return owner; }
    public int getType() { return machineType; }

    public void removeMachine(JadGensAPI api) {
        if (api.isAPIValid()) {
            if (JadGens.getInstance().isAPIDebugEnabled())
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&3JadGens &7>> &eThe &3&lMachine &ewith &b&lID &a\"" + this.getID() + "\" &ewas &c&lRemoved &eby " + api.getPluginName()));
            new MachinePurger().removeMachineInstant(this.getID());
        }
    }

    public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }

}