package ml.jadss.jadgens.tasks;

import ml.jadss.jadgens.JadGens;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class BlocksRemover {

    private BukkitTask task;
    private int speed = 10;
    private final List<Block> blocks = new ArrayList<>();
    private final List<Chunk> chunks = new ArrayList<>();
    private boolean SUPERMODE = false;

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
                if (block != null) {
                    if (!block.getLocation().getChunk().isLoaded()) {
                        block.getLocation().getChunk().load();
                        Bukkit.getConsoleSender().sendMessage("Loaded A chunk.");
                        chunks.add(block.getLocation().getChunk());
                    }
                    block.setType(Material.AIR);
                }
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
        if (JadGens.getInstance().getConfig().getBoolean("machinesConfig.logRemovalTasks")) {

            int failed = 0;
            int amount = chunks.size();
            Bukkit.getConsoleSender().sendMessage("amount: " + amount + "; failed = " + failed);
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens&e(&c&lRemoval&e) &7>> &eUnloading &b&laffected &3chunks&e..."));
            for(Chunk chunk : chunks) {
                chunks.remove(chunk);
                if (!chunk.unload(true, true)) failed++;
            }

            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&3&lJadGens&e(&c&lRemoval&e) &7>> &b&lUnloaded &f" + (amount-failed) + " &3chunks&e, &c&lfailed &eto &b&lunload &f" + failed + " &3chunks&e. " + "(&e&3Total&e: &f" + amount + "&e)"));


            if (reason.equalsIgnoreCase("BlocksIs0"))
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens&e(&c&lRemoval&e) &7>> &eTask &c&lStopped&e. &e(&bBlocks to purge is 0&e)"));
            else if (reason.equalsIgnoreCase("ShuttingDown"))
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens&e(&c&lRemoval&e) &7>> &eTask &c&lStopped&e. &e(&bServer is shutting down&e)"));
            else
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens&e(&c&lRemoval&e) &7>> &eTask &c&lStopped&e. &e(&bNo specific reason&e)"));
        }
    }

    public void updateStatus(List<Block> list, boolean warn) {
        if (list != null) this.blocks.addAll(list);

        if (!SUPERMODE) calculateSpeed();

        if (task != null) return;
        else if (this.blocks.size() != 0) startTask();

        if (JadGens.getInstance().getConfig().getBoolean("machinesConfig.logRemovalTasks") && warn)
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens&e(&c&lRemoval&e) &7>> &eTask &b&lUpdated&e. &e(&b&lSpeed: &3&l" + this.speed + "&e)"));

    }

    private void calculateSpeed() {
        if (this.blocks.size() < 10) this.speed = 5;
        else if (this.blocks.size() < 50) this.speed = this.blocks.size() / 2;
        else if (this.blocks.size() < 100) this.speed = this.blocks.size() / 3;
        else if (this.blocks.size() < 500) this.speed = this.blocks.size() / 5;
        else if (this.blocks.size() < 1000) this.speed = this.blocks.size() / 10;
        else if (this.blocks.size() < 2000) this.speed = this.blocks.size() / 20;
        else this.speed = 200;

        assert false;
        if (this.speed == 0 || this.speed == 1) this.speed = 10;
    }

    public void goSuperSpeedBrrrrrrrrrrrrr() {
        if (task != null) {
            stopTask("ShuttingDown");
            this.speed = 99999999;

            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &3&lShutdown asked, Interrupting main thread to finish blocks remover."));
            Thread.currentThread().interrupt();

            while(this.blocks.size() != 0) {
                Block block = null;
                try { block = this.blocks.get(0); } catch(IndexOutOfBoundsException ignored) { break; }
                if (block != null) block.setType(Material.AIR);
                this.blocks.remove(block);
            }

            try {
                Thread.sleep(0);
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eResuming execution."));
            } catch(InterruptedException ex) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eReceived InterruptedException, resuming execution."));
            }
        }
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
