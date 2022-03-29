package dev.jadss.jadgens.api.config.serializers.lists;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import dev.jadss.jadgens.api.config.serializers.MachineInformation;
import dev.jadss.jadgens.controller.VersionControlled;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineDataList implements Configuration, VersionControlled {

    public final String configVersion;
    public final MachineInformation[] machines;

    public MachineDataList() {
        this(null, null);
    }

    @Override
    public String getVersion() {
        return configVersion;
    }
}
