package dev.jadss.jadgens.api.config.serializers.lists;

import dev.jadss.jadgens.api.config.fuelConfig.FuelConfiguration;
import dev.jadss.jadgens.api.config.interfaces.Configuration;
import dev.jadss.jadgens.controller.VersionControlled;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FuelList implements Configuration, VersionControlled {

    public final String configVersion;
    public final FuelConfiguration[] fuels;

    public FuelList() {
        this(null, null);
    }

    @Override
    public String getVersion() {
        return configVersion;
    }
}
