package dev.jadss.jadgens.api.config.generalConfig;

import dev.jadss.jadgens.api.config.generalConfig.messages.MessagesConfiguration;
import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GeneralConfiguration implements Configuration {

    public final boolean removeInvalidMachines;
    public final boolean protectMachines;
    public final boolean protectCrafting;

    public final boolean produceIfOffline;
    public final boolean stopProduceIfChunkIsUnloaded;

    public final MessagesConfiguration messages;

    public GeneralConfiguration() {
        this(false, false, false, false, false, null);
    }
}
