package dev.jadss.jadgens.api.config.machineConfig.production.nbt;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineItemNBTConfiguration implements Configuration {

    public final boolean enabled;
    public final MachineNBTEntry[] entries;

    public MachineItemNBTConfiguration() {
        this(false, null);
    }

    public enum NBTType {
        BOOLEAN,
        INTEGER,
        STRING;
    }
}
