package ml.jadss.jadgens;

import ml.jadss.jadgens.commands.JadGensCommand;
import ml.jadss.jadgens.commands.TabCompleter;
import ml.jadss.jadgens.hooks.PlaceHolders;
import ml.jadss.jadgens.listeners.*;
import ml.jadss.jadgens.management.DataFile;
import ml.jadss.jadgens.management.LangFile;
import ml.jadss.jadgens.management.MetricsLite;
import ml.jadss.jadgens.management.UpdateChecker;
import ml.jadss.jadgens.tasks.BlocksRemover;
import ml.jadss.jadgens.tasks.ProduceRunnable;
import ml.jadss.jadgens.utils.MachineLoader;
import ml.jadss.jadgens.utils.ParticleSystem;
import ml.jadss.jadgens.utils.ParticleType;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class JadGens extends JavaPlugin {

    //Files
    private DataFile dataFile;
    private LangFile langFile;
    //hook booleans.
    private boolean hookedVault = false;
    private boolean hookedPlaceHolderAPI = false;
    private boolean hookedPlayerPoints = false;
    //hooks
    private Economy eco;
    private PlayerPointsAPI pointsAPI;
    private MetricsLite metrics;
    //tasks
    private BukkitTask producer;
    //Instance
    private static JadGens instance;
    //Update Checker
    private UpdateChecker updateChecker;
    //Compatibility Modes
    private boolean compatibilityMode = false;
    private boolean skyblockMode = false;
    //Particle System
    private ParticleType particleType;
    //Block remover system
    private BlocksRemover blocksRemover;
    //List with players purging machines.
    private final List<UUID> playersPurgingMachines = new ArrayList<>();
    //List with players who disconnected while purging machines.
    private final HashMap<UUID, HashMap<Integer, Integer>> playersWhoDisconnectedWhilePurgingMachines = new HashMap<>();


    @Override
    public void onEnable() {
        //Compatibility Mode
        if (getServer().getBukkitVersion().contains("1.7")) {
            compatibilityMode = true;
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &bEnabling &3&lCompatibility Mode&e..."));
        }
        if (getServer().getPluginManager().getPlugin("IridiumSkyblock") != null ||
                getServer().getPluginManager().getPlugin("SuperiorSkyblock") != null ||
                getServer().getPluginManager().getPlugin("SuperiorSkyblock2") != null ||
                getServer().getPluginManager().getPlugin("FabledSkyblock") != null) {
            skyblockMode = true;
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &bEnabling &3&lSkyblock Mode&e..."));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eAll gens production has been delayed for 30 seconds."));
        }

        //Setting instance
        instance = this;

        //Setup configs
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        reloadConfig();

        //Data File
        dataFile = new DataFile();
        dataFile.setupDataFile();

        //Lang File
        langFile = new LangFile();
        langFile.setupLangFile();

        //Setup the API
        setupAPIDebug();

        //Setup blocks remover.
        blocksRemover = new BlocksRemover();

        //Hook into plugins
        hookVault();
        hookPlaceHolderAPI();
        hookPlayerPoints();
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eHooked into &bXP&e!"));
        if (hookedVault) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eHooked into &bVault&e!"));
        }
        if (hookedPlaceHolderAPI) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eHooked into &bPlaceHolderAPI&e!"));
        }
        if (hookedPlayerPoints) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eHooked into &bPlayerPoints&e!"));
        }

        //Setup the shop
        if (!setupShop()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        //Create the Scheduler
        if (getSkyblockMode())
            producer = new ProduceRunnable().runTaskTimer(this, 20 * 30, getConfig().getInt("machinesConfig.machinesDelay") * 20L);
        else
            producer = new ProduceRunnable().runTaskTimer(this, 20L, getConfig().getInt("machinesConfig.machinesDelay") * 20L);

        //Register everything
        registerStuff();

        //Start metrics if compatibility mode = disabled.
        if (!getCompatibilityMode()) { metrics = new MetricsLite(this, 8789); }

        //Load machines
        new MachineLoader().loadMachines();

        //Starting Update checker.
        updateChecker = new UpdateChecker();
        updateChecker.check();

        //Plugin enabled!
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &3Plugin &bEnabled&7!"));

        //Checks

        //Check particle working
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eChecking particle System"));
        boolean particleCheck = new ParticleSystem().checkParticle();
        if (particleCheck)
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &3&lParticle System &eis &a&lWorking&e!"));
         else
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eYour &a&lselected &3particle&e, is not &a&lcorrect&e, &b&lfix it&e!"));
    }

    @Override
    public void onDisable() {
        //SuperSpeed.
        this.getBlocksRemover().goSuperSpeedBrrrrrrrrrrrrr();

        //Stop producer
        producer.cancel();

        //Save data
        getDataFile().saveData();

        //Disable hooks
        if (isHookedVault()) {
            hookedVault = false;
            eco = null;
        }
        if (isHookedPlaceHolderAPI()) {
            hookedPlaceHolderAPI = false;
            try { new PlaceHolders().unregister(); } catch(NullPointerException ex) { }
        }
        if (isHookedPlayerPoints()) {
            hookedPlayerPoints = false;
            pointsAPI = null;
        }

        //Unload Machines
        new MachineLoader().unloadMachines();

        //Disabling the instance
        instance = null;

        //Plugin DISABLED!
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &bDisabling &3Plugin&e!"));
    }

    //API STUFF
    private boolean APIDebug;
    public void setupAPIDebug() {
        APIDebug = lang().getBoolean("messages.debugAPI");
    }

    public boolean isAPIDebugEnabled() {
        if (lang().getBoolean("messages.debugAPI") != APIDebug) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eA plugin tried to disable API Debug, but it was reverted."));
            APIDebug = getConfig().getBoolean("messages.debugAPI");
            return APIDebug;
        } else {
            return APIDebug;
        }
    }
    //END OF API STUFF


    //Other stuff
    private void registerStuff() {
        //Register Commands
        getCommand("JadGens").setExecutor(new JadGensCommand());
        getCommand("JadGens").setTabCompleter(new TabCompleter());

        //Register Listeners
        getServer().getPluginManager().registerEvents(new PlayerBuildListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerBreakListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new OpenGuiListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        if (!getCompatibilityMode()) getServer().getPluginManager().registerEvents(new BlockExplodeListener(), this);
        getServer().getPluginManager().registerEvents(new EntityExplodeEvent(), this);
        getServer().getPluginManager().registerEvents(new PistonMoveListener(), this);
        getServer().getPluginManager().registerEvents(new ShopListeners(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerCraftListener(), this);
    }

    //SETUP SHOP STUFF
    private boolean setupShop() {
        if (getConfig().getBoolean("shop.enabled")) {
            for (String key : getConfig().getConfigurationSection("machines").getKeys(false)) {
                if (getConfig().getString("machines." + key + ".shop.currency").equalsIgnoreCase("ECO")) {
                    if (!hookedVault) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eCouldn't &b&lstartup &ebecause, a &3machine currency &eis &a\"ECO\"&e, but &3&lVault &cwasn't &afound&e!"));
                        return false;
                    }
                }
                if (getConfig().getString("machines." + key + ".shop.currency").equalsIgnoreCase("POINTS")) {
                    if (!hookedPlayerPoints) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eCouldn't &b&lstartup &ebecause, a &3machine currency &eis &a\"POINTS\"&e, but &3&lPlayerPoints &cwasn't &afound&e!"));
                        return false;
                    }
                }
            }
            for (String key : getConfig().getConfigurationSection("fuels").getKeys(false)) {
                if (getConfig().getString("fuels." + key + ".shop.currency").equalsIgnoreCase("ECO")) {
                    if (!hookedVault) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eCouldn't &b&lstartup &ebecause, a &3machine currency &eis &a\"ECO\"&e, but &3&lVault &cwasn't &afound&e!"));
                        return false;
                    }
                }
                if (getConfig().getString("fuels." + key + ".shop.currency").equalsIgnoreCase("POINTS")) {
                    if (!hookedPlayerPoints) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eCouldn't &b&lstartup &ebecause, a &3machine currency &eis &a\"POINTS\"&e, but &3&lPlayerPoints &cwasn't &afound&e!"));
                        return false;
                    }
                }
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &3Shop &eis &cDisabled&e!"));
            return true;
        }
        return true;
    }

    //Hooks
    public void hookVault() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            hookedVault = false;
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            hookedVault = false;
            return;
        }
        eco = rsp.getProvider();
        hookedVault = true;
    }

    public void hookPlayerPoints() {
        if (getServer().getPluginManager().getPlugin("PlayerPoints") == null) {
            hookedPlayerPoints = false;
            return;
        }
        pointsAPI = new PlayerPointsAPI((PlayerPoints) getServer().getPluginManager().getPlugin("PlayerPoints"));
        hookedPlayerPoints = true;
    }

    public void hookPlaceHolderAPI() {
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            return;
        }
        try { new PlaceHolders().unregister(); } catch(NullPointerException ignored) { Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &3PlaceHolderAPI &eCommitted a NPE, it was not shown so you think it is an error.")); }
        hookedPlaceHolderAPI = true;
        new PlaceHolders().register();
    }

    /* Getters */

    //files
    public DataFile getDataFile() { return dataFile; }
    public LangFile getLangFile() { return langFile; }
    //hook booleans.
    public boolean isHookedVault() { return hookedVault; }
    public boolean isHookedPlaceHolderAPI() { return hookedPlaceHolderAPI; }
    public boolean isHookedPlayerPoints() { return hookedPlayerPoints; }
    //hooks
    public Economy getEco() { return eco; }
    public PlayerPointsAPI getPointsAPI() { return pointsAPI; }
    //tasks
    public BukkitTask getProducer() { return producer; }
    public void setProducer(BukkitTask producer) { this.producer = producer; }
    //Instance
    public static JadGens getInstance() { return instance; }
    //Update Checker
    public UpdateChecker getUpdateChecker() { return updateChecker; }
    //Compatibility Mode
    public boolean getCompatibilityMode() { return compatibilityMode; }
    public boolean getSkyblockMode() { return skyblockMode; }
    //Particle System
    public ParticleType getParticleType() { return particleType; }
    public void setParticleType(ParticleType type) { particleType = type; }
    //Block remover system
    public BlocksRemover getBlocksRemover() { return blocksRemover; }
    //List with players purging machines.
    public List<UUID> getPlayersPurgingMachines() { return playersPurgingMachines; }
    //People who disconnected while purging machines.
    public HashMap<UUID, HashMap<Integer, Integer>> getPlayersWhoDisconnectedWhilePurgingMachines() { return playersWhoDisconnectedWhilePurgingMachines; }

    //Lang file
    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}