package dev.jadss.jadgens.api.config.generalConfig.messages.menu;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShopMenuConfiguration implements Configuration {

    //switch.
    public final boolean enabled;

    //Main config.
    public final boolean separateFuelsAndMachines;

    //"global"
    public final String title;

    public final int inventoryRows;

    public final int machinesInventoryRows;
    public final int fuelsInventoryRows;

    public final MenuItemConfiguration machinesChooseItem;
    public final MenuItemConfiguration fuelsChooseItem;

    public final String[] loreBuyWithEconomy;
    public final String[] loreBuyWithExperience;
    public final String[] loreBuyWithPoints;

    //global background item.
    public final String backgroundItemType;

    public ShopMenuConfiguration() {
        this(false, false, null, 0, 0, 0, null, null, null, null, null, null);
    }
}
