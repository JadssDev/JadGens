package dev.jadss.jadgens.api.config.fuelConfig;

import dev.jadss.jadgens.api.ShopEconomy;
import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FuelShopConfiguration implements Configuration {

    public final boolean showInShop;
    public final int cost;
    public final ShopEconomy economyType;

    public final int slot;

    public FuelShopConfiguration() {
        this(false, 0, null, 0);
    }
}
