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
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteractListener(PlayerInteractEvent e) {
        if (e.getItem() == null || e.getPlayer().getItemInHand() == null || !e.getItem().equals(e.getPlayer().getItemInHand())) return;
        Player player = e.getPlayer();
        ItemStack item = e.getPlayer().getInventory().getItemInHand();
        Block block = e.getClickedBlock();

        if (new Fuel().isFuelItem(item)) {
            e.setCancelled(true);
            if (new MachineLookup().isMachine(block)) {
                Fuel fuel = new Fuel(item);
                Machine machine = new Machine(block);

                if (machine.getOwner().equalsIgnoreCase("none")) {
                    machine.setMachineEnabled(false);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getLangFile().lang().getString("messages.machinesMessages.noOwner")));
                    return;
                }

                if (JadGens.getInstance().getConfig().getBoolean("machines." + machine.getType() + ".fuels.needsFuelToProduce")) {
                    if (machine.isFuelCompatible(fuel.getType())) {
                        if (machine.getDropsRemaining() + fuel.getDrops() <= machine.getDropsMax()) {
                            if (e.getAction() == Action.LEFT_CLICK_BLOCK || player.isSneaking()) {

                                boolean finishedFueling = false;
                                int count = 0;
                                while(!finishedFueling && machine.getDropsRemaining() + fuel.getDrops() <= machine.getDropsMax() && e.getPlayer().getInventory().getItemInHand().getType() != Material.AIR) {
                                    count++;
                                    machine.setDropsRemaining(machine.getDropsRemaining()+fuel.getDrops());

                                    if (e.getPlayer().getInventory().getItemInHand().getAmount() == 1) {
                                        e.getPlayer().getInventory().setItemInHand(null);
                                        finishedFueling = true;
                                    } else {
                                        e.getPlayer().getInventory().getItemInHand().setAmount(e.getPlayer().getInventory().getItemInHand().getAmount()-1);
                                    }
                                }

                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.fuelMessages.usedMultiple").replace("%many%", String.valueOf(count))));

                            } else {
                                if (e.getPlayer().getInventory().getItemInHand().getAmount() == 1) {
                                    e.getPlayer().getInventory().setItemInHand(null);
                                } else {
                                    e.getPlayer().getInventory().getItemInHand().setAmount(item.getAmount() - 1);
                                }
                                machine.setDropsRemaining(machine.getDropsRemaining() + fuel.getDrops());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.fuelMessages.used")));
                            }

                        } else player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.fuelMessages.doesntAcceptMoreFuel")));
                    } else player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.fuelMessages.notRightFuel")));
                } else player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.fuelMessages.machineNotAcceptingFuel")));
            } else player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.fuelMessages.notAMachine")));
        }
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
