package dev.jadss.jadgens.controller.controllers;

import dev.jadss.jadgens.api.config.serializers.lists.MachineList;
import dev.jadss.jadgens.controller.ConfigVersions;
import dev.jadss.jadgens.controller.VersionMigrator;

public class MachineListController extends VersionMigrator<MachineList> {

    public MachineListController() {
        addMigrator(ConfigVersions.NO_VERSION, (list) -> new MachineList(ConfigVersions.NO_VERSION.getNext().getConfigVersion(), list.machines));
    }

    @Override
    public MachineList migrate(MachineList object) {
        return getMigrator(ConfigVersions.getVersionFromConfigString(object.getVersion())).apply(object);
    }
}
