package dev.jadss.jadgens.api.config.generalConfig.messages;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineMessagesConfiguration implements Configuration {

    public final String placed;
    public final String broken;

    public final String reachedMaxMachines;

    public final String notOwner;

    public final String openedMenu;

    public final String toggledOn;
    public final String toggledOff;

    public final String noOwner;

    public MachineMessagesConfiguration() {
        this(null, null, null, null, null, null, null, null);
    }
}
