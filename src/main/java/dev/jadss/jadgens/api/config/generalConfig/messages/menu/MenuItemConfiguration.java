package dev.jadss.jadgens.api.config.generalConfig.messages.menu;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MenuItemConfiguration implements Configuration {

    public final String itemType;
    public final String displayName;
    public final String[] lore;

    public final boolean glow;

    public final int slot;

    public MenuItemConfiguration() {
        this(null, null, null, false, -1);
    }
}
