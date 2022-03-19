package dev.jadss.jadgens.hooks;

import org.bukkit.Server;

/**
 * Represents a hook from JadGens to another plugin!
 */
public interface Hook {

    /**
     * Gets the name of the hook!
     * @return the name of the hook!
     */
    String getName();

    /**
     * The name to display when it was successfully hooked!
     * @return the name to display.
     */
    String getDisplayName();

    /**
     * Check if this hook succeeded.
     * @return if the hook succeeded.
     */
    boolean isAvailable();

    /**
     * Get the hook of the other API.
     * @return the hook of the other API.
     */
    Object getHook();

    /**
     * Attempt to hook!
     * @param server the server instance for hooking.
     * @return if the hook succeeded.
     */
    boolean hook(Server server);

    /**
     * Attempts to unhook this hook from the specified plugin!
     * @param server the server instance for unhooking.
     * @return if unhooking was successful.
     */
    boolean unhook(Server server);
}
