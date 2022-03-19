package dev.jadss.jadgens.api.config.serializers.lists;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import dev.jadss.jadgens.api.config.serializers.MachineInformation;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineDataList implements Configuration {

    public final MachineInformation[] machines;

    public MachineDataList() {
        this(null);
    }
}
