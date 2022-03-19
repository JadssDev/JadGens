package dev.jadss.jadgens.api;

import org.bukkit.entity.Player;

public interface MenusManager {

    /**
     * Checks if the shop is enabled!
     * @return if the shop is enabled.
     */
    boolean isShopEnabled();

    /**
     * Checks if the shops are separated.
     * @return if the shops are separated.
     */
    boolean areShopsSeparated();

    /**
     * Opens a menu for the player to buy machines!
     * @param player The player to open the menu for.
     * @param type the type of shop to open for this player!
     *
     * @param preOpen executes before the inventory opens.
     * @param postOpen executes after the inventory opens.
     *
     * @param postClose executes after the player closes the inventory, he may be offline if he disconnected! be careful.
     * @throws NullPointerException if the player is null.
     * @throws RuntimeException if shop is not enabled!
     */
    void openShopMenu(ShopType type, Player player, Runnable preOpen, Runnable postOpen, Runnable postClose);

    /**
     * Opens a menu for the player to check his drops!
     * @param player The player to open the menu for.
     *
     * @param preOpen executes before the inventory opens.
     * @param postOpen executes after the inventory opens.
     *
     * @param postClose executes after the player closes the inventory, he may be offline if he disconnected! be careful.
     */
    void openDropsMenu(Player player, Runnable preOpen, Runnable postOpen, Runnable postClose);

    enum ShopType {
        /**
         * The shop where players can decide what they want to buy.
         */
        MAIN,
        /**
         * The shop where players can buy machines.
         */
        MACHINES,
        /**
         * The shop where players can buy fuels.
         */
        FUELS;
    }
}
