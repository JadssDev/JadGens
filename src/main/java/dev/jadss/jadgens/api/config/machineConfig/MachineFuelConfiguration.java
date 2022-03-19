package dev.jadss.jadgens.api.config.machineConfig;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineFuelConfiguration implements Configuration {

    public final boolean needsFuelToProduce;

    public final int maxFuel;
    public final boolean needsSpecificFuel;
    public final String[] fuelSpecifics;

    public MachineFuelConfiguration() {
        this(false, 0, false, null);
    }
}
