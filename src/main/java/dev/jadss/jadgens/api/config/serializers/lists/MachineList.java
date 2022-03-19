package dev.jadss.jadgens.api.config.serializers.lists;

import dev.jadss.jadgens.api.config.machineConfig.MachineConfiguration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineList {

    public final MachineConfiguration[] machines;

    public MachineList() {
        this(null);
    }
}
