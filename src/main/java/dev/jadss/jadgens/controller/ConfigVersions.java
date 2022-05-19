package dev.jadss.jadgens.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ConfigVersions {
    NO_VERSION(-1, "No changelog"), //Represents a config from the start.
    VERSION_1(1, "Added the version to the configuration files!"), //Represents the config from the start but with a version associated with it!
    VERSION_2(2, "Added a new value to machine holograms, \"YAxisOffset\", this value controls how high the hologram of the machine spawns!!"), //New version!
    VERSION_3(3, "Made it so you could actually disable the Machine GUI, missed feature when migrating!",
            "Added a new option for machines to choose if they can receive fuel from hoppers."); //Newer version now =)

    private static final List<ConfigVersions> VALUES = Arrays.asList(values());

    private final int version;
    private final String[] changelog;

    ConfigVersions(int version, String... changelog) {
        this.version = version;
        this.changelog = changelog;
    }

    public int getVersion() {
        return version;
    }

    public String[] getChangelog() {
        return changelog;
    }

    public String getConfigVersion() {
        return version + "-DO-NOT-CHANGE";
    }

    public ConfigVersions getNext() {
        if (this == getLatest())
            return null;

        return VALUES.get(VALUES.indexOf(this) + 1);
    }


    public static ConfigVersions getVersionFromConfigString(String versionString) {
        if (versionString == null)
            return ConfigVersions.NO_VERSION;
        return getVersionFromVersion(Integer.parseInt(versionString.replace("-DO-NOT-CHANGE", "")));
    }

    public static ConfigVersions getVersionFromVersion(int version) {
        return new ArrayList<>(VALUES).stream().filter(ver -> ver.getVersion() == version).findFirst().orElse(null);
    }

    public static ConfigVersions getLatest() {
        return VALUES.get(VALUES.size() - 1);
    }
}
