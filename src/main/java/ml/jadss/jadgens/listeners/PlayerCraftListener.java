package ml.jadss.jadgens.listeners;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.utils.Fuel;
import ml.jadss.jadgens.utils.Machine;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerCraftListener implements Listener {

    Machine checker1 = new Machine();
    Fuel checker2 = new Fuel();

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        if (!JadGens.getInstance().getConfig().getBoolean("machinesConfig.preventCrafting")) return;

        if (e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();

            for(ItemStack checking : e.getInventory().getMatrix()) {

                if (checking != null && checking.getType() != Material.AIR && checker1.isMachineItem(checking) || checker2.isFuelItem(checking)) {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getLangFile().lang().getString("messages.noCraftingItems")));
                    return;
                }

            }
        }
    }
}
