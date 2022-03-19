package dev.jadss.jadgens.listeners;

import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadgens.api.MachinesAPI;
import dev.jadss.jadgens.api.machines.MachineInstance;
import dev.jadss.jadgens.events.MachineBreakEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private final MachinesAPI api = MachinesAPI.getInstance();

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        MachineInstance machine = api.getMachine(event.getBlock().getLocation());
        if (machine == null)
            return;

        if (api.isFuel(event.getPlayer().getItemInHand()))
            return;

        if (machine.getMachine().getOwner() == null || machine.getMachine().getOwner().equals(event.getPlayer().getUniqueId()) ||
                event.getPlayer().hasPermission(MachinesAPI.getInstance().getGeneralConfiguration().getPermissions().machineBypassPermission)) {
            if (event.getPlayer().getInventory().addItem(machine.getMachine().getMachineConfiguration().getMachineItem()).size() == 0) {

                MachineBreakEvent machineBreakEvent = new MachineBreakEvent(machine, event.getPlayer());
                Bukkit.getPluginManager().callEvent(machineBreakEvent);
                if (machineBreakEvent.isCancelled()) {
                    event.setCancelled(true);
                    return;
                }

                machine.remove();
                new JPlayer(event.getPlayer()).sendMessage(api.getGeneralConfiguration().getMessages().machineMessages.broken);
            }
        } else {
            event.setCancelled(true);
            new JPlayer(event.getPlayer()).sendMessage(api.getGeneralConfiguration().getMessages().machineMessages.notOwner);
        }
    }
}
