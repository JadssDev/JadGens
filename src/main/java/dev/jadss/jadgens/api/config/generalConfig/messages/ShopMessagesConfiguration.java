package dev.jadss.jadgens.api.config.generalConfig.messages;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShopMessagesConfiguration implements Configuration {

    public final String purchaseSuccessful;

    public final String notEnoughEconomy;
    public final String notEnoughExperience;
    public final String notEnoughPoints;

    public final String noInventorySlot;

    public ShopMessagesConfiguration() {
        this(null, null, null, null, null);
    }
}
