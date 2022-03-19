package dev.jadss.jadgens.implementations.config;

import dev.jadss.jadapi.bukkitImpl.enums.JParticle;
import dev.jadss.jadgens.api.config.interfaces.LoadedMachineConfiguration;
import dev.jadss.jadgens.api.config.interfaces.LoadedParticleConfiguration;

public class LoadedParticleConfigurationImpl implements LoadedParticleConfiguration {

    private final LoadedMachineConfiguration machineConfiguration;

    private final boolean enabled;

    private final boolean showOnProduce, showOnPlace, showOnBreak;

    private final JParticle particle;

    private final int count;
    private final int speed;

    private final int times;
    private final int rows;

    public LoadedParticleConfigurationImpl(LoadedMachineConfiguration configuration) {
        this.machineConfiguration = configuration;

        this.enabled = configuration.getSuperConfiguration().particleConfiguration.enabled;

        if (this.enabled) {
            this.particle = JParticle.valueOf(configuration.getSuperConfiguration().particleConfiguration.particleType);

            this.showOnProduce = configuration.getSuperConfiguration().particleConfiguration.showOnProduce;
            this.showOnPlace = configuration.getSuperConfiguration().particleConfiguration.showOnPlace;
            this.showOnBreak = configuration.getSuperConfiguration().particleConfiguration.showOnBreak;

            this.count = configuration.getSuperConfiguration().particleConfiguration.particleCount;
            this.speed = configuration.getSuperConfiguration().particleConfiguration.speed;

            this.times = configuration.getSuperConfiguration().particleConfiguration.times;
            this.rows = configuration.getSuperConfiguration().particleConfiguration.rows;
        } else {
            this.particle = null;

            this.showOnProduce = false;
            this.showOnPlace = false;
            this.showOnBreak = false;

            this.count = 0;
            this.speed = 0;

            this.times = 0;
            this.rows = 0;
        }
    }

    @Override
    public LoadedMachineConfiguration getSuperConfiguration() {
        return machineConfiguration;
    }

    @Override
    public boolean isParticlesEnabled() {
        return enabled;
    }

    @Override
    public boolean showOnProduce() {
        return showOnProduce;
    }

    @Override
    public boolean showOnPlace() {
        return showOnPlace;
    }

    @Override
    public boolean showOnBreak() {
        return showOnBreak;
    }

    @Override
    public JParticle getParticle() {
        return particle;
    }

    @Override
    public int getParticleCount() {
        return count;
    }

    @Override
    public int getParticleSpeed() {
        return speed;
    }

    @Override
    public int getParticleTimes() {
        return times;
    }

    @Override
    public int getParticleRows() {
        return rows;
    }
}
