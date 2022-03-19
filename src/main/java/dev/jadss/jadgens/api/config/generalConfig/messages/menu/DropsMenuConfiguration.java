package dev.jadss.jadgens.api.config.generalConfig.messages.menu;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DropsMenuConfiguration implements Configuration {

    public final String title;
    public final int inventoryRows;

    public final String backgroundItemType;

    public final String[] loreAmount;

    public DropsMenuConfiguration() {
        this(null, 0, null, null);
    }

}
