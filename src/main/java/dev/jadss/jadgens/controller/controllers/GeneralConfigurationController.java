package dev.jadss.jadgens.controller.controllers;

import dev.jadss.jadgens.api.config.generalConfig.GeneralConfiguration;
import dev.jadss.jadgens.controller.ConfigVersions;
import dev.jadss.jadgens.controller.VersionMigrator;

public class GeneralConfigurationController extends VersionMigrator<GeneralConfiguration> {

    public GeneralConfigurationController() {
        addMigrator(ConfigVersions.NO_VERSION, (config) -> new GeneralConfiguration(ConfigVersions.NO_VERSION.getNext().getConfigVersion(),
                config.removeInvalidMachines, config.protectMachines, config.protectCrafting,
                config.produceIfOffline, config.stopProduceIfChunkIsUnloaded, config.messages));
    }

    @Override
    public GeneralConfiguration migrate(GeneralConfiguration object) {
        return getMigrator(ConfigVersions.getVersionFromConfigString(object.getVersion())).apply(object);
    }
}
