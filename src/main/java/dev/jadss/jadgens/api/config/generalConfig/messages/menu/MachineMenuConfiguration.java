package dev.jadss.jadgens.api.config.generalConfig.messages.menu;

import dev.jadss.jadgens.api.config.generalConfig.messages.menu.specifics.MachineCloseItemConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.messages.menu.specifics.MachineFuelItemConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.messages.menu.specifics.MachineOwnerItemConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.messages.menu.specifics.MachineStatusItemConfiguration;
import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineMenuConfiguration implements Configuration {

    public final String title;
    public final int rows;

    public final MenuItemConfiguration backgroundItem;

    public final MachineOwnerItemConfiguration ownerItem;
    public final MachineFuelItemConfiguration fuelItem;
    public final MachineStatusItemConfiguration statusItem;
    public final MachineCloseItemConfiguration closeItem;

    public MachineMenuConfiguration() {
        this(null, 0, null, null, null, null, null);
    }
}
