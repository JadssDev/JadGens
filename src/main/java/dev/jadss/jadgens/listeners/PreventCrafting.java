package dev.jadss.jadgens.listeners;

import dev.jadss.jadgens.api.MachinesAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class PreventCrafting implements Listener {

    private final MachinesAPI api = MachinesAPI.getInstance();

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        for (ItemStack item : event.getInventory().getMatrix())
            if (api.isMachine(item) || api.isFuel(item)) {
                event.setCancelled(true);
                break;
            }
    }
}