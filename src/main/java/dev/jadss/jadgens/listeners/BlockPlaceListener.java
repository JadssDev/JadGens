package dev.jadss.jadgens.listeners;

import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadgens.api.MachinesAPI;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    private final MachinesAPI api = MachinesAPI.getInstance();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if (api.isMachine(event.getItemInHand())) {
            if (!api.getPlayer(event.getPlayer().getUniqueId()).canPlaceMachines()) {
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', api.getGeneralConfiguration().getMessages().machineMessages.reachedMaxMachines));
                event.setCancelled(true);
                return;
            }
            //Cancel and updated block.
            api.createMachine(api.getMachineConfigurationByItem(event.getItemInHand()), event.getPlayer().getUniqueId(), event.getBlock().getLocation());

            //Notify
            new JPlayer(event.getPlayer()).sendMessage(api.getGeneralConfiguration().getMessages().machineMessages.placed);
        } else if (api.isFuel(event.getItemInHand())) {
            //Prevent fuel from being placed.
            event.setCancelled(true);
        }
    }
}
