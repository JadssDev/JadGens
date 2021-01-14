package ml.jadss.jadgens.listeners;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.utils.MachineLookup;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

public class BlockExplodeListener implements Listener {

    MachineLookup checker = new MachineLookup();

    @EventHandler
    public void blockExplodeEvent(BlockExplodeEvent e) {
        if (!JadGens.getInstance().getConfig().getBoolean("machinesConfig.preventMachineExplosion")) return;
        for (Block b : e.blockList()) {
            if (checker.isMachine(b) || JadGens.getInstance().getBlocksRemover().getBlocks().contains(b)) {
                e.setCancelled(true);
                return;
            }
        }
    }
}
