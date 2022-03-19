package dev.jadss.jadgens.api.config.machineConfig.production;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineExperienceConfiguration implements Configuration {

    public final boolean enabled;
    public final int experienceAmount;

    public MachineExperienceConfiguration() {
        this(false, 0);
    }
}
