package dev.jadss.jadgens.api.config.serializers.lists;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import dev.jadss.jadgens.api.config.playerConfig.PlayerInformation;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PlayerDataList implements Configuration {

    public final PlayerInformation[] playerData;

    public PlayerDataList() {
        this(null);
    }
}
