package dev.jadss.jadgens.controller.controllers;

import dev.jadss.jadgens.api.config.serializers.lists.FuelList;
import dev.jadss.jadgens.controller.ConfigVersions;
import dev.jadss.jadgens.controller.VersionMigrator;

public class FuelListController extends VersionMigrator<FuelList> {

    public FuelListController() {
        addMigrator(ConfigVersions.NO_VERSION, (list) -> new FuelList(ConfigVersions.NO_VERSION.getNext().getConfigVersion(), list.fuels));
        addMigrator(ConfigVersions.VERSION_1, (list) -> new FuelList(ConfigVersions.VERSION_1.getNext().getConfigVersion(), list.fuels));
        addMigrator(ConfigVersions.VERSION_2, (list) -> new FuelList(ConfigVersions.VERSION_2.getNext().getConfigVersion(), list.fuels));
    }

    @Override
    public FuelList migrate(FuelList object) {
        return getMigrator(ConfigVersions.getVersionFromConfigString(object.getVersion())).apply(object);
    }
}
