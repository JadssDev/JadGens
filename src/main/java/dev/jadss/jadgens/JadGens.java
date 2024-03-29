package dev.jadss.jadgens;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.enchantments.EnchantmentInstance;
import dev.jadss.jadgens.api.config.generalConfig.GeneralConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.Permissions;
import dev.jadss.jadgens.api.config.playerConfig.PlayerInformation;
import dev.jadss.jadgens.api.config.serializers.MachineInformation;
import dev.jadss.jadgens.api.config.serializers.lists.FuelList;
import dev.jadss.jadgens.api.config.serializers.lists.MachineDataList;
import dev.jadss.jadgens.api.config.serializers.lists.MachineList;
import dev.jadss.jadgens.api.config.serializers.lists.PlayerDataList;
import dev.jadss.jadgens.commands.JadGensCommand;
import dev.jadss.jadgens.controller.ConfigVersions;
import dev.jadss.jadgens.controller.VersionController;
import dev.jadss.jadgens.hooks.Hook;
import dev.jadss.jadgens.hooks.PlaceholderAPIHook;
import dev.jadss.jadgens.hooks.PlayerPointsHook;
import dev.jadss.jadgens.hooks.VaultHook;
import dev.jadss.jadgens.implementations.MachinesManager;
import dev.jadss.jadgens.listeners.BlockBreakListener;
import dev.jadss.jadgens.listeners.BlockInteractListener;
import dev.jadss.jadgens.listeners.BlockPlaceListener;
import dev.jadss.jadgens.listeners.ProtectMachinesListener;
import dev.jadss.jadgens.utils.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class JadGens extends JavaPlugin {

    private static final String MACHINES_FILE = "machines.json";
    private static final String FUELS_FILE = "fuels.json";

    private static final String CONFIG_FILE = "config.json";
    private static final String PERMISSIONS_FILES = "permissions.json";

    private static final String PLAYERS_DATA_FILE = "player_data.json";
    private static final String MACHINES_DATA_FILE = "machine_data.json";

    private final CustomConfig<MachineList> machinesConfig;
    private final CustomConfig<FuelList> fuelsConfig;

    private final CustomConfig<GeneralConfiguration> generalConfig;
    private final CustomConfig<Permissions> permissionsConfig;

    private final CustomConfig<PlayerDataList> playerDataConfig;
    private final CustomConfig<MachineDataList> machineDataConfig;

    @Getter
    private static JadGens instance;

    private final List<Hook> hooks = new ArrayList<>();

    private MetricsHandler metrics;

    private final GlowEnchantment glowEnch = new GlowEnchantment();

    private LoadingInformation loadInfo;
    private MachinesManager manager;

    public JadGens() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &3&lJadGens &eis &bloading&e..."));
        instance = this;

        getDataFolder().mkdir();

        machinesConfig = new CustomConfig<>(MACHINES_FILE, this, MachineList.class);
        fuelsConfig = new CustomConfig<>(FUELS_FILE, this, FuelList.class);

        generalConfig = new CustomConfig<>(CONFIG_FILE, this, GeneralConfiguration.class);
        permissionsConfig = new CustomConfig<>(PERMISSIONS_FILES, this, Permissions.class);

        playerDataConfig = new CustomConfig<>(PLAYERS_DATA_FILE, this, PlayerDataList.class);
        machineDataConfig = new CustomConfig<>(MACHINES_DATA_FILE, this, MachineDataList.class);

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadgens &7>> &3&lJadGens &afinished &eloading!"));
    }

    private boolean failedAtLoading = false;

    @Override
    public void onLoad() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eLoading &3&lJadGens &eHooks..."));
        //Initialize hooks.
        try { hooks.add(new PlaceholderAPIHook()); } catch(Throwable ignored) {}
        try { hooks.add(new PlayerPointsHook());   } catch(Throwable ignored) {}
        try { hooks.add(new VaultHook());          } catch(Throwable ignored) {}

        //Load the configurations and create a LoadingInformation object for The GensManager.
        if(getLoadInformation()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &3&lConfigurations &bLoaded&e!"));
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &c&lConfigurations &bFailed at loading&e!"));
            getServer().getPluginManager().disablePlugin(this);
            failedAtLoading = true;
            return;
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eWaiting for &3Bukkit's &b&lOnEnable &ecall..."));
    }

    @Override
    public void onEnable() {
        if (failedAtLoading) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> Stopping..."));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        //Setup JadAPI instance and Enchantments.
        new JGens().register(true);

        JadAPI.getInstance().getRegisterer().registerEnchantment(glowEnch);

        //Load hooks!
        hooks.forEach(hook -> {
            hook.hook(getServer());
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eHooked into &3&l" + hook.getDisplayNameWithColors() + "&e!"));
        });

        //Load the GensManager.
        this.manager = new MachinesManager(loadInfo);

        this.metrics = new MetricsHandler(8789);

        //Register Listeners.
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockInteractListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new ProtectMachinesListener(), this);

        //Register the commands.
        getCommand("jadgens").setExecutor(new JadGensCommand());
    }

    @Override
    public void onDisable() {
        if (failedAtLoading)
            return;

        this.manager.save();

        //Register instance!
        instance = null;

        //Remove manager.
        this.manager = null;

        //Unregister hooks!
        hooks.forEach(hook -> hook.unhook(this.getServer()));
    }

    public Hook getHookByName(String name) {
        for (Hook hook : hooks)
            if (hook.getName().equalsIgnoreCase(name))
                return hook;
        return null;
    }

    public MachinesManager getManager() {
        return manager;
    }

    public EnchantmentInstance getGlowEnchantment() {
        return glowEnch.getRegistered();
    }

    private boolean getLoadInformation() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eLoading &3&lConfigurations &e..."));

        GeneralConfiguration generalConfig = this.generalConfig.get();
        Permissions permissionsConfig = this.permissionsConfig.get();

        MachineList machineList = machinesConfig.get();
        FuelList fuelList = fuelsConfig.get();

        MachineDataList machineData = machineDataConfig.get();
        PlayerDataList playerData = playerDataConfig.get();

        String preventedDamage = ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eJadGens has &b&lfound &cerrors &ewhile trying to &bload your files&e, and &3&lJadGens &ehas been stopped to prevent &c&ldamage &eto your &bfiles&e. Please check your files and try again.");

        if(generalConfig == null) {
            if (!this.generalConfig.didExist) {
                generalConfig = this.generalConfig.save(ConfigDefaults.defaultGeneralConfiguration());
            } else {
                //File could not be reed.
                Bukkit.getConsoleSender().sendMessage(preventedDamage);
                return false;
            }
        }

        if (permissionsConfig == null) {
            if (!this.permissionsConfig.didExist) {
                permissionsConfig = this.permissionsConfig.save(ConfigDefaults.defaultPermissionsConfiguration());
            } else {
                //File could not be reed.
                Bukkit.getConsoleSender().sendMessage(preventedDamage);
                return false;
            }
        }

        if(machineList == null) {
            if (!this.machinesConfig.didExist) {
                machineList = this.machinesConfig.save(new MachineList(ConfigVersions.getLatest().getConfigVersion(), ConfigDefaults.defaultMachineConfigurations()));
            } else {
                //File could not be reed.
                Bukkit.getConsoleSender().sendMessage(preventedDamage);
                return false;
            }
        }

        if(fuelList == null) {
            if (!this.fuelsConfig.didExist) {
                fuelList = this.fuelsConfig.save(new FuelList(ConfigVersions.getLatest().getConfigVersion(), ConfigDefaults.defaultFuelConfiguration()));
            } else {
                //File could not be reed.
                Bukkit.getConsoleSender().sendMessage(preventedDamage);
                return false;
            }
        }

        if(machineData == null) {
            if (!this.machineDataConfig.didExist) {
                machineData = this.machineDataConfig.save(new MachineDataList(ConfigVersions.getLatest().getConfigVersion(), new MachineInformation[0]));
            } else {
                //File could not be reed.
                Bukkit.getConsoleSender().sendMessage(preventedDamage);
                return false;
            }
        }

        if(playerData == null) {
            if (!this.playerDataConfig.didExist) {
                playerData = this.playerDataConfig.save(new PlayerDataList(ConfigVersions.getLatest().getConfigVersion(), new PlayerInformation[0]));
            } else {
                //File could not be reed.
                Bukkit.getConsoleSender().sendMessage(preventedDamage);
                return false;
            }
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eFinished loading &3&lConfigurations &efrom &bfiles&e!"));

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eChecking &b&lMigration status &eof the &3Loaded Configuration files&e... Please wait..."));

        generalConfig = VersionController.migrate(generalConfig);
        this.generalConfig.save(generalConfig);

        permissionsConfig = VersionController.migrate(permissionsConfig);
        this.permissionsConfig.save(permissionsConfig);

        machineList = VersionController.migrate(machineList);
        this.machinesConfig.save(machineList);

        fuelList = VersionController.migrate(fuelList);
        this.fuelsConfig.save(fuelList);

        machineData = VersionController.migrate(machineData);
        this.machineDataConfig.save(machineData);

        playerData = VersionController.migrate(playerData);
        this.playerDataConfig.save(playerData);

        this.loadInfo = new LoadingInformation(this,
                machineList,
                fuelList,
                machineData,
                playerData,
                generalConfig,
                permissionsConfig,
                this.playerDataConfig,
                this.machineDataConfig);
        return true;
    }
}
