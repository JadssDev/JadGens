package ml.jadss.jadgens.tasks;

import ml.jadss.jadgens.JadGens;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class BlocksRemover {

    private BukkitTask task;
    private int speed = 10;
    private List<Block> blocks = new ArrayList<>();

    private void startTask() {
        if (this.blocks.size() == 0) { stopTask("BlocksIs0"); return; }
        calculateSpeed();
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens&e(&c&lRemoval&e) &7>> &eTask &b&lUpdated&e. &e(&b&lSpeed: &3&l" + this.speed + "&e)"));
        if (JadGens.getInstance().getConfig().getBoolean("machinesConfig.logRemovalTasks"))
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens&e(&c&lRemoval&e) &7>> &eTask &a&lStarted &ewith &b&lSpeed &3" + this.speed + "&e!"));
        task = Bukkit.getScheduler().runTaskTimer(JadGens.getInstance(), () -> {
            if (this.blocks.size() == 0) { stopTask("BlocksIs0"); return; }
            for (int i = 0; i < this.speed; i++) {
                Block block = null;
                try { block = this.blocks.get(0); } catch(IndexOutOfBoundsException ignored) { stopTask("BlocksIs0"); return; }
                if (block != null) block.setType(Material.AIR);
                this.blocks.remove(block);
            }
            calculateSpeed();
            if (this.blocks.size() == 0) {
                stopTask("BlocksIs0");
                return;
            }
        }, 100, 50);
    }

    private void stopTask(String reason) {
        if (task != null) task.cancel();
        task = null;
        if (JadGens.getInstance().getConfig().getBoolean("machinesConfig.logRemovalTasks"))
            if (reason.equalsIgnoreCase("BlocksIs0"))
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens&e(&c&lRemoval&e) &7>> &eTask &c&lStopped&e. &e(&bBlocks to purge is 0&e)"));
            else
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens&e(&c&lRemoval&e) &7>> &eTask &c&lStopped&e. &e(&bNo specific reason&e)"));
    }

    public void updateStatus(List<Block> list, boolean warn) {
        if (list != null) this.blocks.addAll(list);

        calculateSpeed();

        if (task != null) return;
        else if (this.blocks.size() != 0) startTask();

        if (JadGens.getInstance().getConfig().getBoolean("machinesConfig.logRemovalTasks") && warn)
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens&e(&c&lRemoval&e) &7>> &eTask &b&lUpdated&e. &e(&b&lSpeed: &3&l" + this.speed + "&e)"));

    }

    private void calculateSpeed() {
        if (this.blocks.size() < 10) this.speed = 5;
        else if (this.blocks.size() < 50) this.speed = this.blocks.size() / 2;
        else if (this.blocks.size() < 100) this.speed = this.blocks.size() / 4;
        else if (this.blocks.size() < 500) this.speed = this.blocks.size() / 8;
        else if (this.blocks.size() < 1000) this.speed = this.blocks.size() / 15;
        else if (this.blocks.size() < 2000) this.speed = this.blocks.size() / 25;
        else this.speed = 200;

        assert false;
        if (this.speed == 0 || this.speed == 1) this.speed = 10;
    }

    public List<Block> getBlocks() { return blocks; }
    public int getSpeed() { return speed; }
}


//    public void shortys_Start() { // yikes there we go
//        if (task == null) {
//            System.out.println("Starting removal of some blocks...");
//            AtomicInteger blocksRemoved = new AtomicInteger();
//            List<Block> blocks = new ArrayList<>(MachinePurger.blocksList);
//            int expected = blocks.size();
//            task = Bukkit.getScheduler().runTaskTimer(JadGens.getInstance(), () -> { //uhhh... what does the ? and : mean xD if else
//                blocks.stream().skip(blocksRemoved.get()).limit(blocks.size() > 20 ? blocks.size() / 4 : blocks.size()).forEach(block -> {
//                    block.setType(Material.AIR);
//                    blocksRemoved.getAndIncrement();
//                });
//                if (expected <= blocksRemoved.get())
//                    shortys_Stop();
//            }, 0, 5);
//        } else {
//            shortys_Start();
//        }
//    }
//
//    public void shortys_Stop() {
//        System.out.println("Done.");
//        task.cancel();
//        task = null;
//    }
//}
