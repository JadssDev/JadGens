package dev.jadss.jadgens.api.config.generalConfig.messages.menu.specifics;

import dev.jadss.jadgens.api.config.generalConfig.messages.menu.MenuItemConfiguration;
import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineStatusItemConfiguration implements Configuration {

    public final MenuItemConfiguration enabled;
    public final MenuItemConfiguration disabled;

    public MachineStatusItemConfiguration() {
        this(null, null);
    }
}
