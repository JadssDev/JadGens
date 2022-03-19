package dev.jadss.jadgens.api.config.machineConfig;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineItemConfiguration implements Configuration {

    public final String displayName;
    public final String[] lore;
    public final boolean glow;

    public MachineItemConfiguration() {
        this(null, null, false);
    }
}
