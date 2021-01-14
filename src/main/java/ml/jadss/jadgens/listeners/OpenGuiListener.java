package ml.jadss.jadgens.listeners;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.utils.Fuel;
import ml.jadss.jadgens.utils.Machine;
import ml.jadss.jadgens.utils.MachineLookup;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class OpenGuiListener implements Listener {

    private Fuel checker1 = new Fuel();
    private MachineLookup checker2 = new MachineLookup();
    private Machine checker3 = new Machine();

    @EventHandler
    public void openGuiOnMachineClick(PlayerInteractEvent e) {

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK ||
                e.getPlayer().isSneaking() ||
                checker1.isFuelItem(e.getPlayer().getInventory().getItemInHand()) ||
                checker3.isMachineItem(e.getPlayer().getInventory().getItemInHand()) ||
                e.getPlayer().getInventory().getItemInHand().getType() != Material.AIR) return;

        Player pl = e.getPlayer();
        Block block = e.getClickedBlock();
        if (checker2.isMachine(block)) {
            Machine machine = new Machine(block);
            if (machine.getOwner().equalsIgnoreCase("none") || pl.getUniqueId().equals(UUID.fromString(machine.getOwner())) || pl.hasPermission(lang().getString("messages.bypassPermission"))) {
                e.setCancelled(true);
                pl.openInventory(machine.createGUI());
            } else {
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.machinesMessages.notTheOwner")));
                e.setCancelled(true);
            }
        }
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
