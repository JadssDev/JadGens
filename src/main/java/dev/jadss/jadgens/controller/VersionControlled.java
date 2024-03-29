package dev.jadss.jadgens.controller;

/**
 * Represents a configuration that is version controlled and will be checked for migration when the plugin starts up!
 */
public interface VersionControlled  {

    /**
     * The current version of this configuration.
     * @return The current version of this configuration.
     */
    String getVersion();

    /**
     * Get the {@link ConfigVersions} that this configuration is.
     * @return The {@link ConfigVersions} that this configuration is.
     */
    default ConfigVersions getConfigVersion() {
        return ConfigVersions.getVersionFromConfigString(this.getVersion());
    }
}
