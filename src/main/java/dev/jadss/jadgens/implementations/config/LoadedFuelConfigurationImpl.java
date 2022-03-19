package dev.jadss.jadgens.implementations.config;

import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadgens.api.config.fuelConfig.FuelConfiguration;
import dev.jadss.jadgens.api.config.interfaces.LoadedFuelConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class LoadedFuelConfigurationImpl implements LoadedFuelConfiguration {

    private final FuelConfiguration superConfig;
    private final int fuelAmount;

    private final JItemStack item;

    public LoadedFuelConfigurationImpl(FuelConfiguration configuration) {
        this.superConfig = configuration;
        this.fuelAmount = configuration.fuelAmount;

        JMaterial finalBlockType = JMaterial.getRegistryMaterials().find(configuration.fuelItem.itemType.toUpperCase());
        if(finalBlockType == null || finalBlockType.getMaterial(JMaterial.Type.ITEM) == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eInvalid block type: &b" + configuration.fuelItem.itemType));
            finalBlockType = JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.STONE);
        }

        this.item = new JItemStack(finalBlockType);

        this.item.setDisplayName(configuration.fuelItem.displayName);
        this.item.setLore(configuration.fuelItem.lore);

        this.item.setNBTBoolean("JadGens_Fuel", true);
        this.item.setNBTString("JadGens_Fuel_Type", configuration.fuelType);
    }

    @Override
    public FuelConfiguration getSuperConfiguration() {
        return superConfig;
    }

    @Override
    public String getConfigurationName() {
        return superConfig.fuelType;
    }

    @Override
    public int getFuelAmount() {
        return fuelAmount;
    }

    @Override
    public JItemStack getSuperItem() {
        return item.copy();
    }

    @Override
    public ItemStack getItem() {
        return item.buildItemStack().clone();
    }
}
