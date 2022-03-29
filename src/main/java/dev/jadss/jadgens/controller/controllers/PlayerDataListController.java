package dev.jadss.jadgens.controller.controllers;

import dev.jadss.jadgens.api.config.serializers.lists.PlayerDataList;
import dev.jadss.jadgens.controller.ConfigVersions;
import dev.jadss.jadgens.controller.VersionMigrator;

public class PlayerDataListController extends VersionMigrator<PlayerDataList> {

    public PlayerDataListController() {
        addMigrator(ConfigVersions.NO_VERSION, (list) -> new PlayerDataList(ConfigVersions.NO_VERSION.getNext().getConfigVersion(), list.playerData));
    }

    @Override
    public PlayerDataList migrate(PlayerDataList object) {
        return getMigrator(ConfigVersions.getVersionFromConfigString(object.getVersion())).apply(object);
    }
}
