package dev.jadss.jadgens.api.config.generalConfig.messages.menu.specifics;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineCloseItemConfiguration implements Configuration {

    public final String itemType;
    public final String displayName;
    public final String[] lore;

    public final boolean invertClicks;
    public final boolean glow;

    public final int slot;

    public MachineCloseItemConfiguration() {
        this(null, null, null, false, false, 0);
    }
}
