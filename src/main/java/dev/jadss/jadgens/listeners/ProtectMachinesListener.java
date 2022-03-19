package dev.jadss.jadgens.listeners;

import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadgens.api.MachinesAPI;
import dev.jadss.jadgens.api.config.interfaces.LoadedMachineConfiguration;
import dev.jadss.jadgens.api.machines.MachineInstance;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ProtectMachinesListener implements Listener {

    private final MachinesAPI api = MachinesAPI.getInstance();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onExplosion(BlockExplodeEvent event) {
        if (api.getGeneralConfiguration().protectMachines()) {
            event.blockList().removeIf(block -> api.getMachine(block.getLocation()) != null);
        } else {
            event.blockList().stream()
                    .filter(block -> api.getMachine(block.getLocation()) != null)
                    .forEach(block -> {
                        MachineInstance machine = api.getMachine(block.getLocation());
                        new JItemStack(machine.getMachine().getMachineConfiguration().getMachineItem()).drop(block.getLocation());
                        machine.remove();
                    });
        }
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        if (api.getGeneralConfiguration().protectMachines()) {
            event.blockList().removeIf(block -> api.getMachine(block.getLocation()) != null);
        } else {
            event.blockList().stream()
                    .filter(block -> api.getMachine(block.getLocation()) != null)
                    .forEach(block -> {
                        MachineInstance machine = api.getMachine(block.getLocation());
                        new JItemStack(machine.getMachine().getMachineConfiguration().getMachineItem()).drop(block.getLocation());
                        machine.remove();
                    });
        }
    }
}
