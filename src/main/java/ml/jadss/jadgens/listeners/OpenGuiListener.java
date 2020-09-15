package ml.jadss.jadgens.listeners;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.utils.Machine;
import ml.jadss.jadgens.utils.MachineLookup;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class OpenGuiListener implements Listener {

    @EventHandler
    public void openGuiOnMachineClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player pl = e.getPlayer();
        if (pl.isSneaking()) return;
        if (!pl.getItemInHand().getType().equals(Material.AIR)) return;
        MachineLookup machineCaller = new MachineLookup();
        if (machineCaller.isMachine(e.getClickedBlock())) {
            Location loc = e.getClickedBlock().getLocation();
            String id = loc.getWorld().getName() + "_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
            Machine machineBlock = new Machine(id);
            if (pl.getUniqueId().equals(UUID.fromString(machineBlock.getOwner())) || pl.hasPermission(lang().getString("messages.bypassPermission"))) {
                pl.openInventory(machineBlock.createGUI());
            } else {
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.machinesMessages.notTheOwner")));
            }
        }
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
