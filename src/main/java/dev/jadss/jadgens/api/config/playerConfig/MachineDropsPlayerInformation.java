package dev.jadss.jadgens.api.config.playerConfig;

import dev.jadss.jadgens.api.config.serializers.Information;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MachineDropsPlayerInformation implements Information {

    public final String machineID;
    public final long amount;

    public MachineDropsPlayerInformation() {
        this.machineID = null;
        this.amount = 0;
    }
}
