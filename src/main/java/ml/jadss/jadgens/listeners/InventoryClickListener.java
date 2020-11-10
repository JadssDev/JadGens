package ml.jadss.jadgens.listeners;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.dependencies.Compatibility;
import ml.jadss.jadgens.dependencies.nbt.NBTItem;
import ml.jadss.jadgens.utils.Machine;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (new Compatibility().getTitle(e.getClickedInventory(), e.getView()).equals(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machineGui.title")))) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                //do shenanigans.
                if (new NBTItem(e.getCurrentItem()).getBoolean("JadGens_toggleItem")) {
                    Machine machine = new Machine(new NBTItem(e.getCurrentItem()).getString("JadGens_machineID"));
                    Player player = (Player) e.getWhoClicked();
                    if (machine.getId() == null) return;
                    player.closeInventory();
                    machine.setMachineEnabled(!machine.isMachineEnabled());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getLangFile().lang().getString("messages.machinesMessages.machineToggled").replace("%enabled%", machine.getMachineStatusText())));
                }
            }
        }
    }
}
