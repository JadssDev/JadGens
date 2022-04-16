package dev.jadss.jadgens.controller.controllers;

import dev.jadss.jadgens.api.config.generalConfig.Permissions;
import dev.jadss.jadgens.controller.ConfigVersions;
import dev.jadss.jadgens.controller.VersionMigrator;

public class PermissionsController extends VersionMigrator<Permissions> {

    public PermissionsController() {
        addMigrator(ConfigVersions.NO_VERSION, (permissions) -> new Permissions(ConfigVersions.NO_VERSION.getNext().getConfigVersion(), permissions.machineBypassPermission,
                permissions.giveCommandPermission, permissions.actionsCommandPermission, permissions.actionsCommandManageOthersPermission,
                permissions.actionsCommandPurgePermission, permissions.actionsCommandEnablePermission, permissions.actionsCommandDisablePermission,
                permissions.shopCommandPermission, permissions.machineLimitsGroups));
        addMigrator(ConfigVersions.VERSION_1, (permissions) -> new Permissions(ConfigVersions.VERSION_1.getNext().getConfigVersion(), permissions.machineBypassPermission,
                permissions.giveCommandPermission, permissions.actionsCommandPermission, permissions.actionsCommandManageOthersPermission,
                permissions.actionsCommandPurgePermission, permissions.actionsCommandEnablePermission, permissions.actionsCommandDisablePermission,
                permissions.shopCommandPermission, permissions.machineLimitsGroups));
    }

    @Override
    public Permissions migrate(Permissions object) {
        return getMigrator(ConfigVersions.getVersionFromConfigString(object.getVersion())).apply(object);
    }
}
