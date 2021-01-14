package ml.jadss.jadgens.listeners;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.events.MachineBreakEvent;
import ml.jadss.jadgens.events.MachineUnloadEvent;
import ml.jadss.jadgens.utils.Machine;
import ml.jadss.jadgens.utils.MachineLookup;
import ml.jadss.jadgens.utils.MachinePurger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

public class PlayerBreakListener implements Listener {

    MachineLookup checker = new MachineLookup();

    @EventHandler
    public void PlayerBreakEvent(BlockBreakEvent e) {
        Block block = e.getBlock();
        Player player = e.getPlayer();

        if (JadGens.getInstance().getBlocksRemover().getBlocks().contains(e.getBlock())) { e.setCancelled(true); return; }
        if (!checker.isMachine(block)) return;

        Machine machine = new Machine(block);

        MachineBreakEvent event = new MachineBreakEvent(player, machine.getType(), (machine.getOwner().equalsIgnoreCase("none") || machine.getOwner().equalsIgnoreCase(player.getUniqueId().toString())), block);
        JadGens.getInstance().getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) { e.setCancelled(true); return; }

        e.setCancelled(true);
        if (machine.getOwner().equalsIgnoreCase("none") || UUID.fromString(machine.getOwner()).equals(player.getUniqueId()) || player.hasPermission(lang().getString("messages.bypassPermission"))) {
            if (player.getInventory().firstEmpty() != -1) {

                MachineUnloadEvent event1 = new MachineUnloadEvent(machine);
                Bukkit.getServer().getPluginManager().callEvent(event1);

                new MachinePurger().removeMachineInstant(machine.getId());

                player.getInventory().addItem(machine.createItem(machine.getType()));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.machinesMessages.broken")));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.noInventorySpace")));
            }
        } else {
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.machinesMessages.notTheOwner")));
        }
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
