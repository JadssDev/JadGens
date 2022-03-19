package dev.jadss.jadgens.api.config.generalConfig.messages.commands;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InfoCommandMessagesConfiguration {

    public final String[] infoMessage;
    public final String infinite;

    public InfoCommandMessagesConfiguration() {
        this(null, null);
    }
}
