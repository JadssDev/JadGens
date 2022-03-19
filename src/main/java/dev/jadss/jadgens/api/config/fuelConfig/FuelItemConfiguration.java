package dev.jadss.jadgens.api.config.fuelConfig;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FuelItemConfiguration implements Configuration {

    public final String itemType;

    public final String displayName;
    public final String[] lore;

    public final boolean glow;

    public FuelItemConfiguration() {
        this(null, null, null, false);
    }
}
