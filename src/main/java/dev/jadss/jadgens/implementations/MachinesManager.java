package dev.jadss.jadgens.implementations;

import dev.jadss.jadapi.JadAPIPlugin;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.bukkitImpl.misc.JShapedCraft;
import dev.jadss.jadapi.management.JQuickEvent;
import dev.jadss.jadgens.JadGens;
import dev.jadss.jadgens.api.MachinesAPI;
import dev.jadss.jadgens.api.MenusManager;
import dev.jadss.jadgens.api.config.RecipeConfiguration;
import dev.jadss.jadgens.api.config.fuelConfig.FuelConfiguration;
import dev.jadss.jadgens.api.config.interfaces.GeneralConfiguration;
import dev.jadss.jadgens.api.config.interfaces.LoadedFuelConfiguration;
import dev.jadss.jadgens.api.config.interfaces.LoadedMachineConfiguration;
import dev.jadss.jadgens.api.config.machineConfig.MachineConfiguration;
import dev.jadss.jadgens.api.config.CraftingIngredient;
import dev.jadss.jadgens.api.config.playerConfig.MachineDropsPlayerInformation;
import dev.jadss.jadgens.api.config.playerConfig.PlayerInformation;
import dev.jadss.jadgens.api.config.serializers.MachineInformation;
import dev.jadss.jadgens.api.config.serializers.lists.MachineDataList;
import dev.jadss.jadgens.api.config.serializers.lists.PlayerDataList;
import dev.jadss.jadgens.api.machines.Machine;
import dev.jadss.jadgens.api.machines.MachineInstance;
import dev.jadss.jadgens.api.machines.MachinesList;
import dev.jadss.jadgens.api.player.MachinesUser;
import dev.jadss.jadgens.controller.ConfigVersions;
import dev.jadss.jadgens.implementations.config.GeneralConfigurationImpl;
import dev.jadss.jadgens.implementations.config.LoadedFuelConfigurationImpl;
import dev.jadss.jadgens.implementations.config.LoadedMachineConfigurationImpl;
import dev.jadss.jadgens.implementations.machines.MachineImpl;
import dev.jadss.jadgens.implementations.machines.MachinesListImpl;
import dev.jadss.jadgens.implementations.player.UserMachineDropsInstance;
import dev.jadss.jadgens.implementations.player.UserManager;
import dev.jadss.jadgens.utils.CustomConfig;
import dev.jadss.jadgens.utils.LoadingInformation;
import dev.jadss.jadgens.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class MachinesManager implements MachinesAPI, Runnable {

    private final GeneralConfiguration generalConfiguration;

    private final MenusManager menuManager;

    private final List<MachineInstance> machines = new ArrayList<>();
    private final List<LoadedMachineConfiguration> machineConfigurations = new ArrayList<>();
    private final List<LoadedFuelConfiguration> fuelConfigurations = new ArrayList<>();

    private final List<MachinesUser> users = new ArrayList<>();

    private final HashMap<String, JShapedCraft> machineRecipes = new HashMap<>();
    private final HashMap<String, JShapedCraft> fuelsRecipes = new HashMap<>();

    private final CustomConfig<PlayerDataList> playerData;
    private final CustomConfig<MachineDataList> machineData;

    private final JQuickEvent joinQuickEvent;

    public MachinesManager(LoadingInformation loadingInfo) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eLoading &bManager&e! Please wait......"));

        //General Configuration
        this.generalConfiguration = new GeneralConfigurationImpl(loadingInfo.generalConfig, loadingInfo.permissions);

        menuManager = new MenusManagerImpl(loadingInfo.generalConfig.messages.shopMenu, loadingInfo.generalConfig.messages.dropsMenu);

        //Load Configuration (2 types)
        for (MachineConfiguration machineConfiguration : loadingInfo.machineConfigs.machines)
            loadConfiguration(machineConfiguration);

        for (FuelConfiguration fuelConfiguration : loadingInfo.fuelConfigs.fuels)
            loadConfiguration(fuelConfiguration);

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eLoaded &b" + machineConfigurations.size() + " &3Machine Configurations&e, &b" + fuelConfigurations.size() + " &3Fuel Configurations&e!"));

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eLoaded &3&lPlayer &eand &3&lMachine &bdata&e!"));

        playerData = loadingInfo.playerDataConfig;
        machineData = loadingInfo.machineDataConfig;

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eScheduled &3Loading data task&e, awaiting for &b&lexecution&e!!"));
        //Load Data
        Bukkit.getScheduler().runTask(loadingInfo.plugin, () -> {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eStarting to &bload &3&lMachines&e..."));
            for (MachineInformation machineInformation : loadingInfo.machineData.machines) {
                Machine machine = null;
                try {
                    machine = new MachineImpl(machineInformation, this);
                    if (machine.getMachineConfiguration() == null) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eThe Machine with &3ID " + machine.getId() + " &ewas &c&lremoved &edue to not having a &bvalid Configuration&e!"));
                        continue;
                    }
                } catch(RuntimeException ex) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eError while loading a &3&lMachine&e! it has been &b&lskipped&e!"));
                    continue;
                }
                this.machines.add(machine.getInstance());
            }
        });

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eStarting to &bload &3&lPlayer Data&e..."));
        for (PlayerInformation playerInformation : loadingInfo.playerDataList.playerData) {
            UserManager user = new UserManager(playerInformation, this);
            List<UserMachineDropsInstance> errorInstances = user.getAllDropsInformation().stream()
                    .filter(instance -> instance.getMachineConfiguration() == null)
                    .map(instance -> (UserMachineDropsInstance) instance)
                    .collect(Collectors.toCollection(ArrayList::new));

            if (errorInstances.size() >= 1) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eThe Player with &3UUID " + user.getPlayer() + " &ehas invalid &3Drop Information&e! &3Fixing&e..."));
                errorInstances.forEach(user::removeDropInstance);
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &cProblematic instances &ehave been &cremoved &efrom " + user.getPlayer() + "&e!"));
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eAmount of &c&lProblematic instances " + "(Size &e&m->&e " + errorInstances.size() + ")"));
            }

            user.updateDrops();
            users.add(user);
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eFinished loading &3&lPlayer Data&e!"));

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eStarting up the &3&lMachines&e! &bHere goes EVERYTHING&e!!"));

        //The delay is to prevent Anti-invalidation!
        Bukkit.getScheduler().runTaskTimer(loadingInfo.plugin, this, 20*10L, 1L);
        Bukkit.getScheduler().runTaskTimer(loadingInfo.plugin, this::save, 5 * 60 * 30, 5 * 60 * 30);

        joinQuickEvent = new JQuickEvent<>(JadAPIPlugin.get(JadGens.class), PlayerJoinEvent.class, EventPriority.LOWEST, event -> {
            MachinesUser user = getPlayer(event.getPlayer().getUniqueId());

            if (user.getXpToCollect() != 0) {
                event.getPlayer().setLevel(event.getPlayer().getLevel() + user.getXpToCollect());
                user.setXpToCollect(0);
            }
        }, -1, -1, e -> true, JQuickEvent.generateID()).register(true);

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &3Manager &ehas been &a&lsuccessfully loaded&e!!"));

    }

    @Override
    public MachineInstance getMachine(String id) {
        return machines.stream().filter(machine -> machine.getMachine().getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    @Override
    public MachineInstance getMachine(Location location) {
        return machines.stream().filter(machine -> machine.getMachine().getLocation().equals(location)).findFirst().orElse(null);
    }

    @Override
    public LoadedMachineConfiguration getMachineConfiguration(String type) {
        return machineConfigurations.stream().filter(configuration -> configuration.getConfigurationName().equalsIgnoreCase(type)).findFirst().orElse(null);
    }

    @Override
    public LoadedFuelConfiguration getFuelConfiguration(String type) {
        return fuelConfigurations.stream().filter(configuration -> configuration.getConfigurationName().equalsIgnoreCase(type)).findFirst().orElse(null);
    }

    @Override
    public boolean isMachineConfigurationExistent(String type) {
        return machineConfigurations.stream().anyMatch(loadedMachineConfiguration -> loadedMachineConfiguration.getConfigurationName().equalsIgnoreCase(type));
    }

    @Override
    public boolean isFuelConfigurationExistent(String type) {
        return fuelConfigurations.stream().anyMatch(loadedFuelConfiguration -> loadedFuelConfiguration.getConfigurationName().equalsIgnoreCase(type));
    }

    @Override
    public MachinesUser getPlayer(UUID playerUUID) {
        MachinesUser user = this.users.stream().filter(p -> p.getPlayer().equals(playerUUID)).findFirst().orElse(null);
        if (user == null) {
            user = new UserManager(new PlayerInformation(playerUUID.getLeastSignificantBits(), playerUUID.getMostSignificantBits(), new MachineDropsPlayerInformation[0], 0), this);
            this.users.add(user);
        }
        return user;
    }

    @Override
    public List<MachinesUser> getPlayers() {
        return users;
    }

    @Override
    public MenusManager getMenuManager() {
        return menuManager;
    }

    @Override
    public boolean isFuel(ItemStack item) {
        if (item == null || item.getType() == JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.AIR).getMaterial(JMaterial.Type.ITEM).getKey())
            return false;

        return new JItemStack(item).getNBTBoolean("JadGens_Fuel");
    }

    @Override
    public LoadedFuelConfiguration getFuelConfigurationByItem(ItemStack item) {
        if (!isFuel(item))
            return null;

        String configurationName = new JItemStack(item).getNBTString("JadGens_Fuel_Type");

        return getFuelConfiguration(configurationName);
    }

    @Override
    public boolean isMachine(ItemStack item) {
        if (item == null || item.getType() == JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.AIR).getMaterial(JMaterial.Type.ITEM).getKey())
            return false;
        return new JItemStack(item).getNBTBoolean("JadGens_Machine");
    }

    @Override
    public LoadedMachineConfiguration getMachineConfigurationByItem(ItemStack item) {
        if (!isMachine(item))
            return null;

        String configurationName = new JItemStack(item).getNBTString("JadGens_Machine_Type");

        return getMachineConfiguration(configurationName);
    }

    @Override
    public List<LoadedMachineConfiguration> getMachineConfigurations() {
        return new ArrayList<>(machineConfigurations);
    }

    @Override
    public List<LoadedFuelConfiguration> getFuelConfigurations() {
        return new ArrayList<>(fuelConfigurations);
    }

    @Override
    public MachinesList getMachines() {
        return new MachinesListImpl(new ArrayList<>(machines));
    }

    @Override
    public MachineInstance createMachine(LoadedMachineConfiguration configuration, UUID uuid, Location location) {
        if (configuration == null)
            throw new IllegalArgumentException("Configuration may not be null!");

        if (location == null)
            throw new IllegalArgumentException("Location may not be null!");

        if (location.getWorld() != null) {
            if (location.getBlock().getType() != configuration.getBlockMaterial()) {
                //Set block...
                Map.Entry<Material, Byte> entry = configuration.getSuperBlockMaterial().getMaterial(JMaterial.Type.BLOCK);
                Block block = location.getWorld().getBlockAt(location);
                block.setType(entry.getKey());
                if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12))
                    block.setData(entry.getValue());
            }

            //Create the machine!
            Machine machine = new MachineImpl(new MachineInformation(Utilities.fromLocation(location), configuration.getConfigurationName(), uuid, false, configuration.getTicksDelay(), 0), this);

            this.machines.add(machine.getInstance());

            return machine.getInstance();
        } else throw new RuntimeException("World is null!");
    }

    @Override
    public GeneralConfiguration getGeneralConfiguration() {
        return generalConfiguration;
    }

    //Unsafe methods

    @Override
    public LoadedMachineConfiguration loadConfiguration(MachineConfiguration configuration) {
        if (isMachineConfigurationExistent(configuration.machineType))
            throw new RuntimeException("Fuel configuration already existent!");

        LoadedMachineConfiguration loadedConfiguration = new LoadedMachineConfigurationImpl(this, configuration);
        this.machineConfigurations.add(loadedConfiguration);

        RecipeConfiguration recipeConfig = loadedConfiguration.getSuperConfiguration().recipe;
        if (recipeConfig != null && recipeConfig.enabled) {
            JShapedCraft recipe = new JShapedCraft(JadAPIPlugin.get(JadGens.class), "Recipe_" + loadedConfiguration.getConfigurationName());
            recipe.setResultItem(loadedConfiguration.getSuperMachineItem());
            recipe.setShape(loadedConfiguration.getSuperConfiguration().recipe.row1, loadedConfiguration.getSuperConfiguration().recipe.row2, loadedConfiguration.getSuperConfiguration().recipe.row3);
            for (CraftingIngredient ingredient : recipeConfig.ingredients)
                recipe.addIngredient(ingredient.key, JMaterial.getRegistryMaterials().find(ingredient.materialType));

            recipe.register(true);
            machineRecipes.put(loadedConfiguration.getConfigurationName(), recipe);
        }

        return loadedConfiguration;
    }

    @Override
    public LoadedFuelConfiguration loadConfiguration(FuelConfiguration configuration) {
        if (isFuelConfigurationExistent(configuration.fuelType))
            throw new RuntimeException("Fuel configuration already existent!");

        LoadedFuelConfiguration loadedConfiguration = new LoadedFuelConfigurationImpl(configuration);
        this.fuelConfigurations.add(loadedConfiguration);

        RecipeConfiguration recipeConfig = loadedConfiguration.getSuperConfiguration().recipe;
        if (recipeConfig != null && recipeConfig.enabled) {
            JShapedCraft recipe = new JShapedCraft(JadAPIPlugin.get(JadGens.class), "Recipe_" + loadedConfiguration.getConfigurationName());
            recipe.setResultItem(loadedConfiguration.getSuperItem());
            recipe.setShape(loadedConfiguration.getSuperConfiguration().recipe.row1, loadedConfiguration.getSuperConfiguration().recipe.row2, loadedConfiguration.getSuperConfiguration().recipe.row3);
            for (CraftingIngredient ingredient : recipeConfig.ingredients)
                recipe.addIngredient(ingredient.key, JMaterial.getRegistryMaterials().find(ingredient.materialType));

            recipe.register(true);
            fuelsRecipes.put(loadedConfiguration.getConfigurationName(), recipe);
        }

        return loadedConfiguration;
    }

    public void removeMachine(MachineInstance machine) {
        this.machines.remove(machine);
    }

    private boolean isCurrentlySaving = false;

    @Override
    public void save() {
        if (isCurrentlySaving)
            return;

        isCurrentlySaving = true;

        try {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eSaving &3&lMachines&e..."));
            List<MachineInformation> machineData = new ArrayList<>();
            List<PlayerInformation> playerData = new ArrayList<>();

            for (MachineInstance machine : machines)
                machineData.add(machine.getMachine().save());

            for (MachinesUser user : this.users)
                playerData.add(user.save());

            this.playerData.save(new PlayerDataList(ConfigVersions.getLatest().getConfigVersion(), playerData.stream().toArray(PlayerInformation[]::new)));
            this.machineData.save(new MachineDataList(ConfigVersions.getLatest().getConfigVersion(), machineData.stream().toArray(MachineInformation[]::new)));

            isCurrentlySaving = false;

            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eSave &3&lMachines &a&lSucceeded&e!!"));
        } catch (Exception ex) {
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eSave &3&lMachines &c&lFailed&e!!"));
            isCurrentlySaving = false;
        }
    }

    //Runnable

    @Override
    public void run() {
        List<MachineInstance> instances = new ArrayList<>(this.machines);
        for (MachineInstance machine : instances) {
            if (!machine.isValid()) {
                this.machines.remove(machine);
                continue;
            }
            machine.tick();
        }
    }
}
