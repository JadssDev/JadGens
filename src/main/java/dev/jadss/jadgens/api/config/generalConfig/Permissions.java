package dev.jadss.jadgens.api.config.generalConfig;

import dev.jadss.jadgens.api.config.generalConfig.limits.MachineLimits;
import dev.jadss.jadgens.api.config.interfaces.Configuration;
import dev.jadss.jadgens.controller.VersionControlled;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Permissions implements Configuration, VersionControlled {

    public final String configVersion;

    public final String machineBypassPermission;

    public final String giveCommandPermission;

    public final String actionsCommandPermission;
    public final String actionsCommandManageOthersPermission;

    public final String actionsCommandPurgePermission;
    public final String actionsCommandEnablePermission;
    public final String actionsCommandDisablePermission;

    public final String shopCommandPermission;

    public final MachineLimits machineLimitsGroups;

    public Permissions() {
        this(null, null, null, null, null, null, null, null, null, null);
    }

    @Override
    public String getVersion() {
        return configVersion;
    }
}
