package dev.jadss.jadgens.api.config.machineConfig.misc;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineHologramConfiguration implements Configuration {

    public final boolean enabled;
    public final String[] lines;

    public final String statusOn;
    public final String statusOff;

    public MachineHologramConfiguration() {
        this(false, null, null, null);
    }
}
