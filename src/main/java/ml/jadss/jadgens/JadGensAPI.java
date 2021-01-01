package ml.jadss.jadgens;

import ml.jadss.jadgens.utils.Fuel;
import ml.jadss.jadgens.utils.Machine;
import ml.jadss.jadgens.utils.MachineLimiter;
import ml.jadss.jadgens.utils.MachineLookup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

@SuppressWarnings("unused")
public class JadGensAPI {

    private Fuel fuel = new Fuel();
    private Machine mac = new Machine();
    private MachineLimiter limiter = new MachineLimiter();
    private MachineLookup lookup = new MachineLookup();
    private static String[] versions = new String[] {"1.0"} ;
    private static String[] invalidNames = new String[] {"Vault", "PlaceHolderAPI", "Essentials", "PermissionsEx", "LuckPerms", "PermissionsEx"};
    private JavaPlugin plugin;
    private String pluginName;
    private String apiVersion;
    private boolean validAPI;

    /**
     * Summon the API so you can check for machines etc.<p>
     * If the validation fails, the API will not work! Use: isValid() to check the validation.<p>
     * I recommend creating this just once and saving somewhere.
     *
     * @param plugin Specifies the plugin that is calling the API.
     * @param apiVersion Version API of JadGens. (All version are supported =D )
     */
    public JadGensAPI(JavaPlugin plugin, String apiVersion) {
        if (plugin != null && apiVersion != null) {

            boolean isValidPlugin = true;
            boolean isValidAPIVersion = false;
            String pluginName = plugin.getDescription().getName();

            //check if the name is invalid
            for (String s : invalidNames) { if (s.equalsIgnoreCase(pluginName)) { isValidPlugin = false; } }
            for (String v : versions) { if (v.equalsIgnoreCase(apiVersion)) { isValidAPIVersion = true; } }

            if (!isValidPlugin) { Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eA plugin tried using a very known plugin but failed. (" + pluginName + ")"));this.plugin = null;this.pluginName = null;this.pluginName = null;this.validAPI = false;return; }
            if (!isValidAPIVersion) { Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eA plugin tried using an Invalid API Version but failed. (" + apiVersion + ")"));this.plugin = null;this.pluginName = null;this.pluginName = null;this.validAPI = false;return; }

            this.plugin = plugin;
            this.pluginName = pluginName;
            this.apiVersion = apiVersion;
            this.validAPI = true;

            if (JadGens.getInstance().isAPIDebugEnabled()) { logAPI("&3JadGens &7>> &eAPI Created! ( Name: " + this.pluginName + "; APIVersion: " + this.apiVersion); }
        } else {
            if (JadGens.getInstance().isAPIDebugEnabled()) { logAPI("&3JadGens &7>> &eA plugin tried to &acreate &ethe &3&lAPI &eusing a &cinvalid plugin&e/&cinvalid API Version&e!"); }

            this.plugin = null;
            this.pluginName = null;
            this.apiVersion = null;
            this.validAPI = false;
        }
    }

    public static boolean validatePlugin(Plugin pl) {
        if (pl != null) {
            boolean isValidPlugin = true;
            for (String s : invalidNames) { if (s.equalsIgnoreCase(pl.getDescription().getName())) { isValidPlugin = false; } }
            return isValidPlugin;
        }
        return false;
    }

    /**
     * API Logger.
     * @param msg API Message
     */
    protected void logAPI(String msg) {
        if (!getValidation()) return;
        if (!JadGens.getInstance().isAPIDebugEnabled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }

    /**
     * Get a machine's item using the type. (contains nbt)
     * @param type What machine type item to create?
     * @return A machine's ItemStack, with nbt tags etc.
     */
    public ItemStack getMachineItem(int type) {
        if (!getValidation()) return null;
        if (getApiVersion().equals("1.0")) {
            logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eCreated Machine item with id &b" + type);
            return mac.createItem(type);
        }
        return null;
    }

    /**
     * Get a fuel item using the type. (contains nbt)
     * @param type What machine type item to create?
     * @return A machine's ItemStack, with nbt tags etc.
     */
    public ItemStack getFuelItem(int type) {
        if (!getValidation()) return null;
        if (getApiVersion().equals("1.0")) {
            logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eCreated Fuel item with id &b" + type);
            return fuel.createItem(type);
        }
        return null;
    }

    /**
     * Check if an item is a fuel.<p>
     * Results:<p>
     * -2 - API Is null!<p>
     * -1 - Item is null.<p>
     *  0 - Not a fuel.
     *
     * @param item The item to check if it is a fuel.
     * @return What type the fuel is.
     */
    public int isFuel(ItemStack item) {
        if (item == null) return -1;
        if (!getValidation()) return -2;
        if (getApiVersion().equals("1.0")) {
            logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eChecking if an Item is a fuel...");
            if (fuel.isFuelItem(item)) {
                Fuel f = new Fuel(item);
                return f.getType();
            }
            return 0;
        }
        return -1;
    }

    /**
     * Check for how many machines a player has.
     * @param player The player to check the machine limit.
     * @return The limit of machines that the player can have.
     */
    public int getMachinesLimit(Player player) {
        if (player == null) return 0;
        if (!getValidation()) return 0;
        if (getApiVersion().equals("1.0")) {
            logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting machines limit for &a" + player.getName() + "&e...");
            return limiter.getMaxLimit(player);
        }
        return 0;
    }


    /**
     * Check for how many machines left the player can place.<p>
     * If the player can place infinite machines, it will return "-1"
     * @param player The player to check the machines left.
     * @return The machines that a player can place. (-1 if infinite)
     */
    public int getPlayerMachinesLeft(Player player) {
        if (player == null) return 0;
        if (!getValidation()) return 0;
        if (getApiVersion().equals("1.0")) {
            logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting machines left for &a" + player.getName() + "&e...");
            return limiter.getMachinesLeft(player);
        }
        return 0;
    }

    /**
     * Check if the player can place more machines.<p>
     * true - Can place more machines<p>
     * false - Can't place any more machines
     * @param player The player to check if he can place more machines
     * @return If the player can place more machines.
     */
    public boolean hasMachinesLeft(Player player) {
        if (player == null) return false;
        if (!getValidation()) return false;
        if (getApiVersion().equals("1.0")) {
            logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting if &a" + player.getName() + "&e can place more machines...");
            return limiter.canPlaceMachine(player);
        }
        return false;
    }

    /**
     * Gets how many machines the player has.
     * @param player_uuid The player's uuid to check how many machines
     * @return Total machines of the player.
     */
    public int getMachinesCount(UUID player_uuid) {
        if (player_uuid == null) return 0;
        if (!getValidation()) return 0;
        if (getApiVersion().equals("1.0")) {
            logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting machines of &a" + player_uuid + "&e... &f(Player UUID)");
            return lookup.getMachines(player_uuid);
        }
        return 0;
    }

    /**
     * Gets how many machines the player has.
     * @param player The player object to check how many machines
     * @return Total machines of the player.
     */
    public int getMachinesCount(Player player) {
        if (player == null) return 0;
        if (!getValidation()) return 0;
        if (getApiVersion().equals("1.0")) {
            logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting machines of &a" + player.getName() + "&e...");
            return lookup.getMachines(player.getUniqueId());
        }
        return 0;
    }

    /**
     * Gets how many machines the player has.
     * @param player The player object to check how many machines
     * @param machine_type The machine type to look for
     * @return Total machines of the type specified that the owner is the player.
     */
    public int getMachinesCount(Player player, int machine_type) {
        if (player == null) return 0;
        if (!getValidation()) return 0;
        if (getApiVersion().equals("1.0")) {
            logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting machines of &a" + player.getName() + "&e with &btype id " + machine_type + "&e...");
            return lookup.getMachines(player.getUniqueId(), machine_type);
        }
        return 0;
    }

    /**
     * Gets how many machines the player has.
     * @param player_uuid The player's uuid to check how many machines
     * @param machine_type The machine type to look for
     * @return Total machines of the type specified that the owner is the player.
     */
    public int getMachinesCount(UUID player_uuid, int machine_type) {
        if (player_uuid == null) return 0;
        if (!getValidation()) return 0;
        if (getApiVersion().equals("1.0")) {
            logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting machines of &a" + player_uuid + "&e with &btype id " + machine_type + "&e... &f(Player UUID)");
            return lookup.getMachines(player_uuid, machine_type);
        }
        return 0;
    }

    /**
     * Check if the specified block/location/coordinates is a machine<P>
     * true - It is a machine.<p>
     * false - It is not a machine.
     * @param block The block to check if it is a Machine.
     * @return If the block is a machine or not.
     */
    public boolean checkMachine(Block block) {
        if (block == null) return false;
        if (!getValidation()) return false;
        if (getApiVersion().equals("1.0")) {
            logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting if a location is a machine... &f(&b" + block.getLocation().getBlockX() + "&f,&b " + block.getLocation().getBlockY() + "&f,&b " + block.getLocation().getBlockZ() + "&f)");
            return lookup.isMachine(block);
        }
        return false;
    }

    /**
     * Check if the specified block/location/coordinates is a machine<P>
     * true - It is a machine.<p>
     * false - It is not a machine.
     * @param machine_check_location The location to check if it is a Machine.
     * @return If the location is a machine or not.
     */
    public boolean checkMachine(Location machine_check_location) {
        if (machine_check_location == null) return false;
        if (!getValidation()) return false;
        if (getApiVersion().equals("1.0")) {
            logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting if a location is a machine... &f(&b" + machine_check_location.getBlockX() + "&f,&b " + machine_check_location.getBlockY() + "&f,&b " + machine_check_location.getBlockZ() + "&f)");
            return lookup.isMachine(machine_check_location);
        }
        return false;
    }

    /**
     * Check if the specified block/location/coordinates is a machine<P>
     * true - It is a machine.<p>
     * false - It is not a machine.
     * @param world The world to check.
     * @param x The x Coordinate to check.
     * @param y The y Coordinate to check.
     * @param z The z Coordinate to check.
     * @return If the location is a machine or not.
     */
    public boolean checkMachine(World world, int x, int y, int z) {
        if (world == null) return false;
        if (!getValidation()) return false;
        if (getApiVersion().equals("1.0")) {
            logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting if a location is a machine... &f(&b" + x + "&f,&b " + y + "&f,&b " + z + "&f)");
            return lookup.isMachine(world, x, y, z);
        }
        return false;
    }

    /**
     * Check if the api is valid.
     * @return If the api is valid
     */
    public boolean isValid() { return validAPI; }

    private boolean getValidation() { return validAPI; }

    private JavaPlugin getPlugin() { return plugin; }
    /**
     * Check the plugin's name.
     * @return The API Version.
     */
    private String getPluginName() { return pluginName; }
    /**
     * Check if the API Version
     * @return The API Version.
     */
    private String getApiVersion() { return apiVersion; }
}
