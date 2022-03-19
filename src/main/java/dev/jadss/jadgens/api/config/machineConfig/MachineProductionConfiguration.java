package dev.jadss.jadgens.api.config.machineConfig;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import dev.jadss.jadgens.api.config.machineConfig.production.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineProductionConfiguration implements Configuration {

    public final MachineProductItemConfiguration itemProduction;

    public final MachineExperienceConfiguration experienceProduction;
    public final MachineEconomyConfiguration economyProduction;
    public final MachinePointsConfiguration pointsProduction;

    public final MachineCommandsConfiguration commandsProduction;

    public MachineProductionConfiguration() {
        this(null, null, null, null, null);
    }

}
