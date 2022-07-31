package dev.jadss.jadgens.listeners;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.utils.reflection.reflectors.JMethodReflector;
import dev.jadss.jadgens.api.MachinesAPI;
import dev.jadss.jadgens.api.config.interfaces.LoadedFuelConfiguration;
import dev.jadss.jadgens.api.machines.MachineInstance;
import dev.jadss.jadgens.api.events.MachineFuelEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockInteractListener implements Listener {

    private final MachinesAPI api = MachinesAPI.getInstance();

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        //Check if it's the main hand this is getting called at!
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_9)) {
            Enum<?> handEnum = ((Enum<?>) JMethodReflector.executeMethod(event.getClass(), "getHand", event, new Object[]{}));

            if (handEnum == null)
                return;
            if (handEnum.name().equals("OFF_HAND"))
                return;
        }

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

            if (!machine.getMachine().getMachineConfiguration().needsFuelToProduce()) {
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', api.getGeneralConfiguration().getMessages().fuelMessages.machineHasInfiniteFuel));
                return;
            }

            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                int fuelCount = 1; //How many fuels we will use.
                int playerFuelCount = event.getPlayer().getItemInHand().getAmount(); //how many fuels the player has in total.
                while (machine.getFuelAmount() + (fuel.getFuelAmount() * fuelCount) < machine.getMachine().getMachineConfiguration().getMaxFuelAmount()) {
                    if (fuelCount == playerFuelCount)
                        break;
                    fuelCount++;
                }

                int fuelAmount = fuel.getFuelAmount() * fuelCount;

                MachineFuelEvent machineFuelEvent = new MachineFuelEvent(machine, event.getPlayer(), fuelAmount, true, fuel);
                Bukkit.getPluginManager().callEvent(machineFuelEvent);
                if (machineFuelEvent.isCancelled())
                    return;

                //Update it!
                fuelAmount = machineFuelEvent.getFuelAmount();

                if (fuelCount == playerFuelCount)
                    event.getPlayer().setItemInHand(null);
                else
                    event.getPlayer().getItemInHand().setAmount(playerFuelCount - fuelCount);

                //Set fuel!
                machine.setFuelAmount(fuelAmount);

                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', api.getGeneralConfiguration().getMessages().fuelMessages.usedMultipleFuels.replace("%amount%", "" + fuelCount)));
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                MachineFuelEvent machineFuelEvent = new MachineFuelEvent(machine, event.getPlayer(), fuel.getFuelAmount(), false, fuel);
                Bukkit.getPluginManager().callEvent(machineFuelEvent);
                if (machineFuelEvent.isCancelled())
                    return;

                if (fuel.getFuelAmount() != machineFuelEvent.getFuelAmount() ||
                        machine.getFuelAmount() + fuel.getFuelAmount() <= machine.getMachine().getMachineConfiguration().getMaxFuelAmount()) {
                    if (event.getPlayer().getItemInHand().getAmount() == 1)
                        event.getPlayer().setItemInHand(null);
                    else
                        event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
                    machine.setFuelAmount(machine.getFuelAmount() + fuel.getFuelAmount());
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', api.getGeneralConfiguration().getMessages().fuelMessages.usedFuel));
                } else {
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', api.getGeneralConfiguration().getMessages().fuelMessages.reachedMaxCapacity));
                }
            }
        } else if (api.getGeneralConfiguration().getMessages().machineMenu.enabled && (!api.isMachine(event.getItem()) && event.getAction() == Action.RIGHT_CLICK_BLOCK)) {

            MachineInstance machine = api.getMachine(event.getClickedBlock().getLocation());
            if (machine == null)
                return;

            event.setCancelled(true);

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
