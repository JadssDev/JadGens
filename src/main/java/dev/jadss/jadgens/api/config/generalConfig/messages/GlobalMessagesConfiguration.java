package dev.jadss.jadgens.api.config.generalConfig.messages;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GlobalMessagesConfiguration implements Configuration {

    public final String notAPlayer;
    public final String notANumber;
    public final String playerNotFound;
    public final String noPermission;

    public GlobalMessagesConfiguration() {
        this(null, null, null, null);
    }
}
