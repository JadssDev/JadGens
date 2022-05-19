package dev.jadss.jadgens.controller.controllers;

import dev.jadss.jadgens.api.config.generalConfig.GeneralConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.messages.MessagesConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.messages.menu.MachineMenuConfiguration;
import dev.jadss.jadgens.controller.ConfigVersions;
import dev.jadss.jadgens.controller.VersionMigrator;

public class GeneralConfigurationController extends VersionMigrator<GeneralConfiguration> {

    public GeneralConfigurationController() {
        addMigrator(ConfigVersions.NO_VERSION, (config) -> new GeneralConfiguration(ConfigVersions.NO_VERSION.getNext().getConfigVersion(),
                config.removeInvalidMachines, config.protectMachines, config.protectCrafting,
                config.produceIfOffline, config.stopProduceIfChunkIsUnloaded, config.messages));
        addMigrator(ConfigVersions.VERSION_1, (config) -> new GeneralConfiguration(ConfigVersions.VERSION_1.getNext().getConfigVersion(),
                config.removeInvalidMachines, config.protectMachines, config.protectCrafting,
                config.produceIfOffline, config.stopProduceIfChunkIsUnloaded, config.messages));
        addMigrator(ConfigVersions.VERSION_2, (config) -> {
            MessagesConfiguration messages = new MessagesConfiguration(config.messages.helpCommand, config.messages.giveCommand,
                    config.messages.actionsCommand, config.messages.shopCommand, config.messages.infoCommand,
                    new MachineMenuConfiguration(config.messages.machineMenu.title, config.messages.machineMenu.rows, true, config.messages.machineMenu.backgroundItem, config.messages.machineMenu.ownerItem,
                            config.messages.machineMenu.fuelItem, config.messages.machineMenu.statusItem, config.messages.machineMenu.closeItem),
                    config.messages.shopMenu, config.messages.dropsMenu, config.messages.giveMenu,
                    config.messages.machineMessages, config.messages.fuelMessages, config.messages.shopMessages,
                    config.messages.dropsMessages, config.messages.globalMessages);
            return new GeneralConfiguration(ConfigVersions.VERSION_2.getNext().getConfigVersion(),
                    config.removeInvalidMachines, config.protectMachines, config.protectCrafting,
                    config.produceIfOffline, config.stopProduceIfChunkIsUnloaded, messages);
        });
    }

    @Override
    public GeneralConfiguration migrate(GeneralConfiguration object) {
        return getMigrator(ConfigVersions.getVersionFromConfigString(object.getVersion())).apply(object);
    }
}
