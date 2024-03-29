package dev.jadss.jadgens.controller.controllers;

import dev.jadss.jadgens.api.config.serializers.lists.MachineDataList;
import dev.jadss.jadgens.controller.ConfigVersions;
import dev.jadss.jadgens.controller.VersionMigrator;

public class MachineDataListController extends VersionMigrator<MachineDataList> {

    public MachineDataListController() {
        addMigrator(ConfigVersions.NO_VERSION, (list) -> new MachineDataList(ConfigVersions.NO_VERSION.getNext().getConfigVersion(), list.machines));
        addMigrator(ConfigVersions.VERSION_1, (list) -> new MachineDataList(ConfigVersions.VERSION_1.getNext().getConfigVersion(), list.machines));
        addMigrator(ConfigVersions.VERSION_2, (list) -> new MachineDataList(ConfigVersions.VERSION_2.getNext().getConfigVersion(), list.machines));
    }

    @Override
    public MachineDataList migrate(MachineDataList object) {
        return getMigrator(ConfigVersions.getVersionFromConfigString(object.getVersion())).apply(object);
    }
}
