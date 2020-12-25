package ml.jadss.jadgens.utils;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.events.MachineUnloadEvent;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MachinePurger {

    private int speed = JadGens.getInstance().getConfig().getInt("machinesConfig.removalTasksSpeed");
    private BukkitTask globalMachinePurger = null;
    private BukkitTask playerMachinePurger = null;
    private BukkitTask playerMachinePurger2 = null;

    public MachinePurger() { return; }

    @SuppressWarnings("all")
    public void purgeMachines() {
        AtomicInteger count = new AtomicInteger();

        List<Machine> machines = new MachineLookup().getAllMachines();

        globalMachinePurger = Bukkit.getScheduler().runTaskTimer(JadGens.getInstance(), () -> {
            for (int i = 0; i < speed; i++) {
                Machine machine = null;
                try { machine = machines.get(0); } catch (IndexOutOfBoundsException ex) { JadGens.getInstance().getBlocksRemover().updateStatus(null, false); JadGens.getInstance().getDataFile().saveData(); globalMachinePurger.cancel(); return; }
                if (machine == null || machine.getId() == null) {
                    JadGens.getInstance().getBlocksRemover().updateStatus(null, false);
                    JadGens.getInstance().getDataFile().saveData();
                    globalMachinePurger.cancel();
                    return;
                }

                int x = data().getInt("machines." + machine.getId() + ".x");
                int y = data().getInt("machines." + machine.getId() + ".y");
                int z = data().getInt("machines." + machine.getId() + ".z");
                World world = Bukkit.getServer().getWorld(data().getString("machines." + machine.getId() + ".world"));
                if (world == null) {
                    data().set("machines." + machine.getId(), null);
                } else {
                    Location location = new Location(world, x, y, z);
                    JadGens.getInstance().getBlocksRemover().getBlocks().add(location.getBlock());
                    data().set("machines." + machine.getId(), null);
                    count.getAndIncrement();
                }
                machines.remove(0);
            }
        }, 20, 60);
        JadGens.getInstance().getBlocksRemover().updateStatus(null, true);
    }


    /**
     * Remove player's machines (and gives a count of how many machines were removed.)
     *
     * @param player player to remove the machines
     * @param giveBack should we give back the machines?
     */
    public void purgeMachines(UUID player, boolean giveBack) {
        HashMap<Integer, Integer> count = new HashMap<>();
        List<Machine> machines = new MachineLookup().getPlayerMachines(player);

        playerMachinePurger = Bukkit.getScheduler().runTaskTimer(JadGens.getInstance(), () -> {
            for (int i = 0; i < this.speed; i++) {
                if (machines.size() == 0) { playerMachinePurger.cancel(); playerMachinePurger = null; return; }
                Machine machine = null;
                try { machine = machines.get(0); } catch(IndexOutOfBoundsException ignored) { playerMachinePurger.cancel(); playerMachinePurger = null; break; }

                if (machine.getLocation().getBlock() == null || machine.getLocation().getBlock().getType().equals(Material.AIR)) {
                    machines.remove(machine);
                    continue;
                }

                int total = count.getOrDefault(machine.getType(), 0);
                count.put(machine.getType(), total + 1);

                if (machine.getId() == null) return;

                data().set("machines." + machine.getId(), null);

                JadGens.getInstance().getBlocksRemover().getBlocks().add(machine.getLocation().getBlock());
                machines.remove(machine);
            }
        }, 40, 60);

        playerMachinePurger2 = Bukkit.getScheduler().runTaskTimer(JadGens.getInstance(), () -> {
            if (playerMachinePurger == null) {
                if (giveBack) {
                    List<ItemStack> types = new ArrayList<>();
                    for (int ID : count.keySet()) {
                        types.add(new Machine().createItem(ID));
                    }

                    for(int type : count.keySet()) {
                        ItemStack item = types.get(type - 1);
                        item.setAmount(count.get(type));
                        if (Bukkit.getOfflinePlayer(player).isOnline()) Bukkit.getPlayer(player).getLocation().getWorld().dropItemNaturally(Bukkit.getPlayer(player).getLocation(), item);
                    }

                    Bukkit.getPlayer(player).sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getLangFile().lang().getString("messages.actionsMessages.purgedOwnMachines")));
                }
                JadGens.getInstance().getDataFile().saveData();
                JadGens.getInstance().getDataFile().reloadData();

                JadGens.getInstance().getBlocksRemover().updateStatus(null, false);

                playerMachinePurger2.cancel();;
                playerMachinePurger2 = null;
                return;
            }
        }, 80, 120);
    }

    public boolean removeIfAir(String id) {
        if (id == null) return false;
        if (!JadGens.getInstance().getConfig().getBoolean("machinesConfig.autoDestroy")) return false;
        World world = Bukkit.getServer().getWorld(data().getString("machines." + id + ".world"));
        if (world == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eThe &3&lMachine &ewith &b&lID &a\"" + id + "\" &ewas &c&lRemoved&e!"));
            MachineUnloadEvent event = new MachineUnloadEvent(new Machine(id));
            Bukkit.getServer().getPluginManager().callEvent(event);
            data().set("machines." + id, null);
            return true;
        }
        Location loc = new Location(world, data().getInt("machines." + id + ".x"),
                data().getInt("machines." + id + ".y"),
                data().getInt("machines." + id + ".z"));

        if (new MachineLookup().isMachine(loc.getBlock())) {
            if (loc.getBlock().getType().equals(Material.AIR)) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eThe &3&lMachine &ewith &b&lID &a\"" + id + "\" &ewas &c&lRemoved&e!"));
                MachineUnloadEvent event = new MachineUnloadEvent(new Machine(id));
                Bukkit.getServer().getPluginManager().callEvent(event);
                data().set("machines." + id, null);
                JadGens.getInstance().getDataFile().saveData();
                return true;
            }
        }
        return false;
    }

    public void removeMachine(String id) {
        if (id == null) return;

        Machine mac = new Machine(id);
        if (mac.getId() == null) return;
        if (mac.getLocation().getWorld() == null) {
            data().set("machines." + id, null);
        }

        data().set("machines." + id, null);
        JadGens.getInstance().getDataFile().saveData();

        JadGens.getInstance().getBlocksRemover().getBlocks().add(mac.getLocation().getBlock());
        JadGens.getInstance().getBlocksRemover().updateStatus(null, false);
    }

    public void removeMachineInstant(String id) {
        if (id == null) return;

        Machine mac = new Machine(id);
        if (mac.getId() == null) return;
        if (mac.getLocation().getWorld() == null) {
            data().set("machines." + id, null);
        }

        data().set("machines." + id, null);
        JadGens.getInstance().getDataFile().saveData();

        mac.getLocation().getBlock().setType(Material.AIR);
    }

    public FileConfiguration data() {
        return JadGens.getInstance().getDataFile().data();
    }


}
