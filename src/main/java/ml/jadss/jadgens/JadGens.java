package ml.jadss.jadgens;

import ml.jadss.jadgens.commands.JadGensCommand;
import ml.jadss.jadgens.commands.TabCompleter;
import ml.jadss.jadgens.listeners.*;
import ml.jadss.jadgens.management.DataFile;
import ml.jadss.jadgens.management.LangFile;
import ml.jadss.jadgens.management.MetricsLite;
import ml.jadss.jadgens.tasks.ProduceScheduler;
import ml.jadss.jadgens.utils.PlaceHolders;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

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
    private ScheduledFuture<?> producer;
    //Instance
    private static JadGens instance;
    //ScheduledExecutor
    private final static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
    //Compatibility Mode
    public boolean compatibilityMode = false;

    @Override
    public void onEnable() {
        //Compatibility Mode
        if (getServer().getBukkitVersion().contains("1.7")) {
            compatibilityMode = true;
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &bEnabling &3&lCompatibility Mode&e..."));
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
        producer = new ProduceScheduler(getConfig().getInt("machinesConfig.machinesDelay")).getScheduledFuture();

        //Setup the API
        setupAPIDebug();

        //Register everything
        registerStuff();

        //Start metrics if compatibility mode = disabled.
        if (!getCompatibilityMode()) { metrics = new MetricsLite(this, 8789); }

        //Plugin enabled!
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &3Plugin &bEnabled&7!"));
    }

    @Override
    public void onDisable() {
        //Terminate the Scheduler
        producer.cancel(false);

        //Disable hooks
        eco = null;
        pointsAPI = null;
        metrics = null;

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
                if (getConfig().getString("machines." + key + ".shop.currency").equalsIgnoreCase("POINTS")) {
                    if (!hookedPlayerPoints) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eCouldn't &b&lstartup &ebecause, a &3machine currency &eis &a\"POINTS\"&e, but &3&lPlayerPoints &cwasn't &afound&e!"));
                        return false;
                    }
                }
            }
        } else if (!getConfig().getBoolean("shop.enabled")) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &3Shop &eis &cDisabled&e!"));
            return true;
        }
        return true;
    }


    //Hooks
    private void hookVault() {
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

    private void hookPlayerPoints() {
        if (getServer().getPluginManager().getPlugin("PlayerPoints") == null) {
            hookedPlayerPoints = false;
            return;
        }
        pointsAPI = new PlayerPointsAPI((PlayerPoints) getServer().getPluginManager().getPlugin("PlayerPoints"));
        hookedPlayerPoints = true;
    }

    private void hookPlaceHolderAPI() {
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            return;
        }
        hookedPlaceHolderAPI = true;
        new PlaceHolders().register();
    }

    /* Getters */

    //files
    public DataFile getDataFile() { return dataFile; }
    public LangFile getLangFile() { return langFile; }
    //hook booleans.
    public boolean isHookedVault() {
        return hookedVault;
    }
    public boolean isHookedPlaceHolderAPI() {
        return hookedPlaceHolderAPI;
    }
    public boolean isHookedPlayerPoints() {
        return hookedPlayerPoints;
    }
    //hooks
    public Economy getEco() {
        return eco;
    }
    public PlayerPointsAPI getPointsAPI() {
        return pointsAPI;
    }
    //tasks

    public ScheduledFuture<?> getProducer() {
        return producer;
    }

    public void setProducer(ScheduledFuture<?> producer) {
        this.producer = producer;
    }

    //Instance
    public static JadGens getInstance() {
        return instance;
    }
    //Compatibility Mode
    public boolean getCompatibilityMode() { return compatibilityMode; }

    public static ScheduledExecutorService getExecutorService() {
        return executorService;
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}