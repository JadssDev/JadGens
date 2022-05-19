package dev.jadss.jadgens.api;

import dev.jadss.jadgens.JadGens;
import dev.jadss.jadgens.api.config.interfaces.GeneralConfiguration;
import dev.jadss.jadgens.api.config.interfaces.LoadedFuelConfiguration;
import dev.jadss.jadgens.api.config.interfaces.LoadedMachineConfiguration;
import dev.jadss.jadgens.api.machines.MachineInstance;
import dev.jadss.jadgens.api.machines.MachinesList;
import dev.jadss.jadgens.api.player.MachinesUser;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * Represents the API of JadGens!
 */
public interface MachinesAPI extends UnsafeMachinesAPI {

    /**
     * Get the instance of JadGens API!
     * @return the instance!
     */
    static MachinesAPI getInstance() {
        return JadGens.getInstance().getManager();
    }

    /**
     * Get a machine!
     * @param id the id of the machine!
     * @return the Machine, or null if it doesn't exist!
     */
    MachineInstance getMachine(String id);

    /**
     * Get a machine by using its location.
     * @param location the location of the machine!
     * @return the Machine, or null if it doesn't exist!
     */
    MachineInstance getMachine(Location location);

    /**
     * Create a machine.
     * @param configuration the configuration of the machine to create with!
     * @param owner the owner of the machine! Can be null, the machine will be accessible to everyone!
     * @param location the location the machine should be created at!
     * @return the Machine created! may be null if the machine couldn't be created!
     */
    MachineInstance createMachine(LoadedMachineConfiguration configuration, UUID owner, Location location);

    //Configurations

    /**
     * Gets configuration of the plugin, such as if the machines should get removed if they are invalid!
     * @return the {@link GeneralConfiguration} object!
     */
    GeneralConfiguration getGeneralConfiguration();

    /**
     * Get a machine configuration by its name.
     * @param type the configuration name.
     * @return the {@link LoadedMachineConfiguration}, or null if it doesn't exist!
     */
    LoadedMachineConfiguration getMachineConfiguration(String type);

    /**
     * Get a fuel configuration by its name.
     * @param type the configuration name.
     * @return the {@link LoadedFuelConfiguration}, or null if it doesn't exist!
     */
    LoadedFuelConfiguration getFuelConfiguration(String type);

    /**
     * Checks if a machine configuration name is REGISTERED.
     * @param type the machine configuration name!
     * @return true if the machine configuration is registered, false otherwise!
     */
    boolean isMachineConfigurationExistent(String type);

    /**
     * Checks if a fuel configuration name is REGISTERED.
     * @param type the fuel configuration name!
     * @return true if the fuel configuration is registered, false otherwise!
     */
    boolean isFuelConfigurationExistent(String type);

    //User thingies

    /**
     * Get a player by his UUID!
     * @param player the UUID of the player!
     * @return The {@link MachinesUser} object with the player information!
     */
    MachinesUser getPlayer(UUID player);

    /**
     * Get all the players with machine information, please note JadGens creates information for everyone which joined at least once the server!
     * <p>Note: this list will be very big due to how JadGens stores players!</p>
     * @return the players in a {@link List}!
     */
    List<MachinesUser> getPlayers();

    //Getters

    /**
     * Get the {@link MenusManager}, so you can open menus like shop for a player...
     * @return A {@link MenusManager}!
     */
    MenusManager getMenuManager();

    ///Fuel

    /**
     * Check if the item is a fuel!
     * @param item the item to check!
     * @return true if the item is a fuel, false otherwise!
     */
    boolean isFuel(ItemStack item);

    /**
     * Get the {@link LoadedFuelConfiguration} object by an item!
     * @param item the item to get the configuration from!
     * @return the {@link LoadedFuelConfiguration} object, null if it doesn't exist!
     */
    LoadedFuelConfiguration getFuelConfigurationByItem(ItemStack item);

    ///Machine

    /**
     * Check if the item is a machine!
     * @param item the item to check!
     * @return true if the item is a machine, false otherwise!
     */
    boolean isMachine(ItemStack item);

    /**
     * Get the {@link LoadedMachineConfiguration} object by an item!
     * @param item the item to get the configuration from!
     * @return the {@link LoadedMachineConfiguration} object, null if it doesn't exist!
     */
    LoadedMachineConfiguration getMachineConfigurationByItem(ItemStack item);

    //Lists

    /**
     * Get all the machine configurations {@link MachinesAPI} knows about!
     * @return the list of machine configurations!
     */
    List<LoadedMachineConfiguration> getMachineConfigurations();

    /**
     * Get all the fuel configurations {@link MachinesAPI} knows about!
     * @return the list of fuel configurations!
     */
    List<LoadedFuelConfiguration> getFuelConfigurations();

    /**
     * Get all machines {@link MachinesAPI} knows about!
     * @return the list of machines!
     */
    MachinesList getMachines();

    /**
     * Calls upon JadGens to save everything, player data, machine data everything!
     */
    void save();
}
