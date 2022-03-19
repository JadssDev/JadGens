package dev.jadss.jadgens.api.config.generalConfig.messages;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DropsMessageConfiguration {

    public final String menuOpened;
    public final String notEnoughDropsToCollect;
    public final String dropsCollected;

    public DropsMessageConfiguration() {
        this(null, null, null);
    }
}
