package ml.jadss.jadgens.listeners;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.dependencies.nbt.NBTCompound;
import ml.jadss.jadgens.dependencies.nbt.NBTItem;
import ml.jadss.jadgens.events.MachineLoadEvent;
import ml.jadss.jadgens.events.MachinePlaceEvent;
import ml.jadss.jadgens.utils.Fuel;
import ml.jadss.jadgens.utils.Machine;
import ml.jadss.jadgens.utils.MachineLimiter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.noise.PerlinNoiseGenerator;
import org.bukkit.util.noise.PerlinOctaveGenerator;

public class PlayerBuildListener implements Listener {

    MachineLimiter limiter = new MachineLimiter();
    Machine machineChecker = new Machine();
    Fuel fuelChecker = new Fuel();

    @EventHandler(priority = EventPriority.MONITOR)
    public void PlayerBuildEvent(BlockPlaceEvent e) {
        if (!e.getItemInHand().hasItemMeta()) return;
        Player pl = e.getPlayer();
        Block block = e.getBlock();
        ItemStack item = e.getItemInHand();

        if (fuelChecker.isFuelItem(item)) { e.setCancelled(true); return; }

        if (!JadGens.getInstance().getCompatibilityMode()) {
            if (machineChecker.isMachineItem(item)) {
                NBTCompound nbtCompound = new NBTItem(item);
                if (e.isCancelled()) return;
                if (!limiter.canPlaceMachine(pl)) {
                    e.setCancelled(true);
                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.machinesMessages.limitReached")));
                    return;
                }
                int machineType = nbtCompound.getInteger("JadGens_machineType");
                MachinePlaceEvent event = new MachinePlaceEvent(pl, machineType);
                JadGens.getInstance().getServer().getPluginManager().callEvent(event);
                if(event.isCancelled()) { e.setCancelled(true); return; }
                Machine machine = new Machine(block.getLocation(), machineType, pl.getUniqueId().toString());
                machine.addToConfig();
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.machinesMessages.placed")));
                MachineLoadEvent event1 = new MachineLoadEvent(machine);
                Bukkit.getServer().getPluginManager().callEvent(event1);
                return;
            } else if(new Fuel().isFuelItem(item)) {
                e.setCancelled(true);
            }
        } else {
            for (String key : JadGens.getInstance().getConfig().getConfigurationSection("machines").getKeys(false)) {
                if (ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machines." + key + ".displayName")).equals(item.getItemMeta().getDisplayName())) {
                    if (!limiter.canPlaceMachine(pl)) {
                        e.setCancelled(true);
                        pl.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.machinesMessages.limitReached")));
                        return;
                    }
                    int machineType = Integer.parseInt(key);
                    MachinePlaceEvent event = new MachinePlaceEvent(pl, machineType);
                    JadGens.getInstance().getServer().getPluginManager().callEvent(event);
                    if(event.isCancelled()) { e.setCancelled(true); return; }
                    Machine machine = new Machine(block.getLocation(), machineType, pl.getUniqueId().toString());
                    machine.addToConfig();
                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.machinesMessages.placed")));
                    MachineLoadEvent event1 = new MachineLoadEvent(machine);
                    Bukkit.getServer().getPluginManager().callEvent(event1);
                    return;
                }
            }
        }
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
