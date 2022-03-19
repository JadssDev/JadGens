package dev.jadss.jadgens.api.config.generalConfig.messages.commands;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HelpCommandMessagesConfiguration implements Configuration {

    public final String[] message;

    public HelpCommandMessagesConfiguration() {
        this(null);
    }
}
