package dev.jadss.jadgens.api.config.machineConfig.misc;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@JsonPropertyOrder(value = { "enabled", "showOnProduce", "showOnPlace", "showOnBreak", "particleType", "particleCount", "speed", "radius", "times", "rows" })
public class MachineParticleConfiguration implements Configuration {

    public final boolean enabled;

    public final boolean showOnProduce;
    public final boolean showOnPlace;
    public final boolean showOnBreak;

    public final String particleType;
    public final int particleCount;
    public final int speed;

    public final int times;
    public final int rows;

    public MachineParticleConfiguration() {
        this(false, false, false, false, null, 0, 0, 0, 0);
    }
}
