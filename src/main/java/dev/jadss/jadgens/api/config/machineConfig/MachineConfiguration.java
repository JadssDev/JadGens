package dev.jadss.jadgens.api.config.machineConfig;

import dev.jadss.jadgens.api.config.RecipeConfiguration;
import dev.jadss.jadgens.api.config.interfaces.Configuration;
import dev.jadss.jadgens.api.config.machineConfig.misc.MachineHologramConfiguration;
import dev.jadss.jadgens.api.config.machineConfig.misc.MachineParticleConfiguration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineConfiguration implements Configuration {

    // General configuration
    public final String machineType;
    public final int ticksToGenerate;
    public final String blockType;
    public final boolean allowToReceiveFuelThroughHoppers;

    // RECIPE

    public final RecipeConfiguration recipe;

    // Item meta!
    public final MachineItemConfiguration machineItem;

    // Specifics
    public final MachineShopConfiguration shop;

    public final MachineFuelConfiguration fuels;

    public final MachineProductionConfiguration productionConfig;

    public final MachineHologramConfiguration hologramConfiguration;

    public final MachineParticleConfiguration particleConfiguration;

    public MachineConfiguration() {
        this(null, 0, null, false, null, null, null, null, null, null, null);
    }
}
