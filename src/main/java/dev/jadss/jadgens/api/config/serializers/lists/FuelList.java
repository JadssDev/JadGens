package dev.jadss.jadgens.api.config.serializers.lists;

import dev.jadss.jadgens.api.config.fuelConfig.FuelConfiguration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FuelList {

    public final FuelConfiguration[] fuels;

    public FuelList() {
        this(null);
    }
}
