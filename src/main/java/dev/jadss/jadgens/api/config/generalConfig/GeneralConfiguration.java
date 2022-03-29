package dev.jadss.jadgens.api.config.generalConfig;

import dev.jadss.jadgens.api.config.generalConfig.messages.MessagesConfiguration;
import dev.jadss.jadgens.api.config.interfaces.Configuration;
import dev.jadss.jadgens.controller.VersionControlled;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GeneralConfiguration implements Configuration, VersionControlled {

    public final String configVersion;

    public final boolean removeInvalidMachines;
    public final boolean protectMachines;
    public final boolean protectCrafting;

    public final boolean produceIfOffline;
    public final boolean stopProduceIfChunkIsUnloaded;

    public final MessagesConfiguration messages;

    public GeneralConfiguration() {
        this(null, false, false, false, false, false, null);
    }

    @Override
    public String getVersion() {
        return configVersion;
    }
}
