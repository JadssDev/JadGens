package dev.jadss.jadgens.api.config.generalConfig.limits;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@AllArgsConstructor
@JsonPropertyOrder(value = { "defaultLimitGroupName", "defaultLimit", "groups" })
public class MachineLimits implements Configuration {

    public final String defaultLimitGroupName;
    public int defaultLimit;

    public LimitGroup[] groups;

    public MachineLimits() {
        this(null, -1, null);
    }
}
