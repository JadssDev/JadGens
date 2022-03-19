package dev.jadss.jadgens.listeners;

import dev.jadss.jadgens.api.MachinesAPI;
import dev.jadss.jadgens.api.config.interfaces.LoadedFuelConfiguration;
import dev.jadss.jadgens.api.machines.MachineInstance;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockInteractListener implements Listener {

    private final MachinesAPI api = MachinesAPI.getInstance();

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        //Check if it's the main hand this is getting called at!
        if (event.getItem() != null && event.getPlayer().getItemInHand() != null && !event.getItem().equals(event.getPlayer().getItemInHand()))
            return;

        if (api.isFuel(event.getItem())) {

            MachineInstance machine = api.getMachine(event.getClickedBlock().getLocation());
            if (machine == null)
                return;

            LoadedFuelConfiguration fuel = api.getFuelConfigurationByItem(event.getItem());

            if (!machine.getMachine().getMachineConfiguration().isFuelCompatible(fuel)) {
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', api.getGeneralConfiguration().getMessages().fuelMessages.incompatibleFuel));
                return;
            }

            event.setCancelled(true);

            if(!machine.getMachine().getMachineConfiguration().needsFuelToProduce()) {
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', api.getGeneralConfiguration().getMessages().fuelMessages.machineHasInfiniteFuel));
                return;
            }

            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                int count = 0;
                while(machine.getFuelAmount() + fuel.getFuelAmount() <= machine.getMachine().getMachineConfiguration().getMaxFuelAmount() && event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType() != Material.AIR) {
                    count++;
                    machine.setFuelAmount(machine.getFuelAmount()+fuel.getFuelAmount());
                    if (event.getPlayer().getItemInHand().getAmount() == 1)
                        event.getPlayer().setItemInHand(null);
                    else
                        event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
                }
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', api.getGeneralConfiguration().getMessages().fuelMessages.usedMultipleFuels.replace("%amount%", "" + count)));
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(machine.getFuelAmount() + fuel.getFuelAmount() <= machine.getMachine().getMachineConfiguration().getMaxFuelAmount()) {
                    if (event.getPlayer().getItemInHand().getAmount() == 1)
                        event.getPlayer().setItemInHand(null);
                    else
                        event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
                    machine.setFuelAmount(machine.getFuelAmount()+fuel.getFuelAmount());
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', api.getGeneralConfiguration().getMessages().fuelMessages.usedFuel));
                } else {
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', api.getGeneralConfiguration().getMessages().fuelMessages.reachedMaxCapacity));
                }
            }
        } else if (!api.isMachine(event.getItem()) && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            MachineInstance machine = api.getMachine(event.getClickedBlock().getLocation());
            if(machine == null)
                return;

            if (machine.getMachine().getOwner() == null) {
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', api.getGeneralConfiguration().getMessages().machineMessages.noOwner));
            } else if (machine.getMachine().getOwner().equals(event.getPlayer().getUniqueId()) || event.getPlayer().hasPermission(MachinesAPI.getInstance().getGeneralConfiguration().getPermissions().machineBypassPermission)) {
                machine.openMachineMenu(event.getPlayer());
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', api.getGeneralConfiguration().getMessages().machineMessages.openedMenu));
            } else {
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', api.getGeneralConfiguration().getMessages().machineMessages.notOwner));
            }
        }
    }
}
