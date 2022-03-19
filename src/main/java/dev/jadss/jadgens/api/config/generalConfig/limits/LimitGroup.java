package dev.jadss.jadgens.api.config.generalConfig.limits;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LimitGroup implements Configuration {

    public final String limitGroupName;
    public final int limit;

    public final String permission;

    public LimitGroup() {
        this(null, 0, null);
    }
}
