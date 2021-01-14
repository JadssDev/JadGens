package ml.jadss.jadgens.listeners;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.dependencies.Compatibility;
import ml.jadss.jadgens.dependencies.nbt.NBTCompound;
import ml.jadss.jadgens.dependencies.nbt.NBTItem;
import ml.jadss.jadgens.utils.Fuel;
import ml.jadss.jadgens.utils.Machine;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    Fuel checker1 = new Fuel();

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null &&
                e.getCurrentItem() != null &&
                new Compatibility().getTitle(e.getClickedInventory(), e.getView()).equals(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machineGui.title")))) {

            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            NBTCompound nbt = new NBTItem(e.getCurrentItem());
            //do shenanigans.
            if (nbt.getBoolean("JadGens_toggleItem")) {
                Machine machine = new Machine(new NBTItem(e.getCurrentItem()).getString("JadGens_machineID"));
                if (machine.getId() == null) return;

                if (machine.getOwner().equalsIgnoreCase("none")) {
                    machine.setMachineEnabled(false);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getLangFile().lang().getString("messages.machinesMessages.noOwner")));
                    return;
                }

                player.closeInventory();
                machine.setMachineEnabled(!machine.isMachineEnabled());
                player.openInventory(machine.createGUI());

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getLangFile().lang().getString("messages.machinesMessages.machineToggled").replace("%enabled%", machine.getMachineStatusText())));
            } else if (new NBTItem(e.getCurrentItem()).getBoolean("JadGens_dropsItem")) {
                if (e.getCursor() != null && e.getCursor().getItemMeta() != null && checker1.isFuelItem(e.getCursor())) {
                    Machine machine = new Machine(new NBTItem(e.getCurrentItem()).getString("JadGens_machineID"));
                    if (machine.getId() == null) return;

                    if (machine.getOwner().equalsIgnoreCase("none")) {
                        machine.setMachineEnabled(false);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getLangFile().lang().getString("messages.machinesMessages.noOwner")));
                        return;
                    }

                    Fuel fuel = new Fuel(e.getCursor());

                    if (!machine.isFuelCompatible(fuel.getType())) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getLangFile().lang().getString("messages.fuelMessages.notRightFuel")));
                        return;
                    }

                    boolean executed = false;
                    boolean finishedFueling = false;
                    int count = 0;
                    while (!finishedFueling && machine.getDropsRemaining() + fuel.getDrops() <= machine.getDropsMax() && player.getItemOnCursor() != null) {
                        executed = true;
                        count++;
                        machine.setDropsRemaining(machine.getDropsRemaining() + fuel.getDrops());

                        if (player.getItemOnCursor().getAmount() == 1) {
                            e.getWhoClicked().setItemOnCursor(null);
                            finishedFueling = true;
                        } else {
                            player.getItemOnCursor().setAmount(player.getItemOnCursor().getAmount() - 1);
                        }
                    }

                    if (executed) {
                        if (count == 1) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getLangFile().lang().getString("messages.fuelMessages.used")));
                        } else player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getLangFile().lang().getString("messages.fuelMessages.usedMultiple").replace("%many%", String.valueOf(count))));
                    } else player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getLangFile().lang().getString("messages.fuelMessages.doesntAcceptMoreFuel")));


                } else {
                    e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getLangFile().lang().getString("messages.machinesMessages.clickWithFuel")));
                }
            }

        }
    }
}
