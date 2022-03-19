package dev.jadss.jadgens.api.config.machineConfig.production.nbt;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineNBTEntry implements Configuration {

    public final String key;
    public final Object value;
    public final MachineItemNBTConfiguration.NBTType type;

    public MachineNBTEntry() {
        this(null, null, null);
    }
}
