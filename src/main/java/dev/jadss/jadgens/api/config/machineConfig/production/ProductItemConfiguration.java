package dev.jadss.jadgens.api.config.machineConfig.production;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import dev.jadss.jadgens.api.config.machineConfig.production.nbt.MachineItemNBTConfiguration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductItemConfiguration implements Configuration {

    public final String itemType;
    public final int amount;

    public final String displayName;
    public final String[] lore;

    public final boolean glow;

    public final MachineItemNBTConfiguration nbtConfiguration;

    public ProductItemConfiguration() {
        this(null, -1, null, null, false, null);
    }
}
