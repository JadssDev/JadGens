package ml.jadss.jadgens;

import com.cryptomorin.xseries.XMaterial;
import ml.jadss.jadgens.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class JadGensAPI {

    private final Fuel fuelChecker = new Fuel();
    private final Machine machineChecker = new Machine();
    private final MachineLimiter limiter = new MachineLimiter();
    private final MachineLookup lookup = new MachineLookup();

    private String[] invalidNames = new String[] {"Vault", "PlaceHolderAPI", "Essentials", "PermissionsEx", "LuckPerms", "PermissionsEx", "JadGens", "urMom"};
    protected static final List<JadGensAPI> apis = new ArrayList<>();

    private JavaPlugin apiOwner;
    private boolean validAPI;

    /**
     * Summon the API so you can check for machines etc.<p>
     * If the validation fails, the API will not work! Use: isValid() to check the validation.<p>
     * I recommend creating this just once and saving somewhere.
     *
     * @param plugin Specifies the plugin that is calling the API.
     */
    public JadGensAPI(JavaPlugin plugin) throws APIException {
        if (plugin != null) {

            //test sh*t
            for (JadGensAPI api : apis)
                if (api.getPluginName().equals(plugin.getName()))
                    throw new APIException("There's already an instance of this JadGensAPI!");

            //validate plugin.
            for (String invalid : invalidNames) {
                if (plugin.getDescription().getName().equals(invalid)) {
                    this.validAPI = false;
                    throw new APIException("A plugin tried to create an API, but it was an invalid name. plis fix.");
                }
            }

            this.apiOwner = plugin;
            this.validAPI = true;

            apis.add(this);
        } else {
            this.apiOwner = null;
            this.validAPI = false;
        }
    }
    
    //depecrated method.
    @Deprecated
    public JadGensAPI(JavaPlugin plugin, String apiVersion) {
        if (plugin != null) {

            //test sh*t
            for (JadGensAPI api : apis)
                if (api.getPluginName().equals(plugin.getName()))
                    throw new APIException("There's already an instance of this JadGensAPI!");

            //validate plugin.
            for (String invalid : invalidNames) {
                if (plugin.getDescription().getName().equals(invalid)) {
                    this.validAPI = false;
                    throw new APIException("A plugin tried to create an API, but it was an invalid name. plis fix.");
                }
            }

            this.apiOwner = plugin;
            this.validAPI = true;

            apis.add(this);
        } else {
            this.apiOwner = null;
            this.validAPI = false;
        }
    }

    /**
     * Get a JadGensAPI thru jadgens itself, =)<p>
     * @param plugin The plugin to get the API.
     * @return A JadGensAPI version, returns null if not found btw.
     */
    public static JadGensAPI getApi(Plugin plugin) {
        for (JadGensAPI api : JadGensAPI.apis) {
            if (api.getApiOwner().equals(plugin)) return api;
        }
        return null;
    }

    public static List<String> getAPIsList() {
        List<String> apisList = new ArrayList<>();
        for(JadGensAPI api : JadGensAPI.apis) apisList.add(api.getPluginName());
        return apisList;
    }

    /**
     * API Logger.
     * @param msg API Message
     */
    protected void logAPI(String msg) {
        if (!isAPIValid()) return;
        if (!JadGens.getInstance().isAPIDebugEnabled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }

    /**
     * Get a machine's item using the type. (contains nbt)<p>
     * @param type What machine type item to create?
     * @return A machine's ItemStack, with nbt tags etc.
     */
    public ItemStack getMachineItem(int type) {
        if (!isAPIValid()) return null;
        logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eCreated Machine item with id &b" + type);
        return machineChecker.createItem(type);

    }

    /**
     * Get a fuel item using the type. (contains nbt)<p>
     * @param type What machine type item to create?
     * @return A machine's ItemStack, with nbt tags etc.
     */
    public ItemStack getFuelItem(int type) {
        if (!isAPIValid()) return null;
        logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eCreated Fuel item with id &b" + type);
        return fuelChecker.createItem(type);
    }

    /**
     * Get the types of machines or fuels.
     * @param whatType What type of types? "machine" or "fuel"?
     * @return the types of machine or fuel.
     */
    public List<Integer> getTypes(String whatType) {
        if (whatType.equalsIgnoreCase("machine")) {
            return machineChecker.getExistingTypes();
        } else if (whatType.equalsIgnoreCase("fuel")) {
            return fuelChecker.getExistingTypes();
        } else throw new APIException("Type is not correct.");
    }

    /**
     * Create a machine in the specific location!<p>
     * Please note, that, the owner can actually be null, it will have no owner, and anyone can break the machine.
     * @param world The world the machine is located.
     * @param x The x of the machine location.
     * @param y blah blah blah
     * @param z blahhhhhhhhhhhhhhhh
     * @param type the Type of the machine, input a non existent machine and I'LL BAN YOU
     * @param owner the owner of the machine, you can place null to make the machine completely public.
     * @return the machine object, it WILL be null if you're dumb.
     */
    public Machine createMachine(World world, int x, int y, int z, int type, UUID owner) {
        if (!machineChecker.typeExists(type)) throw new APIException("Type does not exist.");
        if (world == null) throw new APIException("World is null");

        Machine machine;
        Location location = new Location(world, x, y, z);

        location.getBlock().setType(XMaterial.valueOf(JadGens.getInstance().getConfig().getString("machines." + type + ".MachineBlock.material")).parseMaterial());
        location.getBlock().setData((byte) JadGens.getInstance().getConfig().getInt("machines." + type + ".MachineBlock.damage"));

        if (owner == null) {
            machine = new Machine(location, type, "none");
        } else {
            machine = new Machine(location, type, owner.toString());
        }

        return machine;
    }

    /**
     * Create a machine in the specific location!<p>
     * Please note, that, the owner can actually be null, it will have no owner, and anyone can break the machine.
     * @param location The location the machine is located.
     * @param type the Type of the machine, input a non existent machine and I'LL BAN YOU
     * @param owner the owner of the machine, you can place null to make the machine completely public.
     * @return the machine object, it WILL be null if you're dumb.
     */
    public Machine createMachine(Location location, int type, UUID owner) {
        if (!machineChecker.typeExists(type)) throw new APIException("Type does not exist.");
        if (location == null) throw new APIException("Location is null");

        Machine machine;

        location.getBlock().setType(XMaterial.valueOf(JadGens.getInstance().getConfig().getString("machines." + type + ".MachineBlock.material")).parseMaterial());
        location.getBlock().setData((byte) JadGens.getInstance().getConfig().getInt("machines." + type + ".MachineBlock.damage"));

        if (owner == null) {
            machine = new Machine(location, type, "none");
        } else {
            machine = new Machine(location, type, owner.toString());
        }

        return machine;
    }

    /**
     * Check if an item is a fuel.<p>
     * Results:<p>
     * null - if it is not a fuel or api is invalid.<p>
     * fuel object - if it s a fuel, you can check type etc.
     *
     * @param item The item to check if it is a fuel.
     * @return The fuel object itself.
     */
    public Fuel isFuel(ItemStack item) {
        if (!isAPIValid()) return null;
        if (item == null) return null;

        logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eChecking if an Item is a fuel...");
        if (fuelChecker.isFuelItem(item)) {
            Fuel fuell = new Fuel(item);
            return fuell;
        }
        return null;
    }

    /**
     * Check for how many machines a player can place.<p>
     * Results:<p>
     * -2 - the api is null.<p>
     * -1 - infinite machines<p>
     * other - player's limit.
     * @param player The player to check the machine limit.
     * @return The limit of machines that the player can have.
     */
    public int getMachinesLimit(Player player) {
        if (player == null) return 0;
        if (!isAPIValid()) return -2;

        logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting machines limit for &a" + player.getName() + "&e...");
        return limiter.getMaxLimit(player);
    }


    /**
     * Check for how many machines left the player can place.<p>
     * Results:<p>
     * -2 - the api or player is null.<p>
     * -1 - infinite machines<p>
     * @param player The player to check the machines left.
     * @return The machines that a player can place.
     */
    public int getPlayerMachinesLeft(Player player) {
        if (player == null) return -2;
        if (!isAPIValid()) return -2;

        logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting machines left for &a" + player.getName() + "&e...");
        return limiter.getMachinesLeft(player);
    }

    /**
     * Check if the player can place more machines.<p>
     * true - Can place more machines<p>
     * false - Can't place any more machines or api is invalid or player is invalid.
     * @param player The player to check if he can place more machines
     * @return If the player can place more machines.
     */
    public boolean hasMachinesLeft(Player player) {
        if (player == null) return false;
        if (!isAPIValid()) return false;

        logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting if &a" + player.getName() + "&e can place more machines...");
        return limiter.canPlaceMachine(player);
    }

    /**
     * Gets how many machines the player has.
     * Results:<p>
     * -2 the api or player is null.<p>
     * -1 infinite machines<p>
     * @param player_uuid The player's uuid to check how many machines
     * @return Total machines of the player.
     */
    public int getMachinesCount(UUID player_uuid) {
        if (player_uuid == null) return -2;
        if (!isAPIValid()) return -2;

        logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting machines of &a" + player_uuid + "&e... &f(Player UUID)");
        return lookup.getPlayerMachineCount(player_uuid);

    }

    /**
     * Gets how many machines the player has.
     * Results:<p>
     * -2 the api or player is null.<p>
     * other - total machines of player. <p>
     * @param player The player to check how many machines
     * @return Total machines of the player.
     */
    public int getMachinesCount(Player player) {
        if (player == null) return -2;
        if (!isAPIValid()) return -2;

        logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting machines of &a" + player.getName() + "&e...");
        return lookup.getPlayerMachineCount(player.getUniqueId());
    }

    /**
     * Gets how many machines the player has.
     * Results:<p>
     * null - API Invalid. <p>
     * List of Machines - Get all machines <p>
     * @return Total machines of the player.
     */
    public List<Machine> getAllMachines() {
        if (!isAPIValid()) return null;

        logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting &ball &3&lmachines&e...");
        return lookup.getAllMachines();
    }

    /**
     * Gets how many machines the player has.
     * Results:<p>
     * -2 - the api or player is null.<p>
     * other - total machines<p>
     * @param player The player's uuid to check how many machines
     * @param machine_type the machine type to check.
     * @return Total machines of the player.
     */
    public int getMachinesCount(Player player, int machine_type) {
        if (player == null) return -2;
        if (!isAPIValid()) return -2;

        logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting machines of &a" + player.getName() + "&e with &btype id " + machine_type + "&e...");
        return lookup.getPlayerMachineCount(player.getUniqueId(), machine_type);
    }

    /**
     * Gets how many machines the player has.<p>
     * Results:<p>
     * -2 - the api or player is null.<p>
     * other - total machines<p>
     * @param player_uuid The player's uuid to check how many machines
     * @param machine_type the machine type to check.
     * @return Total machines of the player.
     */
    public int getMachinesCount(UUID player_uuid, int machine_type) {
        if (player_uuid == null) return -2;
        if (!isAPIValid()) return -2;

        logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting machines of &a" + player_uuid + "&e with &btype id " + machine_type + "&e... &f(Player UUID)");
        return lookup.getPlayerMachineCount(player_uuid, machine_type);
    }

    /**
     * Check if the specified block/location/coordinates is a machine<P>
     * Results:<p>
     * true - It is a machine.<p>
     * false - It is not a machine or API is invalid or block is null.
     * @param block The block to check if it is a Machine.
     * @return If the block is a machine or not.
     */
    public boolean checkMachine(Block block) {
        if (block == null) return false;
        if (!isAPIValid()) return false;

        logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting if a location is a machine... &f(&b" + block.getLocation().getBlockX() + "&f,&b " + block.getLocation().getBlockY() + "&f,&b " + block.getLocation().getBlockZ() + "&f)");
        return lookup.isMachine(block);
    }

    /**
     * Check if the specified block/location/coordinates is a machine<p>
     * true - It is a machine.<p>
     * false - It is not a machine or API is invalid or location is null..
     * @param location The location to check if it is a Machine.
     * @return If the location is a machine or not.
     */
    public boolean checkMachine(Location location) {
        if (location == null) return false;
        if (!isAPIValid()) return false;
        logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting if a location is a machine... &f(&b" + location.getBlockX() + "&f,&b " + location.getBlockY() + "&f,&b " + location.getBlockZ() + "&f)");
        return lookup.isMachine(location);
    }

    /**
     * Check if the specified block/location/coordinates is a machine<p>
     * true - It is a machine.<p>
     * false - It is not a machine or API is invalid or location is null..
     * @param world The world to check.
     * @param x The x Coordinate to check.
     * @param y The y Coordinate to check.
     * @param z The z Coordinate to check.
     * @return If the location is a machine or not.
     */
    public boolean checkMachine(World world, int x, int y, int z) {
        if (world == null) return false;
        if (!isAPIValid()) return false;

        logAPI("&3JadGens&e(&b" + getPluginName() + "&e) &7>> &eGetting if a location is a machine... &f(&b" + x + "&f,&b " + y + "&f,&b " + z + "&f)");
        return lookup.isMachine(world, x, y, z);
    }

    /**
     * Check if the api is valid.
     * @return If the api is valid
     */
    public boolean isAPIValid() { return validAPI; }

    /**
     * Get the owner of the api
     * @return the JavaPlugin from the api owner.
     */
    private JavaPlugin getApiOwner() { return apiOwner; }
    /**
     * Check the plugin's name.
     * @return The API Version.
     */
    public String getPluginName() { return this.getApiOwner().getDescription().getName(); }
}
