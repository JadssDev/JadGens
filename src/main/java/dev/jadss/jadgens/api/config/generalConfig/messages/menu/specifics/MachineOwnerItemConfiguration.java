package dev.jadss.jadgens.api.config.generalConfig.messages.menu.specifics;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineOwnerItemConfiguration implements Configuration {

    public final String itemType;
    public final String displayName;
    public final String[] lore;

    public final String noOwnerPlaceholderText;

    public final boolean glow;

    public final int slot;

    public MachineOwnerItemConfiguration() {
        this(null, null, null, null, false, 0);
    }
}
