package ml.jadss.jadgens.listeners;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.utils.MachineLookup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class PistonMoveListener implements Listener {

    @EventHandler
    public void pistonExtendEvent(BlockPistonExtendEvent e) {
        if (!JadGens.getInstance().getConfig().getBoolean("machinesConfig.preventPistonsMoveMachines")) return;
        MachineLookup checker = new MachineLookup();
        for (Block b : e.getBlocks()) {
            if (checker.isMachine(b)) {
                e.setCancelled(true);
                for (Player nearPlayer : Bukkit.getOnlinePlayers()) {
                    if (nearPlayer.getLocation().distance(b.getLocation()) <= 5) {
                        nearPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.noPistonMoving")));
                    }
                }
                return;
            } else if (JadGens.getInstance().getBlocksRemover().getBlocks().contains(b)) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void pistonRetractEvent(BlockPistonRetractEvent e) {
        if (!JadGens.getInstance().getConfig().getBoolean("machinesConfig.preventPistonsMoveMachines")) return;
        MachineLookup checker = new MachineLookup();
        for (Block b : e.getBlocks()) {
            if (checker.isMachine(b)) {
                e.setCancelled(true);
                for (Player nearPlayer : Bukkit.getOnlinePlayers()) {
                    if (nearPlayer.getLocation().distance(b.getLocation()) <= 5) {
                        nearPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.noPistonMoving")));
                    }
                }
                return;
            } else if (JadGens.getInstance().getBlocksRemover().getBlocks().contains(b)) {
                e.setCancelled(true);
                return;
            }
        }
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
