package dev.jadss.jadgens.api.config.generalConfig.messages;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FuelMessagesConfiguration implements Configuration {

    public final String usedFuel;
    public final String usedMultipleFuels;
    public final String machineHasInfiniteFuel;
    public final String reachedMaxCapacity;
    public final String incompatibleFuel;

    public FuelMessagesConfiguration() {
        this(null, null, null, null, null);
    }
}
