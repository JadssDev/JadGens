package ml.jadss.jadgens.tasks;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.events.ProduceEvent;
import ml.jadss.jadgens.events.MachineProduceEvent;
import ml.jadss.jadgens.utils.Compatibility;
import ml.jadss.jadgens.utils.Machine;
import ml.jadss.jadgens.utils.PurgeMachines;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;
import java.util.UUID;


public class ProduceRunnable implements Runnable {

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
                    produceMachineStuff(id, type);
                }
            } else {
                produceMachineStuff(id, type);
            }
        }
        JadGens.getInstance().getDataFile().saveData();
    }

    private void produceMachineStuff(String id, int type) {
        //////////////////////////////////////////////

        MachineProduceEvent machineProduceEvent = new MachineProduceEvent(new Machine(id));
        JadGens.getInstance().getServer().getPluginManager().callEvent(machineProduceEvent);
        if (machineProduceEvent.isCancelled()) return;

        if (JadGens.getInstance().getConfig().getBoolean("machinesConfig.stopProducingIfOffline")) {
            Player testOnline = Bukkit.getPlayer(UUID.fromString(data().getString("machines." + id + ".owner")));
            if (testOnline == null) return;
        }

        if (JadGens.getInstance().getConfig().getBoolean("machines." + type + ".dropItems.enabled")) {
            World wl = Bukkit.getServer().getWorld(data().getString("machines." + id + ".world"));
            if (wl == null) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eThe &3&lMachine &eWith &b&lID &3" + id + " &eWas &c&lNot &b&lFound&e!"));
                return;
            }
            Location location = new Location(wl,
                    data().getInt("machines." + id + ".x"),
                    data().getInt("machines." + id + ".y"),
                    data().getInt("machines." + id + ".z"));
            location.setY(location.getBlockY() + 1);

            ItemStack dropItem = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("machines." + type + ".dropItems.material")),
                    JadGens.getInstance().getConfig().getInt("machines." + type + ".dropItems.amount"),
                    (short) JadGens.getInstance().getConfig().getInt("machines." + type + ".dropItems.damage"));
            wl.dropItem(location, dropItem);
        }

        if (JadGens.getInstance().getConfig().getBoolean("machines." + type + ".commands.enabled")) {
            Player pl = Bukkit.getPlayer(UUID.fromString(data().getString("machines." + id + ".owner")));
            if (!(pl == null)) {
                for (String command : JadGens.getInstance().getConfig().getStringList("machines." + type + ".commands.commands")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%owner%", pl.getName()));
                }
            }
        }

        if (JadGens.getInstance().getConfig().getBoolean("machines." + type + ".economy.enabled") && JadGens.getInstance().isHookedVault()) {
            OfflinePlayer pl = Bukkit.getOfflinePlayer(UUID.fromString(data().getString("machines." + id + ".owner")));
            JadGens.getInstance().getEco().depositPlayer(pl, JadGens.getInstance().getConfig().getDouble("machines." + type + ".economy.give"));
        }

        if (JadGens.getInstance().getConfig().getBoolean("machines." + type + ".points.enabled") && JadGens.getInstance().isHookedPlayerPoints()) {
            UUID player = UUID.fromString(data().getString("machines." + id + ".owner"));
            JadGens.getInstance().getPointsAPI().give(player, JadGens.getInstance().getConfig().getInt("machines." + type + ".points.give"));
        }

        if (JadGens.getInstance().getConfig().getBoolean("machines." + type + ".exp.enabled")) {
            Player onlinePL = Bukkit.getPlayer(UUID.fromString(data().getString("machines." + id + ".owner")));
            if (onlinePL == null) return;
            onlinePL.setLevel(onlinePL.getLevel()+JadGens.getInstance().getConfig().getInt("machines." + type + ".exp.givelevels"));
        }
        //////////////////////////////////////////////
    }
    
    protected FileConfiguration data() {
        return JadGens.getInstance().getDataFile().data();
    }
    
}
