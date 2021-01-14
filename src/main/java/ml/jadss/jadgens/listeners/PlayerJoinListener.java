package ml.jadss.jadgens.listeners;

import ml.jadss.jadgens.JadGens;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (lang().getBoolean("messages.updateChecker.enabled") && lang().getBoolean("messages.updateChecker.notifyOnJoin"))
            if (e.getPlayer().hasPermission(lang().getString("messages.updateChecker.permission")) &&
                    JadGens.getInstance().getUpdateChecker().hasChecked() && !JadGens.getInstance().getUpdateChecker().isUpdated())
                        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.updateChecker.message")));
    }


    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
