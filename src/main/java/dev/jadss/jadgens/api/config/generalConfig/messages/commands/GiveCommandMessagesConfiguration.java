package dev.jadss.jadgens.api.config.generalConfig.messages.commands;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GiveCommandMessagesConfiguration implements Configuration {

    public final String wrongSyntax;

    public final String[] machineAliases;
    public final String[] fuelAliases;

    public final String giveMenuOpened;

    public final String couldNotFindAnyMatchingAlias;

    public final String couldntFindId;

    public final String givenMachine;
    public final String givenMachineTo;

    public final String givenFuel;
    public final String givenFuelTo;

    public GiveCommandMessagesConfiguration() {
        this(null, null, null, null, null, null, null, null, null, null);
    }
}
