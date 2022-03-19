package dev.jadss.jadgens.implementations;

import dev.jadss.jadapi.JadAPIPlugin;
import dev.jadss.jadapi.bukkitImpl.item.JInventory;
import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.management.JQuickEvent;
import dev.jadss.jadgens.JadGens;
import dev.jadss.jadgens.api.MachinesAPI;
import dev.jadss.jadgens.api.MenusManager;
import dev.jadss.jadgens.api.ShopEconomy;
import dev.jadss.jadgens.api.config.generalConfig.messages.menu.DropsMenuConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.messages.menu.MenuItemConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.messages.menu.ShopMenuConfiguration;
import dev.jadss.jadgens.api.config.interfaces.LoadedFuelConfiguration;
import dev.jadss.jadgens.api.config.interfaces.LoadedMachineConfiguration;
import dev.jadss.jadgens.api.player.MachinesUser;
import dev.jadss.jadgens.api.player.UserMachineDrops;
import dev.jadss.jadgens.hooks.Hook;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static dev.jadss.jadgens.utils.Utilities.replace;

public class MenusManagerImpl implements MenusManager {


    //SHOP
    private final boolean isShopEnabled;
    private final boolean separateShops;
    private final String shopTitle;

    private final int mainInventoryRows;

    private final int machinesInventoryRows;
    private final int fuelsInventoryRows;

    private final int machinesMainInventoryItemSlot;
    private final JItemStack machinesMainInventoryItem;
    private final int fuelsMainInventoryItemSlot;
    private final JItemStack fuelsMainInventoryItem;

    private final JItemStack backgroundShopItem;

    //lores
    private final List<String> shopLoreEconomy;
    private final List<String> shopLoreExperience;
    private final List<String> shopLorePoints;

    //DROPS
    private final String dropsTitle;
    private final int dropsInventoryRows;

    private final JItemStack backgroundDropsItem;

    //lores
    private final List<String> dropsLore;

    public MenusManagerImpl(ShopMenuConfiguration shopMenuConfiguration, DropsMenuConfiguration dropsMenuConfiguration) {
        isShopEnabled = shopMenuConfiguration.enabled;

        if (!isShopEnabled) {
            separateShops = false;
            shopTitle = null;

            mainInventoryRows = 0;

            machinesInventoryRows = 0;
            fuelsInventoryRows = 0;

            machinesMainInventoryItemSlot = 0;
            machinesMainInventoryItem = null;
            fuelsMainInventoryItemSlot = 0;
            fuelsMainInventoryItem = null;

            backgroundShopItem = null;

            shopLoreEconomy = null;
            shopLoreExperience = null;
            shopLorePoints = null;
        } else {
            separateShops = shopMenuConfiguration.separateFuelsAndMachines;
            shopTitle = shopMenuConfiguration.title;

            mainInventoryRows = shopMenuConfiguration.inventoryRows;

            machinesInventoryRows = shopMenuConfiguration.machinesInventoryRows;
            fuelsInventoryRows = shopMenuConfiguration.fuelsInventoryRows;

            MenuItemConfiguration machinesChooseItem = shopMenuConfiguration.machinesChooseItem;

            machinesMainInventoryItemSlot = machinesChooseItem.slot;
            machinesMainInventoryItem = new JItemStack(JMaterial.getRegistryMaterials().find(machinesChooseItem.itemType))
                    .setDisplayName(machinesChooseItem.displayName)
                    .setLore(machinesChooseItem.lore)
                    .setNBTBoolean("Choose_Machines", true);
            if (machinesChooseItem.glow)
                machinesMainInventoryItem.addEnchantment(JadGens.getInstance().getGlowEnchantment().asEnchantment(), 69);


            MenuItemConfiguration fuelsChooseItem = shopMenuConfiguration.fuelsChooseItem;

            fuelsMainInventoryItemSlot = fuelsChooseItem.slot;
            fuelsMainInventoryItem = new JItemStack(JMaterial.getRegistryMaterials().find(fuelsChooseItem.itemType))
                    .setDisplayName(fuelsChooseItem.displayName)
                    .setLore(fuelsChooseItem.lore)
                    .setNBTBoolean("Choose_Fuels", true);
            if (fuelsChooseItem.glow)
                fuelsMainInventoryItem.addEnchantment(JadGens.getInstance().getGlowEnchantment().asEnchantment(), 69);


            backgroundShopItem = new JItemStack(JMaterial.getRegistryMaterials().find(shopMenuConfiguration.backgroundItemType))
                    .setDisplayName(" ");

            shopLoreEconomy = Arrays.asList(shopMenuConfiguration.loreBuyWithEconomy);
            shopLoreExperience = Arrays.asList(shopMenuConfiguration.loreBuyWithExperience);
            shopLorePoints = Arrays.asList(shopMenuConfiguration.loreBuyWithPoints);
        }

        dropsTitle = dropsMenuConfiguration.title;
        dropsInventoryRows = dropsMenuConfiguration.inventoryRows;

        backgroundDropsItem = new JItemStack(JMaterial.getRegistryMaterials()
                .find(dropsMenuConfiguration.backgroundItemType))
                .setDisplayName(" ");

        dropsLore = Arrays.asList(dropsMenuConfiguration.loreAmount);

    }

