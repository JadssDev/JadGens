package dev.jadss.jadgens.controller;

import java.util.HashMap;
import java.util.Map;

public abstract class VersionMigrator<T extends VersionControlled> {

    private final Map<ConfigVersions, VersionMigratorRunnable<T>> map = new HashMap<>();

    public void addMigrator(ConfigVersions version, VersionMigratorRunnable<T> runnable) {
        map.put(version, runnable);
    }

    public VersionMigratorRunnable<T> getMigrator(ConfigVersions version) {
        return map.get(version);
    }

    public abstract T migrate(T object);

    public boolean isLatest(T object) {
        return ConfigVersions.getVersionFromConfigString(object.getVersion()) == ConfigVersions.getLatest();
    }
}
