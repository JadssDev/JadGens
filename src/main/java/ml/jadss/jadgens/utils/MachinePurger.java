package ml.jadss.jadgens.utils;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.events.MachineUnloadEvent;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MachinePurger {

    private int speed = JadGens.getInstance().getConfig().getInt("machinesConfig.removalTasksSpeed");
    private BukkitTask globalPurger = null;
    private BukkitTask playerPurger = null;
    private BukkitTask playerGiveBack = null;

    public MachinePurger() { return; }

    @SuppressWarnings("all")
    public void purgeMachines() {
        AtomicInteger count = new AtomicInteger();

        List<Machine> machines = new MachineLookup().getAllMachines();

        globalPurger = Bukkit.getScheduler().runTaskTimer(JadGens.getInstance(), () -> {
            for (int i = 0; i < speed; i++) {
                Machine machine = null;
                try { machine = machines.get(0); } catch (IndexOutOfBoundsException ex) { JadGens.getInstance().getBlocksRemover().updateStatus(null, false); JadGens.getInstance().getDataFile().saveData(); globalPurger.cancel(); return; }
                if (machine == null || machine.getId() == null) {
                    JadGens.getInstance().getBlocksRemover().updateStatus(null, false);
                    JadGens.getInstance().getDataFile().saveData();
                    globalPurger.cancel();
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


    public void purgeMachines(UUID player, boolean giveBack) {

        if (Bukkit.getOfflinePlayer(player).isOnline() && JadGens.getInstance().getPlayersPurgingMachines().contains(player)) {
            Bukkit.getPlayer(player).sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getLangFile().lang().getString("messages.actionsMessages.alreadyInQueue")));
            return;
        }

        JadGens.getInstance().getPlayersPurgingMachines().add(player);
        HashMap<Integer, Integer> count = new HashMap<>();
        List<Machine> machines = new MachineLookup().getPlayerMachines(player);

        playerPurger = Bukkit.getScheduler().runTaskTimer(JadGens.getInstance(), () -> {
            for (int i = 0; i < this.speed; i++) {
                if (machines.size() == 0) { playerPurger.cancel();playerPurger = null;return; }
                Machine machine = null;
                try { machine = machines.get(0); } catch (IndexOutOfBoundsException ignored) { playerPurger.cancel();playerPurger = null;break; }

                if (machine.getLocation().getBlock() == null || machine.getLocation().getBlock().getType().equals(Material.AIR)) {
                    machines.remove(machine);
                    continue;
                }

                count.put(machine.getType(), count.getOrDefault(machine.getType(), 0) + 1);

                if (machine.getId() == null) return;
                data().set("machines." + machine.getId(), null);

                JadGens.getInstance().getBlocksRemover().getBlocks().add(machine.getLocation().getBlock());
                machines.remove(machine);
            }
        }, 5, 20);


        if (giveBack) {
            playerGiveBack = Bukkit.getScheduler().runTaskTimer(JadGens.getInstance(), () -> {

                if (playerPurger == null) {

                    if (!Bukkit.getOfflinePlayer(player).isOnline()) {
                        JadGens.getInstance().getPlayersPurgingMachines().remove(player);
                        JadGens.getInstance().getPlayersWhoDisconnectedWhilePurgingMachines().put(player, count);
                        playerGiveBack.cancel();
                        playerGiveBack = null;
                        return;
                    }

                    for (int type : count.keySet()) {
                        ItemStack item = new Machine().createItem(type);
                        item.setAmount(count.get(type));
                        Bukkit.getPlayer(player).getLocation().getWorld().dropItemNaturally(Bukkit.getPlayer(player).getLocation(), item);
                    }

                    JadGens.getInstance().getPlayersPurgingMachines().remove(player);
                    Bukkit.getPlayer(player).sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getLangFile().lang().getString("messages.actionsMessages.purgedOwnMachines")));
                    playerGiveBack.cancel();
                    playerGiveBack = null;
                    return;

                }

            }, 10, 8);
        }
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

        mac.getLocation().getChunk().load();
        mac.getLocation().getBlock().setType(Material.AIR);
        mac.getLocation().getChunk().unload(true);
    }

    public FileConfiguration data() {
        return JadGens.getInstance().getDataFile().data();
    }


}