    public JInventory getShopInventory(ShopType type) {
        return new JInventory((type == ShopType.MAIN ? mainInventoryRows : type == ShopType.MACHINES ? machinesInventoryRows : fuelsInventoryRows), shopTitle).fill(backgroundShopItem);
    }

    public JInventory getDropsInventory() {
        return new JInventory(dropsInventoryRows, dropsTitle).fill(backgroundDropsItem);
    }

    @Override
    public boolean isShopEnabled() {
        return isShopEnabled;
    }

    @Override
    public boolean areShopsSeparated() {
        return separateShops;
    }

    private final HashMap<UUID, Long> builtInDelay = new HashMap<>();
    private final List<UUID> bypassBuiltInDelay = new ArrayList<>();
    private final HashMap<UUID, Long> clickDelay = new HashMap<>();

    @Override
    public void openShopMenu(ShopType type, Player player, Runnable preOpen, Runnable postOpen, Runnable postClose) {
        if (!isShopEnabled)
            throw new RuntimeException("Shop is not enabled, and cannot be opened");

        if (builtInDelay.containsKey(player.getUniqueId()) && builtInDelay.get(player.getUniqueId()) > System.currentTimeMillis() && !bypassBuiltInDelay.contains(player.getUniqueId()))
            return;
        builtInDelay.put(player.getUniqueId(), System.currentTimeMillis() + 2500);

        if (!separateShops)
            if (type != ShopType.MAIN)
                throw new RuntimeException("Shop is not separated, and cannot be opened with type " + type + ", only with ShopType.MAIN!");

        JInventory shopInventory = getShopInventory(type);

        if (type == ShopType.MAIN) {
            if (separateShops) {
                shopInventory.setItem(machinesMainInventoryItemSlot, machinesMainInventoryItem);
                shopInventory.setItem(fuelsMainInventoryItemSlot, fuelsMainInventoryItem);
            } else {
                setMachinesIntoInventory(shopInventory);
                setFuelsIntoInventory(shopInventory);
            }
        } else if (type == ShopType.MACHINES) {
            setMachinesIntoInventory(shopInventory);
        } else if (type == ShopType.FUELS) {
            setFuelsIntoInventory(shopInventory);
        }

        if (preOpen != null)
            preOpen.run();

        player.openInventory(shopInventory.getInventory());

        if (postOpen != null)
            postOpen.run();

        String quickEventId1 = JQuickEvent.generateID();
        String quickEventId2 = JQuickEvent.generateID();
        String quickEventId3 = JQuickEvent.generateID();

        new JQuickEvent(JadAPIPlugin.get(JadGens.class), InventoryClickEvent.class, e -> {
            if (e.isCancelled())
                return;

            if (e.getWhoClicked().getUniqueId().equals(player.getUniqueId())) {
                e.setCancelled(true);
                if (e.getClickedInventory() != null && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    JItemStack item = new JItemStack(e.getCurrentItem());
                    if (item.getNBTBoolean("Choose_Machines")) {
                        JQuickEvent.getQuickEvent(quickEventId1).register(false);
                        JQuickEvent.getQuickEvent(quickEventId2).register(false);
                        JQuickEvent.getQuickEvent(quickEventId3).register(false);

                        player.closeInventory();

                        bypassBuiltInDelay.add(player.getUniqueId());
                        this.openShopMenu(ShopType.MACHINES, player, null, null, () -> {
                            bypassBuiltInDelay.remove(player.getUniqueId());
                            if(postClose != null)
                                postClose.run();
                        });
                    } else if (item.getNBTBoolean("Choose_Fuels")) {
                        JQuickEvent.getQuickEvent(quickEventId1).register(false);
                        JQuickEvent.getQuickEvent(quickEventId2).register(false);
                        JQuickEvent.getQuickEvent(quickEventId3).register(false);

                        player.closeInventory();

                        bypassBuiltInDelay.add(player.getUniqueId());
                        this.openShopMenu(ShopType.FUELS, player, null, null, () -> {
                            bypassBuiltInDelay.remove(player.getUniqueId());
                            if(postClose != null)
                                postClose.run();
                        });
                    } else if (item.getNBTBoolean("Shop_Fuel_Item")) {
                        LoadedFuelConfiguration fuel = MachinesAPI.getInstance().getFuelConfiguration(item.getNBTString("Configuration"));
                        int slot = player.getInventory().firstEmpty();
                        if (slot == -1) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().shopMessages.noInventorySlot));
                            return;
                        }

                        if (clickDelay.containsKey(player.getUniqueId()) && clickDelay.get(player.getUniqueId()) > System.currentTimeMillis())
                            return;
                        clickDelay.put(player.getUniqueId(), System.currentTimeMillis() + 50);

                        if (hasEnoughCreds(player, fuel.getSuperConfiguration().shop.economyType, fuel.getSuperConfiguration().shop.cost)) {
                            player.getInventory().addItem(fuel.getItem());
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().shopMessages.purchaseSuccessful));
                        }
                    } else if (item.getNBTBoolean("Shop_Machine_Item")) {
                        LoadedMachineConfiguration machine = MachinesAPI.getInstance().getMachineConfiguration(item.getNBTString("Configuration"));
                        int slot = player.getInventory().firstEmpty();
                        if (slot == -1) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().shopMessages.noInventorySlot));
                            return;
                        }

                        if (clickDelay.containsKey(player.getUniqueId()) && clickDelay.get(player.getUniqueId()) > System.currentTimeMillis())
                            return;
                        clickDelay.put(player.getUniqueId(), System.currentTimeMillis() + 50);

                        if (hasEnoughCreds(player, machine.getSuperConfiguration().shop.economyType, machine.getSuperConfiguration().shop.cost)) {
                            player.getInventory().addItem(machine.getMachineItem());
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().shopMessages.purchaseSuccessful));
                        }
                    }
                }
            }
        }, EventPriority.MONITOR, -1, -1, quickEventId1).register(true);


        new JQuickEvent(JadAPIPlugin.get(JadGens.class), InventoryCloseEvent.class, e -> {
            if (e.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                JQuickEvent.getQuickEvent(quickEventId1).register(false);
                JQuickEvent.getQuickEvent(quickEventId2).register(false);
                JQuickEvent.getQuickEvent(quickEventId3).register(false);
                if (postClose != null)
                    postClose.run();
            }
        }, EventPriority.MONITOR, -1, -1, quickEventId2).register(true);


        new JQuickEvent(JadAPIPlugin.get(JadGens.class), PlayerQuitEvent.class, e -> {
            if (e.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                JQuickEvent.getQuickEvent(quickEventId1).register(false);
                JQuickEvent.getQuickEvent(quickEventId2).register(false);
                JQuickEvent.getQuickEvent(quickEventId3).register(false);
                if (postClose != null)
                    postClose.run();
            }
        }, EventPriority.MONITOR, -1, -1, quickEventId3).register(true);

    }

    @Override
    public void openDropsMenu(Player player, Runnable preOpen, Runnable postOpen, Runnable postClose) {
        JInventory inventory = new JInventory(dropsInventoryRows, dropsTitle)
                .fill(backgroundDropsItem);

        MachinesUser user = MachinesAPI.getInstance().getPlayer(player.getUniqueId());

        if(user == null)
            throw new RuntimeException("User is null?? Blunder.");

        List<UserMachineDrops> drops = user.getAllDropsInformation();

        int taskId = Bukkit.getScheduler().runTaskTimer(JadGens.getInstance(), () -> {
            inventory.fill(backgroundDropsItem);

            for (int i = 0; i < drops.size(); i++) {
                UserMachineDrops drop = drops.get(i);
                int itemAmount = (int) (drop.getAmount() / 1024);
                itemAmount = (itemAmount <= 0 ? 1 : itemAmount);
                JItemStack item = drop.getMachineConfiguration().getSuperMachineItem()
                        .setAmount(itemAmount)
                        .setLore(replace(this.dropsLore, "%amount%", "" + drop.getAmount()))
                        .setNBTBoolean("Drop_Item", true)
                        .setNBTString("Drop_Item_Type", drop.getMachineConfiguration().getConfigurationName());

                inventory.setItem(i, item);
            }
        }, 30L, 15L).getTaskId();

        for (int i = 0; i < drops.size(); i++) {
            UserMachineDrops drop = drops.get(i);
            int itemAmount = (int) (drop.getAmount() / 1024);
            itemAmount = (itemAmount <= 0 ? 1 : itemAmount);
            JItemStack item = drop.getMachineConfiguration().getSuperMachineItem()
                    .setAmount(itemAmount)
                    .setLore(replace(this.dropsLore, "%amount%", "" + drop.getAmount()))
                    .setNBTBoolean("Drop_Item", true)
                    .setNBTString("Drop_Item_Type", drop.getMachineConfiguration().getConfigurationName());

            inventory.setItem(i, item);
        }

        if (preOpen != null)
            preOpen.run();

        player.openInventory(inventory.getInventory());

        if (postOpen != null)
            postOpen.run();

        String quickEventId1 = JQuickEvent.generateID();
        String quickEventId2 = JQuickEvent.generateID();
        String quickEventId3 = JQuickEvent.generateID();

        new JQuickEvent(JadAPIPlugin.get(JadGens.class), InventoryClickEvent.class, e -> {
            if (e.isCancelled())
                return;

            if (e.getWhoClicked().getUniqueId().equals(player.getUniqueId())) {
                e.setCancelled(true);
                if (e.getClickedInventory() != null && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    JItemStack item = new JItemStack(e.getCurrentItem());
                    if (item.getNBTBoolean("Drop_Item")) {
                        String machineConfigurationName = item.getNBTString("Drop_Item_Type");
                        LoadedMachineConfiguration configuration = MachinesAPI.getInstance().getMachineConfiguration(machineConfigurationName);
                        UserMachineDrops drop = user.getDropInformation(configuration);

                        if (player.getInventory().firstEmpty() == -1) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().shopMessages.noInventorySlot));
                            return;
                        }

                        if (e.getClick() == ClickType.LEFT || e.getClick() == ClickType.SHIFT_LEFT) {
                            if (drop.hasAtLeast(1)) {
                                drop.removeAmount(1);
                                HashMap<Integer, ItemStack> map = player.getInventory().addItem(configuration.getProductionConfiguration().getProduceItem().setAmount(1).buildItemStack());
                                if (!map.isEmpty()) // fail safe...
                                    drop.addAmount(1);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().dropsMessages.dropsCollected.replace("%amount%", "" + 1)));
                            } else player.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().dropsMessages.notEnoughDropsToCollect));
                        } else if (e.getClick() == ClickType.RIGHT) {
                            if (drop.hasAtLeast(64)) {
                                drop.removeAmount(64);
                                HashMap<Integer, ItemStack> map = player.getInventory().addItem(configuration.getProductionConfiguration().getProduceItem().setAmount(64).buildItemStack());
                                if (!map.isEmpty()) // fail safe...
                                    drop.addAmount(64);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().dropsMessages.dropsCollected.replace("%amount%", "" + 64)));
                            } else player.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().dropsMessages.notEnoughDropsToCollect));
                        } else if (e.getClick() == ClickType.SHIFT_RIGHT) {
                            int amountOfItems = 0;
                            while (player.getInventory().firstEmpty() != -1 && drop.getAmount() > 0) {
                                int amount = drop.getAmount() >= 64 ? 64 : (int) drop.getAmount();
                                drop.removeAmount(amount);
                                amountOfItems += amount;
                                HashMap<Integer, ItemStack> map = player.getInventory().addItem(configuration.getProductionConfiguration().getProduceItem().setAmount(amount).buildItemStack());
                                if (!map.isEmpty()) // fail safe...
                                    drop.addAmount(amount);
                            }
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().dropsMessages.dropsCollected.replace("%amount%", "" + amountOfItems)));
                        }
                    }
                }
            }
        }, EventPriority.MONITOR, -1, -1, quickEventId1).register(true);

        new JQuickEvent(JadAPIPlugin.get(JadGens.class), InventoryCloseEvent.class, e -> {
            if (e.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                JQuickEvent.getQuickEvent(quickEventId1).register(false);
                JQuickEvent.getQuickEvent(quickEventId2).register(false);
                JQuickEvent.getQuickEvent(quickEventId3).register(false);
                Bukkit.getScheduler().cancelTask(taskId);
                if (postClose != null)
                    postClose.run();
            }
        }, EventPriority.MONITOR, -1, -1, quickEventId2).register(true);


        new JQuickEvent(JadAPIPlugin.get(JadGens.class), PlayerQuitEvent.class, e -> {
            if (e.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                JQuickEvent.getQuickEvent(quickEventId1).register(false);
                JQuickEvent.getQuickEvent(quickEventId2).register(false);
                JQuickEvent.getQuickEvent(quickEventId3).register(false);
                Bukkit.getScheduler().cancelTask(taskId);
                if (postClose != null)
                    postClose.run();
            }
        }, EventPriority.MONITOR, -1, -1, quickEventId3).register(true);
    }

    public boolean hasEnoughCreds(Player player, ShopEconomy type, int cost) {
        boolean hasEnough = false;
        switch (type) {
            case ECONOMY: {
                Hook hook = JadGens.getInstance().getHookByName("Vault");
                if (hook.isAvailable()) {
                    if (((Economy) hook.getHook()).has(player, cost)) {
                        hasEnough = true;
                        ((Economy) hook.getHook()).withdrawPlayer(player, cost);
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().shopMessages.notEnoughEconomy));
                    }
                } else throw new RuntimeException("Vault is not available Fix it!");
            }
            case EXPERIENCE: {
                if (player.getLevel() >= cost) {
                    hasEnough = true;
                    player.setLevel(player.getLevel() - cost);
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().shopMessages.notEnoughExperience));
                }
                break;
            }
            case POINTS: {
                Hook hook = JadGens.getInstance().getHookByName("playerpoints");
                if (hook.isAvailable()) {
                    if (((PlayerPointsAPI) hook.getHook()).take(player.getUniqueId(), cost)) {
                        hasEnough = true;
                    } else
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().shopMessages.notEnoughPoints));
                } else throw new RuntimeException("PlayerPoints is not available Fix it!");
                break;
            }
            default: {
                throw new RuntimeException("Unknown Economy error....");
            }
        }
        return hasEnough;
    }

    public void setMachinesIntoInventory(JInventory inventory) {
        for (LoadedMachineConfiguration machines : MachinesAPI.getInstance().getMachineConfigurations()) {
            if (!machines.getSuperConfiguration().shop.showInShop)
                continue;

            JItemStack item = machines.getSuperMachineItem();

            List<String> lore = item.getLore();
            switch (machines.getSuperConfiguration().shop.economyType) {
                case ECONOMY: {
                    lore.addAll(replace(shopLoreEconomy, "%amount%", "" + machines.getSuperConfiguration().shop.cost));
                    break;
                }
                case EXPERIENCE: {
                    lore.addAll(replace(shopLoreExperience, "%amount%", "" + machines.getSuperConfiguration().shop.cost));
                    break;
                }
                case POINTS: {
                    lore.addAll(replace(shopLorePoints, "%amount%", "" + machines.getSuperConfiguration().shop.cost));
                    break;
                }
                default: {
                    throw new RuntimeException("Unknown economy type: " + machines.getSuperConfiguration().shop.economyType);
                }
            }
            item.setLore(lore);
            item
                    .setNBTBoolean("Shop_Machine_Item", true)
                    .setNBTString("Configuration", machines.getConfigurationName());

            inventory.setItem(machines.getSuperConfiguration().shop.slot, item);
        }
    }

    public void setFuelsIntoInventory(JInventory inventory) {
        for (LoadedFuelConfiguration fuels : MachinesAPI.getInstance().getFuelConfigurations()) {
            if (!fuels.getSuperConfiguration().shop.showInShop)
                continue;

            JItemStack item = fuels.getSuperItem();

            List<String> lore = item.getLore();
            switch (fuels.getSuperConfiguration().shop.economyType) {
                case ECONOMY: {
                    lore.addAll(replace(shopLoreEconomy, "%amount%", "" + fuels.getSuperConfiguration().shop.cost));
                    break;
                }
                case EXPERIENCE: {
                    lore.addAll(replace(shopLoreExperience, "%amount%", "" + fuels.getSuperConfiguration().shop.cost));
                    break;
                }
                case POINTS: {
                    lore.addAll(replace(shopLorePoints, "%amount%", "" + fuels.getSuperConfiguration().shop.cost));
                    break;
                }
                default: {
                    throw new RuntimeException("Unknown economy type: " + fuels.getSuperConfiguration().shop.economyType);
                }
            }
            item.setLore(lore);
            item
                    .setNBTBoolean("Shop_Fuel_Item", true)
                    .setNBTString("Configuration", fuels.getConfigurationName());

            inventory.setItem(fuels.getSuperConfiguration().shop.slot, item);
        }
    }


}
