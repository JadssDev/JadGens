package dev.jadss.jadgens.api.config.serializers.lists;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import dev.jadss.jadgens.api.config.playerConfig.PlayerInformation;
import dev.jadss.jadgens.controller.VersionControlled;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PlayerDataList implements Configuration, VersionControlled {

    public final String configVersion;
    public final PlayerInformation[] playerData;

    public PlayerDataList() {
        this(null, null);
    }

    @Override
    public String getVersion() {
        return configVersion;
    }
}
