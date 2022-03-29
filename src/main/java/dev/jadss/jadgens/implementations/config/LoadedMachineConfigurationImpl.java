package dev.jadss.jadgens.implementations.config;

import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadgens.JadGens;
import dev.jadss.jadgens.api.config.interfaces.*;
import dev.jadss.jadgens.api.config.machineConfig.MachineConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class LoadedMachineConfigurationImpl implements LoadedMachineConfiguration {

    private final MachineConfiguration superConfig;

    private final LoadedMachineProductionConfiguration productionConfig;
    private final LoadedHologramConfiguration hologramConfiguration;
    private final LoadedParticleConfiguration particleConfiguration;

    private final int ticksDelay;

    private final boolean needsFuel;
    private final boolean allFuelsAllowed;
    private final int maxFuel;
    private final String[] usableFuels;

    private final JMaterial blockMaterial;

    private final JItemStack item;

    public LoadedMachineConfigurationImpl(MachineConfiguration machineConfiguration) {
        this.superConfig = machineConfiguration;

        this.ticksDelay = machineConfiguration.ticksToGenerate;

        this.needsFuel = machineConfiguration.fuels.needsFuelToProduce;

        this.allFuelsAllowed = !machineConfiguration.fuels.needsSpecificFuel;
        this.maxFuel = machineConfiguration.fuels.maxFuel;
        this.usableFuels = machineConfiguration.fuels.fuelSpecifics;

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

        this.item.setNBTBoolean("JadGens_Machine", true);
        this.item.setNBTString("JadGens_Machine_Type", machineConfiguration.machineType);

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
    public boolean needsFuelToProduce() {
        return needsFuel;
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
        if (allFuelsAllowed)
            return true;

        return Arrays.stream(usableFuels).anyMatch(fuel.getConfigurationName()::equalsIgnoreCase);
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
        return this.item.buildItemStack().clone();
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
