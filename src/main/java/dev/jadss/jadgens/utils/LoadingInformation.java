package dev.jadss.jadgens.utils;

import dev.jadss.jadgens.JadGens;
import dev.jadss.jadgens.api.config.generalConfig.GeneralConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.Permissions;
import dev.jadss.jadgens.api.config.serializers.Information;
import dev.jadss.jadgens.api.config.serializers.lists.FuelList;
import dev.jadss.jadgens.api.config.serializers.lists.MachineDataList;
import dev.jadss.jadgens.api.config.serializers.lists.MachineList;
import dev.jadss.jadgens.api.config.serializers.lists.PlayerDataList;
import lombok.AllArgsConstructor;

/**
 * This information is necessary for the Manager to load up!
 */
@AllArgsConstructor
public class LoadingInformation implements Information {

    public JadGens plugin;

    public MachineList machineConfigs;
    public FuelList fuelConfigs;

    public MachineDataList machineData;
    public PlayerDataList playerDataList;

    public GeneralConfiguration generalConfig;
    public Permissions permissions;

    public CustomConfig<PlayerDataList> playerDataConfig;
    public CustomConfig<MachineDataList> machineDataConfig;
}
