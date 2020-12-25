package ml.jadss.jadgens.tasks;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.events.ProduceEvent;
import ml.jadss.jadgens.utils.Machine;
import ml.jadss.jadgens.utils.MachinePurger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;


public class ProduceRunnable extends BukkitRunnable {

    private final MachinePurger purger = new MachinePurger();

    @Override
    public void run() {
        ProduceEvent produceEvent = new ProduceEvent();
        JadGens.getInstance().getServer().getPluginManager().callEvent(produceEvent);

        if (produceEvent.isCancelled()) return;
        if (!data().isConfigurationSection("machines")) return;

        Set<String> keys = data().getConfigurationSection("machines").getKeys(false);
        for (String id : keys) {
            Machine machine = new Machine(id);
            if (!purger.removeIfAir(machine.getId())) machine.produce(); else continue;
        }

        JadGens.getInstance().getDataFile().saveData();
        JadGens.getInstance().getBlocksRemover().updateStatus(null, false);
    }
    
    protected FileConfiguration data() {
        return JadGens.getInstance().getDataFile().data();
    }
    
}
