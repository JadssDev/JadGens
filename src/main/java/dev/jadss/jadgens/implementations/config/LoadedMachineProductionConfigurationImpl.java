package dev.jadss.jadgens.implementations.config;

import dev.jadss.jadapi.bukkitImpl.item.ItemNBT;
import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadgens.api.config.interfaces.LoadedMachineConfiguration;
import dev.jadss.jadgens.api.config.interfaces.LoadedMachineProductionConfiguration;
import dev.jadss.jadgens.api.config.machineConfig.MachineProductionConfiguration;
import dev.jadss.jadgens.api.config.machineConfig.production.ProductItemConfiguration;
import dev.jadss.jadgens.api.config.machineConfig.production.nbt.MachineItemNBTConfiguration;
import dev.jadss.jadgens.api.config.machineConfig.production.nbt.MachineNBTEntry;

import java.util.Arrays;
import java.util.List;

public class LoadedMachineProductionConfigurationImpl implements LoadedMachineProductionConfiguration {

    private final LoadedMachineConfiguration configuration;

    private final boolean producesItem;
    private final boolean producesEconomy;
    private final boolean producesExperience;
    private final boolean producesPoints;
    private final boolean usesCommands;

    private final JItemStack itemProduced;
    private final boolean sendToMenu;

    private final double economyAmount;
    private final int experienceAmount;
    private final int pointsAmount;
    private final List<String> commands;

    public LoadedMachineProductionConfigurationImpl(LoadedMachineConfiguration configuration) {
        this.configuration = configuration;

        MachineProductionConfiguration procConfig = configuration.getSuperConfiguration().productionConfig;

        this.producesItem = procConfig.itemProduction.enabled;
        this.producesEconomy = procConfig.economyProduction.enabled;
        this.producesExperience = procConfig.experienceProduction.enabled;
        this.producesPoints = procConfig.pointsProduction.enabled;
        this.usesCommands = procConfig.commandsProduction.commandsEnabled;

        if (this.producesItem) {
            ProductItemConfiguration itemProc = procConfig.itemProduction.item;
            MachineItemNBTConfiguration nbt = itemProc.nbtConfiguration;

            JItemStack item = new JItemStack(JMaterial.getRegistryMaterials().find(itemProc.itemType));
            if (itemProc.displayName != null)
                item.setDisplayName(itemProc.displayName);
            if (itemProc.lore != null)
                item.setLore(itemProc.lore);

            if (nbt != null && nbt.enabled) {
                for (MachineNBTEntry entry : nbt.entries) {
                    switch (entry.type) {
                        case BOOLEAN: {
                            item.getNBT().setBoolean(entry.key, (boolean) entry.value);
                            break;
                        }
                        case INTEGER: {
                            item.getNBT().setInteger(entry.key, (int) entry.value);
                            break;
                        }
                        case STRING: {
                            item.getNBT().setString(entry.key, (String) entry.value);
                            break;
                        }
                        default: {
                            throw new RuntimeException("NBTType is null!");
                        }
                    }
                }
            }

            this.itemProduced = item.setAmount(itemProc.amount);

            this.sendToMenu = procConfig.itemProduction.sendToMenu;
        } else {
            this.itemProduced = null;

            this.sendToMenu = false;
        }

        if (this.producesEconomy) {
            this.economyAmount = procConfig.economyProduction.economyAmount;
        } else {
            this.economyAmount = -1;
        }

        if (this.producesExperience) {
            this.experienceAmount = procConfig.experienceProduction.experienceAmount;
        } else {
            this.experienceAmount = -1;
        }

        if (this.producesPoints) {
            this.pointsAmount = procConfig.pointsProduction.pointsAmount;
        } else {
            this.pointsAmount = -1;
        }

        if (this.usesCommands) {
            this.commands = Arrays.asList(procConfig.commandsProduction.commands);
        } else {
            this.commands = null;
        }
    }

    @Override
    public LoadedMachineConfiguration getSuperConfiguration() {
        return configuration;
    }

    @Override
    public boolean producesItem() {
        return producesItem;
    }

    @Override
    public boolean sendItemToMenu() {
        return sendToMenu;
    }

    @Override
    public boolean producesEconomy() {
        return producesEconomy;
    }

    @Override
    public boolean producesExperience() {
        return producesExperience;
    }

    @Override
    public boolean producesPoints() {
        return producesPoints;
    }

    @Override
    public boolean usesCommands() {
        return usesCommands;
    }

    @Override
    public JItemStack getProduceItem() {
        return itemProduced;
    }

    @Override
    public double getEconomyAmount() {
        return economyAmount;
    }

    @Override
    public int getExperienceAmount() {
        return experienceAmount;
    }

    @Override
    public int getPointsAmount() {
        return pointsAmount;
    }

    @Override
    public List<String> getCommands() {
        return commands;
    }
}
