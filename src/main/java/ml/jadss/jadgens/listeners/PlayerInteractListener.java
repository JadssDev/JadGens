package ml.jadss.jadgens.listeners;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.utils.Fuel;
import ml.jadss.jadgens.utils.Machine;
import ml.jadss.jadgens.utils.MachineLookup;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteractListener(PlayerInteractEvent e) {
        if (e.getItem() == null | e.getPlayer().getItemInHand() == null) return;
        if (!e.getItem().equals(e.getPlayer().getItemInHand())) return;
        Player pl = e.getPlayer();
        ItemStack item = e.getPlayer().getInventory().getItemInHand();
        Block block = e.getClickedBlock();
        if (new Fuel().isFuelItem(item)) {
            e.setCancelled(true);
            if (pl.isSneaking()) {
                if (new MachineLookup().isMachine(block)) {
                    Fuel fuel = new Fuel(item);
                    String id = block.getLocation().getWorld().getName() + "_" + block.getLocation().getBlockX() + "_" + block.getLocation().getBlockY() + "_" + block.getLocation().getBlockZ();
                    Machine machine = new Machine(id);
                    if (!(JadGens.getInstance().getConfig().getBoolean("machines." + machine.getType() + ".fuels.needsFuelToProduce"))) {
                        pl.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.fuelMessages.machineNotAcceptingFuel")));
                    } else {
                        if (machine.isFuelCompatible(fuel.getType())) {
                            Integer drops = machine.getDropsRemaining();
                            if (drops + fuel.getDrops() <= JadGens.getInstance().getConfig().getInt("machines." + machine.getType() + ".fuels.maxFuel")) {
                                ItemStack inHand = pl.getItemInHand();
                                inHand.setAmount(inHand.getAmount() - 1);
                                pl.setItemInHand(inHand);
                                machine.setDropsRemaining( machine.getDropsRemaining() + fuel.getDrops() );
                                machine.addToConfig();
                                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.fuelMessages.used")));
                            } else {
                                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.fuelMessages.doesntAcceptMoreFuel")));
                            }
                        } else {
                            pl.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.fuelMessages.notRightFuel")));
                        }
                    }
                } else {
                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.fuelMessages.notAMachine")));
                }
            }
        }
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
