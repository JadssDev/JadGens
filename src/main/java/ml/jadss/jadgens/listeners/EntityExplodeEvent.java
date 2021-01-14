package ml.jadss.jadgens.listeners;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.utils.MachineLookup;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityExplodeEvent implements Listener {

    @EventHandler
    public void entityExplodeEvent(org.bukkit.event.entity.EntityExplodeEvent e) {
        if (!JadGens.getInstance().getConfig().getBoolean("machinesConfig.preventMachineExplosion")) return;
        MachineLookup checker = new MachineLookup();
        for (Block b : e.blockList()) {
            if (checker.isMachine(b) || JadGens.getInstance().getBlocksRemover().getBlocks().contains(b)) {
                e.setCancelled(true);
                return;
            }
        }
    }
}
