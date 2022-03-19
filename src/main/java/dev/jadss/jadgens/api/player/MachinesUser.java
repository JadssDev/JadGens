package dev.jadss.jadgens.api.player;

import dev.jadss.jadgens.api.config.generalConfig.limits.LimitGroup;
import dev.jadss.jadgens.api.config.interfaces.LoadedMachineConfiguration;
import dev.jadss.jadgens.api.config.playerConfig.PlayerInformation;
import dev.jadss.jadgens.api.machines.MachineInstance;
import dev.jadss.jadgens.api.machines.MachinesList;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

/**
 * Represents a user of the plugin.
 */
public interface MachinesUser {

    /**
     * Get the player's UUID!
     * @return the player UUID!
     */
    UUID getPlayer();

    //Machines..
    /**
     * Get the generators this Player has!
     * @return the list of generators he has!
     */
    MachinesList getMachines();

    /**
     * Checks if this user is allowed to place more machines and hasn't reached the limit.
     * @return true if the player can place more machines, false if the player has reached their limit!
     */
    boolean canPlaceMachines();

    /**
     * Gets the {@link LimitGroup} this player is in.
     * @return the {@link LimitGroup} this player is in
     * <p>Note: Returns null if the LimitGroup of this user is the default one!</p>
     */
    LimitGroup getLimitGroup();

    /**
     * Get how many machines in total this player can have!
     * @return the amount of machines this player can have!
     */
    int getMaximumMachines();

    /**
     * Create a machine for this user.
     * @param configuration the configuration of the machine to create with!
     * @param location the location the machine should be created at!
     * @return the Machine created! may be null if the machine couldn't be created!
     */
    MachineInstance createMachine(LoadedMachineConfiguration configuration, Location location);

    //Drops
    /**
     * Get the information about the player's machine drops!
     * @param configuration the Configuration of the machine to get the information from.
     * @return the information about the player's specified machine drops!
     */
    UserMachineDrops getDropInformation(LoadedMachineConfiguration configuration);

    /**
     * Gets the information about all the player's machine drops!
     * @return the information about all the player's machine drops!
     */
    List<UserMachineDrops> getAllDropsInformation();

    //XP
    /**
     * Gets the XP to collect, this is, when a player is not online, and they have xp to collect, it is stored, so it is not wasted a process to get it.
     * @return the xp to collect!
     */
    int getXpToCollect();

    /**
     * Sets the XP to collect, this is, when a player is not online, and they have xp to collect, it is stored, so it is not wasted a process to get it.
     * @param amount the amount to set to.
     * @return the new amount of xp to collect!
     */
    int setXpToCollect(int amount);

    //SAVING!

    /**
     * Save the information about the player into a {@link PlayerInformation} object.
     * @return the information about the player!
     */
    PlayerInformation save();
}
