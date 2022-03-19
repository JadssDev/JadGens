package dev.jadss.jadgens.api.config.interfaces;

import dev.jadss.jadgens.api.config.generalConfig.Permissions;
import dev.jadss.jadgens.api.config.generalConfig.messages.MessagesConfiguration;

public interface GeneralConfiguration {

    /**
     * Remove invalid machines!
     * @return if we should remove machines which aren't valid!
     */
    boolean removeInvalidMachines();

    /**
     * Protect generators from being destroyed!
     * @return if we should protect the generators.
     */
    boolean protectMachines();

    /**
     * Prevent crafting with generator items.
     * @return if we should prevent it.
     */
    boolean preventCrafting();

    /**
     * Should the generators be able to produce items even if their owner is offline?
     * @return if we should allow such behaviour.
     */
    boolean produceEvenIfOffline();

    /**
     * Should we stop producing if the generators are in unloaded chunks?
     * <p><b>Note:</b> has a major impact in performance!</p>
     * @return if we should stop producing.
     */
    boolean stopProducingInUnloadedChunks();

    /**
     * Get the messages the plugin should use!
     * @return the messages!
     */
    MessagesConfiguration getMessages();

    /**
     * Get the permissions the plugin should use for the commands and others.
     * @return the permissions!
     */
    Permissions getPermissions();
}
