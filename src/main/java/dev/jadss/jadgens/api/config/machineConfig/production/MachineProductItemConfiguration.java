package dev.jadss.jadgens.api.config.machineConfig.production;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineProductItemConfiguration implements Configuration {

    public final boolean enabled;

    public final ProductItemConfiguration item;

    public final boolean sendToMenu;

    public MachineProductItemConfiguration() {
        this(false, null, false);
    }
}
