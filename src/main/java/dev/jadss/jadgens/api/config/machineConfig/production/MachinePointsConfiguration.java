package dev.jadss.jadgens.api.config.machineConfig.production;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachinePointsConfiguration implements Configuration {

    public final boolean enabled;
    public final int pointsAmount;

    public MachinePointsConfiguration() {
        this(false, 0);
    }
}
