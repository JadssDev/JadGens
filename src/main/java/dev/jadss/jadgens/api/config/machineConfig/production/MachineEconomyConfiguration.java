package dev.jadss.jadgens.api.config.machineConfig.production;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineEconomyConfiguration implements Configuration {

    public final boolean enabled;
    public final double economyAmount;

    public MachineEconomyConfiguration() {
        this(false, 0);
    }
}
