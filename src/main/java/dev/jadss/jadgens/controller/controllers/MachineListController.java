package dev.jadss.jadgens.controller.controllers;

import dev.jadss.jadgens.api.config.machineConfig.MachineConfiguration;
import dev.jadss.jadgens.api.config.machineConfig.misc.MachineHologramConfiguration;
import dev.jadss.jadgens.api.config.serializers.lists.MachineList;
import dev.jadss.jadgens.controller.ConfigVersions;
import dev.jadss.jadgens.controller.VersionMigrator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MachineListController extends VersionMigrator<MachineList> {

    public MachineListController() {
        addMigrator(ConfigVersions.NO_VERSION, (list) -> new MachineList(ConfigVersions.NO_VERSION.getNext().getConfigVersion(), list.machines));
        addMigrator(ConfigVersions.VERSION_1, (list) -> {
            List<MachineConfiguration> newMachineList = Arrays.asList(list.machines).stream().map(machineConfig -> {
                MachineHologramConfiguration oldHologram = machineConfig.hologramConfiguration;
                MachineHologramConfiguration newHologram = new MachineHologramConfiguration(oldHologram.enabled, oldHologram.lines, 1, oldHologram.statusOn, oldHologram.statusOff);

                return new MachineConfiguration(machineConfig.machineType, machineConfig.ticksToGenerate, machineConfig.blockType, machineConfig.recipe,
                        machineConfig.machineItem, machineConfig.shop, machineConfig.fuels, machineConfig.productionConfig, newHologram, machineConfig.particleConfiguration);
            }).collect(Collectors.toCollection(ArrayList::new));

            return new MachineList(ConfigVersions.VERSION_1.getNext().getConfigVersion(), newMachineList.stream().toArray(MachineConfiguration[]::new));
        });
    }

    @Override
    public MachineList migrate(MachineList object) {
        return getMigrator(ConfigVersions.getVersionFromConfigString(object.getVersion())).apply(object);
    }
}
