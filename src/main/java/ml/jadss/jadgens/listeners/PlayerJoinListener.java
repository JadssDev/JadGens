package ml.jadss.jadgens.listeners;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.utils.Machine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (lang().getBoolean("messages.updateChecker.enabled") && lang().getBoolean("messages.updateChecker.notifyOnJoin")) {
            if (player.hasPermission(lang().getString("messages.updateChecker.permission")) &&
                    JadGens.getInstance().getUpdateChecker().hasChecked() && !JadGens.getInstance().getUpdateChecker().isUpdated()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.updateChecker.message")));
            }
        }
        if (JadGens.getInstance().getPlayersWhoDisconnectedWhilePurgingMachines().containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.actionsMessages.purgedWhileOffline")));

            HashMap<Integer, Integer> count = JadGens.getInstance().getPlayersWhoDisconnectedWhilePurgingMachines().get(player.getUniqueId());
            JadGens.getInstance().getPlayersWhoDisconnectedWhilePurgingMachines().remove(player.getUniqueId());

            Bukkit.getConsoleSender().sendMessage(String.valueOf(count));

            Bukkit.getScheduler().runTaskLater(JadGens.getInstance(), () -> {
                for (int type : count.keySet()) {
                    ItemStack item = new Machine().createItem(type);
                    item.setAmount(count.get(type));
                    player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
                    Bukkit.getConsoleSender().sendMessage("Dropping " + type + "; Amount: " + count.get(type));
                }
            }, 45);
        }
    }


    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
