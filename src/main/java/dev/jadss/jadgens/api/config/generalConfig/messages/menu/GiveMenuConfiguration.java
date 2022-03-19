package dev.jadss.jadgens.api.config.generalConfig.messages.menu;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GiveMenuConfiguration implements Configuration {

    public final int rows;
    public final String title;

    public final String backgroundItem;

    public GiveMenuConfiguration() {
        this(0, null, null);
    }
}
