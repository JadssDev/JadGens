package dev.jadss.jadgens.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ConfigVersions {
    NO_VERSION(-1), //Represents a config from the start.
    VERSION_1(1); //Represents the config from the start but with a version associated with it!

    private static final List<ConfigVersions> VALUES = Arrays.asList(values());

    private final int version;

    ConfigVersions(int version) {
        this.version = version;
    }



    public int getVersion() {
        return version;
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
