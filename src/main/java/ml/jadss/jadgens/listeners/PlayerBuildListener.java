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

public class PlayerBuildListener implements Listener {

    MachineLimiter limiter = new MachineLimiter();
    Machine checker1 = new Machine();
    Fuel checker2 = new Fuel();

    @EventHandler(priority = EventPriority.MONITOR)
    public void PlayerBuildEvent(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        ItemStack item = e.getPlayer().getInventory().getItemInHand();

        if (item == null || item.getItemMeta() == null || e.isCancelled()) return;

        if (checker2.isFuelItem(item)) { e.setCancelled(true); return; }

        if (!JadGens.getInstance().getCompatibilityMode()) {

            if (checker1.isMachineItem(item)) {

                NBTCompound nbt = new NBTItem(item);
                int machineType = nbt.getInteger("JadGens_machineType");

                if (!limiter.canPlaceMachine(player)) {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.machinesMessages.limitReached")));
                    return;
                }

                if (!JadGens.getInstance().getConfig().getConfigurationSection("machines").getKeys(false).contains(String.valueOf(machineType))) {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.machinesMessages.oldMachine")));
                    return;
                }

                MachinePlaceEvent event1 = new MachinePlaceEvent(player, machineType);
                JadGens.getInstance().getServer().getPluginManager().callEvent(event1);
                if(event1.isCancelled()) { e.setCancelled(true); return; }

                Machine machine = new Machine(block.getLocation(), machineType, player.getUniqueId().toString());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.machinesMessages.placed")));

                MachineLoadEvent event2 = new MachineLoadEvent(machine);
                Bukkit.getServer().getPluginManager().callEvent(event2);

            }
        } else {
            for (String key : JadGens.getInstance().getConfig().getConfigurationSection("machines").getKeys(false)) {
                if (ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machines." + key + ".displayName")).equals(item.getItemMeta().getDisplayName())) {

                    int machineType = Integer.parseInt(key);

                    if (!limiter.canPlaceMachine(player)) {
                        e.setCancelled(true);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.machinesMessages.limitReached")));
                        return;
                    }

                    MachinePlaceEvent event1 = new MachinePlaceEvent(player, machineType);
                    JadGens.getInstance().getServer().getPluginManager().callEvent(event1);
                    if(event1.isCancelled()) { e.setCancelled(true); return; }

                    Machine machine = new Machine(block.getLocation(), machineType, player.getUniqueId().toString());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.machinesMessages.placed")));

                    MachineLoadEvent event2 = new MachineLoadEvent(machine);
                    Bukkit.getServer().getPluginManager().callEvent(event2);
                    return;
                }
            }
            e.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.machinesMessages.oldMachine")));
        }
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
