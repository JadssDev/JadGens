package dev.jadss.jadgens.implementations.config;

import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadgens.JadGens;
import dev.jadss.jadgens.api.config.interfaces.*;
import dev.jadss.jadgens.api.config.machineConfig.MachineConfiguration;
import dev.jadss.jadgens.implementations.MachinesManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LoadedMachineConfigurationImpl implements LoadedMachineConfiguration {

    private final MachineConfiguration superConfig;

    private final LoadedMachineProductionConfiguration productionConfig;
    private final LoadedHologramConfiguration hologramConfiguration;
    private final LoadedParticleConfiguration particleConfiguration;

    private final int ticksDelay;

    private final boolean needsFuel;
    private final boolean needsSpecificFuel;
    private final int maxFuel;
    private final List<LoadedFuelConfiguration> specificFuels;

    private final boolean allowToReceiveFuelThroughHoppers;

    private final JMaterial blockMaterial;

    private final JItemStack item;

    public LoadedMachineConfigurationImpl(MachinesManager manager, MachineConfiguration machineConfiguration) {
        this.superConfig = machineConfiguration;

        this.ticksDelay = machineConfiguration.ticksToGenerate;

        this.needsFuel = machineConfiguration.fuels.needsFuelToProduce;

        this.needsSpecificFuel = machineConfiguration.fuels.needsSpecificFuel;
        this.maxFuel = machineConfiguration.fuels.maxFuel;
        this.specificFuels = Arrays.stream(machineConfiguration.fuels.fuelSpecifics)
                .map(name -> {
                    LoadedFuelConfiguration fuelConfig = manager.getFuelConfiguration(name);
                    if (fuelConfig == null)
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eAn error occurred while loading &bMachine &a" + this.getConfigurationName() + "&e! " +
                                "&eCould not find the &b&lfuel configuration &efor the &aname &eof &c&l" + name + "&e!"));
                    return fuelConfig;
                })
                .collect(Collectors.toCollection(ArrayList::new));
        this.specificFuels.removeIf(Objects::isNull);

        this.allowToReceiveFuelThroughHoppers = machineConfiguration.allowToReceiveFuelThroughHoppers;

        JMaterial finalBlockType = JMaterial.getRegistryMaterials().find(machineConfiguration.blockType.toUpperCase());
        if (finalBlockType == null || finalBlockType.getMaterial(JMaterial.Type.BOTH) == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eInvalid block type: &b" + machineConfiguration.blockType.toUpperCase() + "&e, the &3block &especified has to be a &bblock &eand &bitem&e."));
            finalBlockType = JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.STONE);
        }

        this.blockMaterial = finalBlockType;

        this.item = new JItemStack(blockMaterial);

        this.item.setDisplayName(machineConfiguration.machineItem.displayName);
        this.item.setLore(machineConfiguration.machineItem.lore);

        if (machineConfiguration.machineItem.glow)
            this.item.addEnchantment(JadGens.getInstance().getGlowEnchantment().asEnchantment(), 69); //yes funny.

        this.item.getNBT()
                .setBoolean("JadGens_Machine", true)
                .setString("JadGens_Machine_Type", machineConfiguration.machineType);

        this.productionConfig = new LoadedMachineProductionConfigurationImpl(this);
        this.hologramConfiguration = new LoadedHologramConfigurationImpl(this);
        this.particleConfiguration = new LoadedParticleConfigurationImpl(this);
    }

    @Override
    public String getConfigurationName() {
        return superConfig.machineType;
    }

    @Override
    public int getTicksDelay() {
        return ticksDelay;
    }

    @Override
    public int getMaxFuelAmount() {
        return maxFuel;
    }

    @Override
    public boolean IsSpecificFuelNeeded() {
        return needsSpecificFuel;
    }

    @Override
    public List<String> getSpecificFuelNeeded() {
        return specificFuels.stream().map(LoadedFuelConfiguration::getConfigurationName).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<LoadedFuelConfiguration> getSpecificFuelConfigurationsNeeded() {
        return new ArrayList<>(specificFuels);
    }

    @Override
    public boolean needsFuelToProduce() {
        return needsFuel;
    }

    @Override
    public boolean isAllowedToReceiveFuelThroughHoppers() {
        return allowToReceiveFuelThroughHoppers;
    }

    @Override
    public LoadedHologramConfiguration getHologramConfiguration() {
        return hologramConfiguration;
    }

    @Override
    public LoadedParticleConfiguration getParticleConfiguration() {
        return particleConfiguration;
    }

    @Override
    public boolean isFuelCompatible(LoadedFuelConfiguration fuel) {
        if (!needsSpecificFuel)
            return true;

        return specificFuels.stream().map(LoadedFuelConfiguration::getConfigurationName).anyMatch(fuel.getConfigurationName()::equalsIgnoreCase);
    }

    //todo: on a reload of config, find a way to update this.
    private Material cacheMaterial;

    @Override
    public Material getBlockMaterial() {
        if (cacheMaterial == null)
            this.cacheMaterial = blockMaterial.getMaterial(JMaterial.Type.BLOCK).getKey();
        return cacheMaterial;
    }

    @Override
    public JMaterial getSuperBlockMaterial() {
        return blockMaterial;
    }

    @Override
    public ItemStack getMachineItem() {
        return this.item.getBukkitItem().clone();
    }

    @Override
    public JItemStack getSuperMachineItem() {
        return this.item.copy();
    }

    @Override
    public LoadedMachineProductionConfiguration getProductionConfiguration() {
        return productionConfig;
    }

    @Override
    public MachineConfiguration getSuperConfiguration() {
        return superConfig;
    }
}
