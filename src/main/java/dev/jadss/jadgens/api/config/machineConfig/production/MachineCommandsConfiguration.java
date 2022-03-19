package dev.jadss.jadgens.api.config.machineConfig.production;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineCommandsConfiguration implements Configuration {

    public final boolean commandsEnabled;
    public final String[] commands;

    public MachineCommandsConfiguration() {
        this(false, new String[]{});
    }
}
