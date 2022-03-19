package dev.jadss.jadgens.api.config.generalConfig.messages.commands;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ActionsCommandMessagesConfiguration implements Configuration {

    public final String wrongSyntax;

    public final String[] enableActionAliases;
    public final String[] disableActionAliases;
    public final String[] purgeActionAliases;

    public final String machinesEnabled;
    public final String machinesDisabled;
    public final String machinesPurged;

    public ActionsCommandMessagesConfiguration() {
        this(null, null, null, null, null, null, null);
    }
}
