package ml.jadss.jadgens.utils;

import ml.jadss.jadgens.JadGens;
import org.bukkit.entity.Player;

public class MachineLimiter {

    private final MachineLookup lookupMachines = new MachineLookup();

    public int getMaxLimit(Player pl) { //machineLimiter.amounts // default
        if (!JadGens.getInstance().getConfig().getBoolean("machineLimiter.infinite.disabled") && pl.hasPermission(JadGens.getInstance().getConfig().getString("machineLimiter.infinite.permission")))
            return -1;

        int max = JadGens.getInstance().getConfig().getInt("machineLimiter.default.amount");

        for (String id : JadGens.getInstance().getConfig().getConfigurationSection("machineLimiter.amounts").getKeys(false))
            if (pl.hasPermission(JadGens.getInstance().getConfig().getString("machineLimiter.amounts." + id + ".permission")) &&
                    JadGens.getInstance().getConfig().getInt("machineLimiter.amounts." + id + ".amount") > max)
                max = JadGens.getInstance().getConfig().getInt("machineLimiter.amounts." + id + ".amount");

        return max;
    }

    public int getMachinesLeft(Player pl) {
        int max = getMaxLimit(pl);
        if (max == -1) return max;

        int has = lookupMachines.getPlayerMachineCount(pl.getUniqueId());
        int remain = max-has;

        if (remain > 0) { return remain; } else { return 0; }
    }

    public boolean canPlaceMachine(Player pl) {
        int max = getMaxLimit(pl);
        if (max == -1) return true;
        int left = lookupMachines.getPlayerMachineCount(pl.getUniqueId());

        if (max > left) return true;
        return false;
    }
}
