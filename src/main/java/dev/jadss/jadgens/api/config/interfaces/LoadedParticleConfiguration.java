package dev.jadss.jadgens.api.config.interfaces;

import dev.jadss.jadapi.bukkitImpl.enums.JParticle;

public interface LoadedParticleConfiguration {

    /**
     * Represents the Machine configuration these configurations are under.
     * @return the config under this config.
     */
    LoadedMachineConfiguration getSuperConfiguration();


    /**
     * Check if particles are enabled!
     * @return true if yes, false otherwise.
     */
    boolean isParticlesEnabled();

    /**
     * Show the particles when a machine produces?
     * @return if it should show the particles.
     */
    boolean showOnProduce();

    /**
     * Show the particles when a machine is placed.
     * @return if it should show the particles.
     */
    boolean showOnPlace();

    /**
     * Show the particles when a machine is broken!
     * @return if it should show the particles.
     */
    boolean showOnBreak();

    /**
     * Get the particle to display.
     * @return the particle.
     */
    JParticle getParticle();

    /**
     * The amount of particles to display.
     * @return the amount of particles.
     */
    int getParticleCount();

    /**
     * The speed of the particles, meaning how fast they should move from where they spawned!
     * @return the speed.
     */
    int getParticleSpeed();

    /**
     * How many times to display this particle per row.
     * @return the amount of times to display.
     */
    int getParticleTimes();

    /**
     * Gets how many rows vertically this particle should have.
     * @return the amount of rows.
     */
    int getParticleRows();
}
