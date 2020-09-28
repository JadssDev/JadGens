package ml.jadss.jadgens.listeners;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.events.MachineBreakEvent;
import ml.jadss.jadgens.events.MachineLoadEvent;
import ml.jadss.jadgens.events.MachineUnloadEvent;
import ml.jadss.jadgens.utils.Machine;
import ml.jadss.jadgens.utils.MachineLookup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

public class PlayerBreakListener implements Listener {

    @EventHandler
    public void PlayerBreakEvent(BlockBreakEvent e) {
        Block block = e.getBlock();
        Player pl = e.getPlayer();
        MachineLookup lookup = new MachineLookup();

        if (!lookup.isMachine(block)) return;

        String id = block.getLocation().getWorld().getName() + "_" + block.getLocation().getBlockX() + "_" + block.getLocation().getBlockY() + "_" + block.getLocation().getBlockZ();
        Machine machine = new Machine(id);
        MachineBreakEvent event = new MachineBreakEvent(pl, machine.getType(), UUID.fromString(machine.getOwner()).equals(pl.getUniqueId()), e.getBlock());
        JadGens.getInstance().getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) { e.setCancelled(true); return; }
        e.setCancelled(true);
        if (UUID.fromString(machine.getOwner()).equals(pl.getUniqueId()) || pl.hasPermission(lang().getString("messages.bypassPermission"))) {
            if (pl.getInventory().firstEmpty() != -1) {
                MachineUnloadEvent event1 = new MachineUnloadEvent(machine);
                Bukkit.getServer().getPluginManager().callEvent(event1);
                machine.removefromConfig();
                pl.getInventory().addItem(machine.createItem(machine.getType()));
                block.setType(Material.AIR);
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.machinesMessages.broken")));
            } else {
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.noInventorySpace")));
            }
        } else {
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.machinesMessages.notTheOwner")));
        }
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
