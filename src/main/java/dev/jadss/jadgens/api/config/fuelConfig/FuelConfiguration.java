package dev.jadss.jadgens.api.config.fuelConfig;

import dev.jadss.jadgens.api.config.RecipeConfiguration;
import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FuelConfiguration implements Configuration {

    public final String fuelType;
    public final int fuelAmount;

    public final RecipeConfiguration recipe;

    public final FuelShopConfiguration shop;

    public final FuelItemConfiguration fuelItem;

    public FuelConfiguration() {
        this(null, 1, null, null, null);
    }
}
