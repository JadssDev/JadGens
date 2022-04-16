package dev.jadss.jadgens.controller;

import dev.jadss.jadgens.api.config.generalConfig.GeneralConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.Permissions;
import dev.jadss.jadgens.api.config.serializers.lists.FuelList;
import dev.jadss.jadgens.api.config.serializers.lists.MachineDataList;
import dev.jadss.jadgens.api.config.serializers.lists.MachineList;
import dev.jadss.jadgens.api.config.serializers.lists.PlayerDataList;
import dev.jadss.jadgens.controller.controllers.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class VersionController {

    private static final Map<Class<? extends VersionControlled>, VersionMigrator<? extends VersionControlled>> map = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends VersionControlled> T migrate(T versionControlled) {
        Class<?> versionControlledClass = versionControlled.getClass();
        for (Map.Entry<Class<? extends VersionControlled>, VersionMigrator<? extends VersionControlled>> entry : map.entrySet()) {
            if (entry.getKey().equals(versionControlledClass)) {

                VersionMigrator<T> migrator = (VersionMigrator<T>) entry.getValue();
                T object = versionControlled;

                while (!migrator.isLatest(object)) {
                    ConfigVersions nextVersion = object.getConfigVersion().getNext();
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eMigrating &3&l" + versionControlledClass.getSimpleName() + " &eto version &b" + nextVersion + "&e. Please wait.."));
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eChangelog:"));
                    for (String s : nextVersion.getChangelog())
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e- &b" + s));
                    object = migrator.migrate(object);
                }

                return object;
            }
        }
        return null;
    }

    static {
        map.put(FuelList.class, new FuelListController());
        map.put(GeneralConfiguration.class, new GeneralConfigurationController());
        map.put(MachineDataList.class, new MachineDataListController());
        map.put(MachineList.class, new MachineListController());
        map.put(Permissions.class, new PermissionsController());
        map.put(PlayerDataList.class, new PlayerDataListController());
    }
}
