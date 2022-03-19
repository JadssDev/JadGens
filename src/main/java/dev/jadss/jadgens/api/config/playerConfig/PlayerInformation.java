package dev.jadss.jadgens.api.config.playerConfig;

import dev.jadss.jadgens.api.config.serializers.Information;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PlayerInformation implements Information {

    public final long leastSigUUID;
    public final long mostSigUUID;

    public final MachineDropsPlayerInformation[] drops;
    public final int xpToCollect;

    public PlayerInformation() {
        this.leastSigUUID = -1;
        this.mostSigUUID = -1;
        this.drops = null;
        this.xpToCollect = -1;
    }
}
