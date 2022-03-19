package dev.jadss.jadgens.implementations.config;

import dev.jadss.jadgens.api.config.generalConfig.Permissions;
import dev.jadss.jadgens.api.config.generalConfig.messages.MessagesConfiguration;
import dev.jadss.jadgens.api.config.interfaces.GeneralConfiguration;

import java.security.Permission;

public class GeneralConfigurationImpl implements GeneralConfiguration {

    private final boolean removeInvalidMachines;
    private final boolean protectMachines;
    private final boolean preventCrafting;
    private final boolean produceEvenIfOffline;
    private final boolean stopProducingInUnloadedChunks;

    private final MessagesConfiguration messages;

    private final Permissions permissions;

    public GeneralConfigurationImpl(dev.jadss.jadgens.api.config.generalConfig.GeneralConfiguration config, Permissions permissions) {
        this.removeInvalidMachines = config.removeInvalidMachines;
        this.protectMachines = config.protectMachines;
        this.preventCrafting = config.protectCrafting;
        this.produceEvenIfOffline = config.produceIfOffline;
        this.stopProducingInUnloadedChunks = config.stopProduceIfChunkIsUnloaded;

        this.messages = config.messages;

        this.permissions = permissions;
    }

    @Override
    public boolean removeInvalidMachines() {
        return removeInvalidMachines;
    }

    @Override
    public boolean protectMachines() {
        return protectMachines;
    }

    @Override
    public boolean preventCrafting() {
        return preventCrafting;
    }

    @Override
    public boolean produceEvenIfOffline() {
        return produceEvenIfOffline;
    }

    @Override
    public boolean stopProducingInUnloadedChunks() {
        return stopProducingInUnloadedChunks;
    }

    @Override
    public MessagesConfiguration getMessages() {
        return messages;
    }

    @Override
    public Permissions getPermissions() {
        return permissions;
    }
}
