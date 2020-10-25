package ml.jadss.jadgens.utils;

import ml.jadss.jadgens.JadGens;
import org.bukkit.entity.Player;

public class MachineLimiter {

    public MachineLimiter() {
        return;
    }

    public int getMaxLimit(Player pl) {
        int max = JadGens.getInstance().getConfig().getInt("machineLimiter.default.amount");
        if (pl.hasPermission(JadGens.getInstance().getConfig().getString("machineLimiter.limit5.permission"))) {
            max = JadGens.getInstance().getConfig().getInt("machineLimiter.limit5.amount");
        } else if (pl.hasPermission(JadGens.getInstance().getConfig().getString("machineLimiter.limit4.permission"))) {
            max = JadGens.getInstance().getConfig().getInt("machineLimiter.limit4.amount");
        } else if (pl.hasPermission(JadGens.getInstance().getConfig().getString("machineLimiter.limit3.permission"))) {
            max = JadGens.getInstance().getConfig().getInt("machineLimiter.limit3.amount");
        } else if (pl.hasPermission(JadGens.getInstance().getConfig().getString("machineLimiter.limit2.permission"))) {
            max = JadGens.getInstance().getConfig().getInt("machineLimiter.limit2.amount");
        } else if (pl.hasPermission(JadGens.getInstance().getConfig().getString("machineLimiter.limit1.permission"))) {
            max = JadGens.getInstance().getConfig().getInt("machineLimiter.limit1.amount");
        }
        return max;
    }

    public int getMachinesLeft(Player pl) {
        MachineLookup lookup = new MachineLookup();

        int max = getMaxLimit(pl);
        if (max == -1) return -1;
        int has = lookup.getMachines(pl.getUniqueId());

        int remain = max-has;

        if (remain > 0) { return remain; } else { return 0; }
    }

    public boolean canPlaceMachine(Player pl) {
        int max = getMaxLimit(pl);
        if (max == -1) return true;
        int left = getMachinesLeft(pl);

        if (max >= left) return true;
        return false;
    }
}
