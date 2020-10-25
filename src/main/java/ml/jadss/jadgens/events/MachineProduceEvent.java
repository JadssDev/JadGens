package ml.jadss.jadgens.events;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.utils.Machine;
import ml.jadss.jadgens.utils.PurgeMachines;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

import static ml.jadss.jadgens.JadGensAPI.validatePlugin;

@SuppressWarnings("unused")
public class MachineProduceEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Machine machine;
    private int dropsRemaining;
    private String id;
    private Location location;
    private UUID owner;
    private int machineType;

    private boolean cancelled;

    public MachineProduceEvent(Machine mac) {
        this.machine = mac;
        this.dropsRemaining = mac.getDropsRemaining();
        this.id = mac.getId();
        this.location = mac.getLocation();
        this.owner = UUID.fromString(mac.getOwner());
        this.machineType = mac.getType();
    }

    public void setCancelled(boolean cancel) { cancelled = cancel; }
    public boolean isCancelled() { return cancelled; }

    public Machine getMachine() { return machine; }
    public int getDropsRemaining() { return dropsRemaining; }
    public void setDropsRemaining(int dropsRemaining) { machine.setDropsRemaining(dropsRemaining); }
    public String getId() { return id; }
    public Location getLocation() { return location; }
    public UUID getOwner() { return owner; }
    public int getType() { return machineType; }

    public void removeMachine(Plugin plugin) {
        if (validatePlugin(plugin)) {
            if (JadGens.getInstance().isAPIDebugEnabled())
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&3JadGens &7>> &eThe &3&lMachine &ewith &b&lID &a\"" + id + "\" &ewas &c&lRemoved &eby " + plugin.getDescription().getName()));
            new PurgeMachines().removeMachine(this.getId());
        }
    }

    public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }

}