package dev.jadss.jadgens.api.config.serializers.lists;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import dev.jadss.jadgens.api.config.machineConfig.MachineConfiguration;
import dev.jadss.jadgens.controller.VersionControlled;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineList implements Configuration, VersionControlled {

    public final String configVersion;
    public final MachineConfiguration[] machines;

    public MachineList() {
        this(null, null);
    }

    @Override
    public String getVersion() {
        return configVersion;
    }
}
