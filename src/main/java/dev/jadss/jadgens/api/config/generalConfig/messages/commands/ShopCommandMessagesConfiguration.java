package dev.jadss.jadgens.api.config.generalConfig.messages.commands;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShopCommandMessagesConfiguration implements Configuration {

    public final String wrongSyntax;

    public final String openedShop;
    public final String closedShop;

    public final String[] machineAliases;
    public final String[] fuelAliases;

    public ShopCommandMessagesConfiguration() {
        this(null, null, null, null, null);
    }
}