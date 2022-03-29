package dev.jadss.jadgens.hooks;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook implements Hook {

    private boolean triedHooking;
    private Economy economy;
    private boolean isAvailable;

    @Override
    public String getName() {
        return "vault";
    }

    @Override
    public String getDisplayName() {
        return "Vault";
    }

    @Override
    public String getDisplayNameWithColors() {
        return "&aVault";
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public Object getHook() {
        return economy;
    }

    @Override
    public boolean hook(Server server) {
        if(triedHooking) return false;
        triedHooking = true;

        if (server.getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> rsp = server.getServicesManager().getRegistration(Economy.class);
            if(rsp != null)
                return isAvailable = rsp.getProvider() != null;
        }

        return false;
    }

    @Override
    public boolean unhook(Server server) {
        this.economy = null;
        this.triedHooking = false;
        this.isAvailable = false;
        return true;
    }
}
