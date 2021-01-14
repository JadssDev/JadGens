package ml.jadss.jadgens.events;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.JadGensAPI;
import ml.jadss.jadgens.utils.Machine;
import ml.jadss.jadgens.utils.MachinePurger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * This event is called hwen a machine is loaded.<p>
 * This means, when the server starts and all the machines are loaded
 */
@SuppressWarnings("unused")
public class MachineLoadEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Machine machine;

    public MachineLoadEvent(Machine machine) {
        this.machine = machine;
    }

    public UUID getOwner() { return UUID.fromString(machine.getOwner()); }
    public String getDisplayName() { return machine.getDisplayName(); }
    public String getID() { return machine.getId(); }
    public int getType() { return machine.getType(); }
    public Location getLocation() { return machine.getLocation(); }
    public Block getBlock() { return machine.getLocation().getBlock(); }
    public Machine getMachine() { return machine; }
    public void removeMachine(JadGensAPI api) {
        if (api.isAPIValid()) {
            if (JadGens.getInstance().isAPIDebugEnabled())
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&3JadGens &7>> &eThe &3&lMachine &ewith &b&lID &a\"" + this.getID() + "\" &ewas &c&lRemoved &eby " + api.getPluginName()));
            new MachinePurger().removeMachineInstant(this.getID());
        }
    }

    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}