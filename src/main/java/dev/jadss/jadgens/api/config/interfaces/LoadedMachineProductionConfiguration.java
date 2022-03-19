package dev.jadss.jadgens.api.config.interfaces;

import dev.jadss.jadapi.bukkitImpl.item.JItemStack;

import java.util.List;

/**
 * Represents the production config!
 */
public interface LoadedMachineProductionConfiguration {

    /**
     * Represents the Machine configuration these configurations are under.
     * @return the config under this config.
     */
    LoadedMachineConfiguration getSuperConfiguration();


    //Checkers

    /**
     * Does this machine produce items?
     * @return true if it does.
     */
    boolean producesItem();

    /**
     * Does the machine produce items and send them to the player menu?
     * @return true if it does, false if it doesn't.
     */
    boolean sendItemToMenu();

    /**
     * Does this machine produce items?
     * @return true if it does.
     */
    boolean producesEconomy();

    /**
     * Does this machine produce experience?
     * @return true if it does.
     */
    boolean producesExperience();

    /**
     * Does this machine produce points?
     * @return true if it does.
     */
    boolean producesPoints();

    /**
     * Does this machine use commands when produced?
     * @return true if it does.
     */
    boolean usesCommands();


    //Getters

    /**
     * The item that is produced.
     * @return the item, or null if it doesn't produce items.
     */
    JItemStack getProduceItem();

    /**
     * Get the amount of economy that is produced.
     * @return the amount of economy, or -1 if it doesn't produce economy.
     */
    double getEconomyAmount();

    /**
     * Get the amount of experience that is produced.
     * @return the amount of experience, or -1 if it doesn't produce experience.
     */
    int getExperienceAmount();

    /**
     * Get the amount of points that is produced.
     * @return the amount of points, or -1 if it doesn't produce points.
     */
    int getPointsAmount();

    /**
     * Get the commands that are executed when produced.
     * @return the commands, or null if it doesn't use commands.
     */
    List<String> getCommands();
}
