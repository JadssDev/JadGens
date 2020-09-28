package ml.jadss.jadgens.tasks;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.events.ProduceEvent;
import ml.jadss.jadgens.utils.Machine;
import ml.jadss.jadgens.utils.PurgeMachines;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;


public class ProduceRunnable extends BukkitRunnable {

    @Override
    public void run() {
        ProduceEvent produceEvent = new ProduceEvent();
        JadGens.getInstance().getServer().getPluginManager().callEvent(produceEvent);
        if (produceEvent.isCancelled()) return;
        if (!data().isConfigurationSection("machines")) return;
        Set<String> keys = data().getConfigurationSection("machines").getKeys(false);
        for (String id : keys) {
            if (new PurgeMachines().removeIfAir(id)) continue;
            int type = data().getInt("machines." + id + ".type");
            if (JadGens.getInstance().getConfig().getBoolean("machines." + type + ".needsFuelToProduce")) {
                if (data().getInt("machines." + id + ".drops") > 0) {
                    data().set("machines." + id + ".drops", data().getInt("machines." + id + ".drops")-1);
                    new Machine(id).produce();
                }
            } else {
                new Machine(id).produce();
            }
        }
        JadGens.getInstance().getDataFile().saveData();
    }
    
    protected FileConfiguration data() {
        return JadGens.getInstance().getDataFile().data();
    }
    
}
