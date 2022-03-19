package dev.jadss.jadgens.api.config.serializers;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class MachineInformation implements Information {

    public final String id;
    public final String type;
    public final UUID owner;
    public final boolean enabled;
    public final int ticksToGenerate;
    public final int fuelAmount;

    public MachineInformation() {
        this(null, null, null, false, 0, 0);
    }
}
