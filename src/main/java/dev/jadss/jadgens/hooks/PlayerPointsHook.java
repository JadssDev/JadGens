package dev.jadss.jadgens.hooks;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Server;

public class PlayerPointsHook implements Hook {

    private boolean triedHooking = false;
    private PlayerPointsAPI hook;
    private boolean isAvailable;

    @Override
    public String getName() {
        return "playerpoints";
    }

    @Override
    public String getDisplayName() {
        return "PlayerPoints";
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public PlayerPointsAPI getHook() {
        return hook;
    }

    @Override
    public boolean hook(Server server) {
        if(triedHooking) return false;
        triedHooking = true;

        if (server.getPluginManager().getPlugin("PlayerPoints") != null) {
            hook = PlayerPoints.getInstance().getAPI();
            return isAvailable = hook != null;
        }

        return false;
    }

    @Override
    public boolean unhook(Server server) {
        this.hook = null;
        this.triedHooking = false;
        this.isAvailable = false;
        return true;
    }
}
