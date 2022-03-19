package dev.jadss.jadgens.listeners;

import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadgens.api.MachinesAPI;
import dev.jadss.jadgens.api.machines.MachineInstance;
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
                machine.remove();
                new JPlayer(event.getPlayer()).sendMessage(api.getGeneralConfiguration().getMessages().machineMessages.broken);
            }
        } else {
            event.setCancelled(true);
            new JPlayer(event.getPlayer()).sendMessage(api.getGeneralConfiguration().getMessages().machineMessages.notOwner);
        }
    }
}
