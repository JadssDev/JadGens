package ml.jadss.jadgens.utils;

import ml.jadss.jadgens.JadGens;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MachineLookup {
    public MachineLookup() {
        return;
    }

    public int getMachines(UUID owner) {
        int count = 0;
        Set<String> keys = JadGens.getInstance().getDataFile().data().getConfigurationSection("machines").getKeys(false);
        for (String key : keys) {
            if (JadGens.getInstance().getDataFile().data().getString("machines." + key + ".owner").equals(String.valueOf(owner))) {
                count++;
            }
        }
        return count;
    }

    @SuppressWarnings("all")
    public List<Machine> getPlayerMachines(UUID player) {
        List<Machine> list = new ArrayList<>();
        list.addAll(getAllMachines());
        for (Machine mac : new ArrayList<>(list)) if (!mac.getOwner().equals(player.toString())) list.remove(mac); else continue;
        return list;
    }

    public boolean isMachine(Block block) {
        if (block == null) return false;
        String id = block.getLocation().getWorld().getName() + "_" + block.getLocation().getBlockX() + "_" + block.getLocation().getBlockY() + "_" + block.getLocation().getBlockZ();
        Machine machine = new Machine(id);
        return machine.getId() != null;
    }

    public boolean isMachine(Location loc) {
        if (loc == null) return false;
        String id = loc.getWorld().getName() + "_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
        Machine machine = new Machine(id);
        return machine.getId() != null;
    }

    public boolean isMachine(World world, int x, int y, int z) {
        String id = world.getName() + "_" + x + "_" + y + "_" + z;
        Machine machine = new Machine(id);
        return machine.getId() != null;
    }

    public int getMachines(UUID owner, int machineType) {
        int count = 0;
        Set<String> keys = JadGens.getInstance().getDataFile().data().getConfigurationSection("machines").getKeys(false);
        for (String key : keys) {
            if (JadGens.getInstance().getDataFile().data().getString("machines." + key + ".owner").equals(String.valueOf(owner)) && JadGens.getInstance().getDataFile().data().getInt("machines." + key + ".type") == machineType) {
                count++;
            }
        }
        return count;
    }

    public List<Machine> getAllMachines() {
        List<Machine> machines = new ArrayList<>();
        Set<String> keys = JadGens.getInstance().getDataFile().data().getConfigurationSection("machines").getKeys(false);
        for (String key : keys) {
            machines.add(new Machine(key));
        }

        for (Machine machine : machines) if (machine == null) machines.remove(machine);

        return machines;
    }
}
