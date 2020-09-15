package ml.jadss.jadgens.listeners;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.events.MachinePlaceEvent;
import ml.jadss.jadgens.nbt.NBTCompound;
import ml.jadss.jadgens.nbt.NBTItem;
import ml.jadss.jadgens.utils.Machine;
import ml.jadss.jadgens.utils.MachineLimiter;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerBuildListener implements Listener {

    @EventHandler
    public void PlayerBuildEvent(BlockPlaceEvent e) {
        if (!e.getItemInHand().hasItemMeta()) return;
        Player pl = e.getPlayer();
        Block block = e.getBlock();
        ItemStack item = e.getItemInHand();
        NBTCompound nbtCompound = new NBTItem(item);
        MachineLimiter limiter = new MachineLimiter();

        if (!JadGens.getInstance().getCompatibilityMode()) {
            if (nbtCompound.getBoolean("JadGens_machine")) {
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
                return;
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
                    return;
                }
            }
        }
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
