package dev.jadss.jadgens.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * PlaceholderAPI hook.
 */
public class PlaceholderAPIHook implements Hook {

    private static String version;

    private boolean triedHooking;
    private PlaceholderExpansion expansion;
    private boolean isAvailable;

    @Override
    public String getName() {
        return "placeholderapi";
    }

    @Override
    public String getDisplayName() {
        return "PlaceholderAPI";
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public PlaceholderExpansion getHook() {
        return expansion;
    }

    @Override
    public boolean hook(Server server) {
        if (triedHooking) return false;
        triedHooking = true;

        version = server.getPluginManager().getPlugin("JadGens").getDescription().getVersion();

        if(server.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            expansion = new Expansion();
            expansion.register();
            isAvailable = true;
        }

        return true;
    }

    @Override
    public boolean unhook(Server server) {
        try {
            expansion.unregister();
            this.triedHooking = false;
            this.isAvailable = false;
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static class Expansion extends PlaceholderExpansion {

        @Override
        public @NotNull String getIdentifier() {
            return "jadgens";
        }

        @Override
        public @NotNull String getAuthor() {
            return "jadss";
        }

        @Override
        public @NotNull String getVersion() {
            return version;
        }

        @Override
        public String onPlaceholderRequest(final Player player, @NotNull final String params) {
            return "N/A";
        }
    }
}
