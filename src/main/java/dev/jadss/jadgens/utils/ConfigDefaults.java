package dev.jadss.jadgens.utils;

import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadgens.api.ShopEconomy;
import dev.jadss.jadgens.api.config.CraftingIngredient;
import dev.jadss.jadgens.api.config.RecipeConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.Permissions;
import dev.jadss.jadgens.api.config.generalConfig.limits.LimitGroup;
import dev.jadss.jadgens.api.config.generalConfig.limits.MachineLimits;
import dev.jadss.jadgens.api.config.generalConfig.messages.*;
import dev.jadss.jadgens.api.config.fuelConfig.FuelConfiguration;
import dev.jadss.jadgens.api.config.fuelConfig.FuelItemConfiguration;
import dev.jadss.jadgens.api.config.fuelConfig.FuelShopConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.GeneralConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.messages.commands.*;
import dev.jadss.jadgens.api.config.generalConfig.messages.menu.*;
import dev.jadss.jadgens.api.config.generalConfig.messages.menu.specifics.MachineCloseItemConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.messages.menu.specifics.MachineFuelItemConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.messages.menu.specifics.MachineOwnerItemConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.messages.menu.specifics.MachineStatusItemConfiguration;
import dev.jadss.jadgens.api.config.machineConfig.*;
import dev.jadss.jadgens.api.config.machineConfig.misc.MachineHologramConfiguration;
import dev.jadss.jadgens.api.config.machineConfig.misc.MachineParticleConfiguration;
import dev.jadss.jadgens.api.config.machineConfig.production.*;
import dev.jadss.jadgens.api.config.machineConfig.production.nbt.MachineItemNBTConfiguration;
import dev.jadss.jadgens.api.config.machineConfig.production.nbt.MachineNBTEntry;
import dev.jadss.jadgens.controller.ConfigVersions;

public class ConfigDefaults {

    private ConfigDefaults() {

    }

    public static MachineConfiguration[] defaultMachineConfigurations() {
        MachineConfiguration[] machines = new MachineConfiguration[4];
        {
            MachineItemConfiguration item = new MachineItemConfiguration("&a&lLevel &3Gen", new String[]{"&eThis is a &a&lLevel &b&lMachine&e!", "&eIt gives you &35 &bLevels &eof &3XP&e!"}, true);
            MachineShopConfiguration shop = new MachineShopConfiguration(true, 50, ShopEconomy.EXPERIENCE, 0);
            MachineFuelConfiguration fuel = new MachineFuelConfiguration(true, 150, false, new String[]{});

            MachineProductItemConfiguration productItemConfiguration = new MachineProductItemConfiguration(false, null, false);
            MachineExperienceConfiguration experienceConfiguration = new MachineExperienceConfiguration(true, 5);
            MachineEconomyConfiguration economyConfiguration = new MachineEconomyConfiguration(false, 0);
            MachinePointsConfiguration pointsConfiguration = new MachinePointsConfiguration(false, 0);
            MachineCommandsConfiguration commandsConfiguration = new MachineCommandsConfiguration(false, new String[]{});

            MachineProductionConfiguration production = new MachineProductionConfiguration(productItemConfiguration, experienceConfiguration, economyConfiguration, pointsConfiguration, commandsConfiguration);

            MachineHologramConfiguration holograms = new MachineHologramConfiguration(true, new String[] {
                    "&a&m----------------------------",
                    "&a&lLevel &3Gen",
                    "&aOwner: &b%owner%",
                    "&aStatus: &b%status%",
                    "&aFuel: &b%fuel%/%max%",
                    "&a&m----------------------------"
            }, "&a&lEnabled", "&c&lDisabled");

            MachineParticleConfiguration particles = new MachineParticleConfiguration(true, true, false, false, "CRIT_MAGIC", 25, 5, 1, 1);

            RecipeConfiguration recipeConfig;
            {
                String row1 = "LLL";
                String row2 = "LEL";
                String row3 = "GGG";
                CraftingIngredient[] ingredients = new CraftingIngredient[] { new CraftingIngredient('L', "WHITE_STAINED_GLASS"), new CraftingIngredient('E', "GLOWSTONE"),
                        new CraftingIngredient('G', "GOLD_BLOCK") };
                recipeConfig = new RecipeConfiguration(true, row1, row2, row3, ingredients);
            }

            machines[0] = new MachineConfiguration("Level_Generator", 300, "GOLD_BLOCK", recipeConfig, item, shop, fuel, production, holograms, particles);
        }

        {
            MachineItemConfiguration item = new MachineItemConfiguration("&b&lFuel &3Gen", new String[]{"&eThis is a &a&lFuel &b&lMachine&e!", "&eIt gives you &3Fuels &eto use on your &b&lItem generators&e!"}, true);
            MachineShopConfiguration shop = new MachineShopConfiguration(true, 150, ShopEconomy.EXPERIENCE, 8);
            MachineFuelConfiguration fuel = new MachineFuelConfiguration(true, 150, false, new String[]{});
            MachineItemNBTConfiguration nbt = new MachineItemNBTConfiguration(true, new MachineNBTEntry[]{
                    new MachineNBTEntry("JadGens_Fuel", true, MachineItemNBTConfiguration.NBTType.BOOLEAN),
                    new MachineNBTEntry("JadGens_Fuel_Type", "ItemGenerator_Fuel", MachineItemNBTConfiguration.NBTType.STRING)
            });
            ProductItemConfiguration productItem = new ProductItemConfiguration("GOLD_INGOT", 1, "&6&lItem generator &bFuel", new String[]{"&eThis &3&lfuel &egives your &b&lItem Generator machines", "&32 &3Drops&e!", "&e", "&bRight-click &eto &a&luse&e!", "&b&lLeft-click &eto &a&lMass &buse&e!"}, true, nbt);

            MachineProductItemConfiguration productItemConfiguration = new MachineProductItemConfiguration(true, productItem, false);
            MachineExperienceConfiguration experienceConfiguration = new MachineExperienceConfiguration(false, 0);
            MachineEconomyConfiguration economyConfiguration = new MachineEconomyConfiguration(false, 0);
            MachinePointsConfiguration pointsConfiguration = new MachinePointsConfiguration(false, 0);
            MachineCommandsConfiguration commandsConfiguration = new MachineCommandsConfiguration(false, new String[]{});

            MachineProductionConfiguration production = new MachineProductionConfiguration(productItemConfiguration, experienceConfiguration, economyConfiguration, pointsConfiguration, commandsConfiguration);

            MachineHologramConfiguration holograms = new MachineHologramConfiguration(true, new String[] {
                    "&b&m----------------------------",
                    "&b&lFuel &3Gen",
                    "&aOwner: &b%owner%",
                    "&aStatus: &b%status%",
                    "&aFuel: &b%fuel%/%max%",
                    "&b&m----------------------------"
            }, "&a&lEnabled", "&c&lDisabled");

            MachineParticleConfiguration particles = new MachineParticleConfiguration(true, true, false, false, "CRIT_MAGIC", 25, 5, 1, 1);

            RecipeConfiguration recipeConfig;
            {
                String row1 = "IEI";
                String row2 = "EDE";
                String row3 = "IEI";
                CraftingIngredient[] ingredients = new CraftingIngredient[] { new CraftingIngredient('E', "EMERALD_BLOCK"), new CraftingIngredient('I', "IRON_BLOCK"),
                        new CraftingIngredient('D', "DIAMOND_BLOCK") };
                recipeConfig = new RecipeConfiguration(true, row1, row2, row3, ingredients);
            }

            machines[1] = new MachineConfiguration("Fuel_Generator", 150, "EMERALD_BLOCK", recipeConfig, item, shop, fuel, production, holograms, particles);
        }

        {
            MachineItemConfiguration item = new MachineItemConfiguration("&b&lDiamond &3Gen", new String[]{"&eThis is a &b&ldiamond machine&e!", "&eIt gives you &2money&e!", "&e", "&eNeeds &b&lSpecialized &3Fuel&e."}, true);
            MachineShopConfiguration shop = new MachineShopConfiguration(true, 5, ShopEconomy.EXPERIENCE, 18);
            MachineFuelConfiguration fuel = new MachineFuelConfiguration(true, 20, true, new String[]{"ItemGenerator_Fuel"});
            ProductItemConfiguration productItem = new ProductItemConfiguration("DIAMOND", 5, "&b&lDiamond", new String[]{"&eCreated by a &b&lMachine&e!"}, true, null);

            MachineProductItemConfiguration productItemConfiguration = new MachineProductItemConfiguration(true, productItem, false);
            MachineExperienceConfiguration experienceConfiguration = new MachineExperienceConfiguration(false, 0);
            MachineEconomyConfiguration economyConfiguration = new MachineEconomyConfiguration(false, 0);
            MachinePointsConfiguration pointsConfiguration = new MachinePointsConfiguration(false, 0);
            MachineCommandsConfiguration commandsConfiguration = new MachineCommandsConfiguration(false, new String[]{});

            MachineProductionConfiguration production = new MachineProductionConfiguration(productItemConfiguration, experienceConfiguration, economyConfiguration, pointsConfiguration, commandsConfiguration);

            MachineHologramConfiguration holograms = new MachineHologramConfiguration(true, new String[] {
                    "&3&m----------------------------",
                    "&b&lDiamond &3Gen",
                    "&aOwner: &b%owner%",
                    "&aStatus: &b%status%",
                    "&aFuel: &b%fuel%/%max%",
                    "&3&m----------------------------"
            }, "&a&lEnabled", "&c&lDisabled");

            MachineParticleConfiguration particles = new MachineParticleConfiguration(true, true, false, false, "CRIT_MAGIC", 25, 5, 1, 1);

            RecipeConfiguration recipeConfig;
            {
                String row1 = "LEL";
                String row2 = "IAD";
                String row3 = "LGL";
                CraftingIngredient[] ingredients = new CraftingIngredient[] { new CraftingIngredient('L', "WHITE_STAINED_GLASS"), new CraftingIngredient('E', "EMERALD_BLOCK"),
                        new CraftingIngredient('D', "DIAMOND_BLOCK"), new CraftingIngredient('G', "GOLD_BLOCK"), new CraftingIngredient('I', "IRON_BLOCK"),
                        new CraftingIngredient('A', "DIAMOND") };
                recipeConfig = new RecipeConfiguration(true, row1, row2, row3, ingredients);
            }

            machines[2] = new MachineConfiguration("Diamond_Generator", 450, "DIAMOND_BLOCK", recipeConfig, item, shop, fuel, production, holograms, particles);
        }

        {
            MachineItemConfiguration item = new MachineItemConfiguration("&6&lLeather &3Gen", new String[]{"&eThis is a &6&lLeather &b&lMachine&e!", "&eIt gives you &3Leather&e!", "&e", "&eNeeds &b&lSpecialized &3Fuel&e."}, true);
            MachineShopConfiguration shop = new MachineShopConfiguration(true, 10, ShopEconomy.EXPERIENCE, 19);
            MachineFuelConfiguration fuel = new MachineFuelConfiguration(true, 50, true, new String[]{"ItemGenerator_Fuel"});
            ProductItemConfiguration productItem = new ProductItemConfiguration("LEATHER", 15, "&6&lLeather", new String[]{"&eCreated by a &b&lMachine&e!"}, false, null);

            MachineProductItemConfiguration productItemConfiguration = new MachineProductItemConfiguration(true, productItem, false);
            MachineExperienceConfiguration experienceConfiguration = new MachineExperienceConfiguration(false, 0);
            MachineEconomyConfiguration economyConfiguration = new MachineEconomyConfiguration(false, 0);
            MachinePointsConfiguration pointsConfiguration = new MachinePointsConfiguration(false, 0);
            MachineCommandsConfiguration commandsConfiguration = new MachineCommandsConfiguration(false, new String[]{});

            MachineProductionConfiguration production = new MachineProductionConfiguration(productItemConfiguration, experienceConfiguration, economyConfiguration, pointsConfiguration, commandsConfiguration);

            MachineHologramConfiguration holograms = new MachineHologramConfiguration(true, new String[] {
                    "&3&m----------------------------",
                    "&6&lLeather &3Gen",
                    "&aOwner: &b%owner%",
                    "&aStatus: &b%status%",
                    "&aFuel: &b%fuel%/%max%",
                    "&3&m----------------------------"
            }, "&a&lEnabled", "&c&lDisabled");

            MachineParticleConfiguration particles = new MachineParticleConfiguration(true, true, false, false, "CRIT_MAGIC", 25, 5, 1, 1);

            RecipeConfiguration recipeConfig;
            {
                String row1 = "LEL";
                String row2 = "IAD";
                String row3 = "LGL";
                CraftingIngredient[] ingredients = new CraftingIngredient[] { new CraftingIngredient('L', "WHITE_STAINED_GLASS"), new CraftingIngredient('E', "EMERALD_BLOCK"),
                        new CraftingIngredient('D', "DIAMOND_BLOCK"), new CraftingIngredient('G', "GOLD_BLOCK"), new CraftingIngredient('I', "IRON_BLOCK"),
                        new CraftingIngredient('A', "HAY_BLOCK") };
                recipeConfig = new RecipeConfiguration(true, row1, row2, row3, ingredients);
            }

            machines[3] = new MachineConfiguration("Leather_Generator", 450, "IRON_BLOCK", recipeConfig, item, shop, fuel, production, holograms, particles);
        }

        return machines;
    }

    public static FuelConfiguration[] defaultFuelConfiguration() {
        FuelConfiguration[] fuels = new FuelConfiguration[4];

        {
            FuelShopConfiguration shop = new FuelShopConfiguration(true, 5, ShopEconomy.EXPERIENCE, 1);
            FuelItemConfiguration item = new FuelItemConfiguration("DIAMOND", "&b&lDiamond &bFuel", new String[]{"&eThis &3&lfuel &egives your &b&lmachine", "&310 &3Drops&e!", "&e", "&bRight-click &eto &a&luse&e!", "&b&lLeft-click &eto &a&lMass use&e!"}, false);
            fuels[0] = new FuelConfiguration("Diamond_Fuel", 10, null, shop, item);
        }

        {
            FuelShopConfiguration shop = new FuelShopConfiguration(true, 15, ShopEconomy.EXPERIENCE, 2);
            FuelItemConfiguration item = new FuelItemConfiguration("EMERALD", "&a&lEmerald &bFuel", new String[]{"&eThis &3&lfuel &egives your &b&lmachine", "&320 &3Drops&e!", "&e", "&bRight-click &eto &a&luse&e!", "&b&lLeft-click &eto &a&lMass use&e!"}, true);
            fuels[1] = new FuelConfiguration("Emerald_Fuel", 20, null, shop, item);
        }

        {
            FuelShopConfiguration shop = new FuelShopConfiguration(false, 0, ShopEconomy.EXPERIENCE, -1);
            FuelItemConfiguration item = new FuelItemConfiguration("GOLD_INGOT", "&6&lItem generator &bFuel", new String[]{"&eThis &3&lfuel &egives your &b&lItem Generator machines", "&32 &3Drops&e!", "&e", "&bRight-click &eto &a&luse&e!", "&b&lLeft-click &eto &a&lMass use&e!"}, true);
            fuels[2] = new FuelConfiguration("ItemGenerator_Fuel", 2, null, shop, item);
        }

        {
            FuelShopConfiguration shop = new FuelShopConfiguration(false, 0, ShopEconomy.EXPERIENCE, -1);
            FuelItemConfiguration item = new FuelItemConfiguration("COAL", "&6&lCrafted Fuel", new String[]{"&eThis is a &6&lCrafted fuel&e!", "&eGives &35 Drops&e!", "&e", "&bRight-click &eto &a&luse&e!", "&b&lLeft-click &eto &a&lMass use&e!"}, false);

            RecipeConfiguration recipeConfig;
            {
                String row1 = "CCC";
                String row2 = "CBC";
                String row3 = "CCC";
                recipeConfig = new RecipeConfiguration(true, row1, row2, row3, new CraftingIngredient[]{new CraftingIngredient('C', "COAL"), new CraftingIngredient('B', "COAL_BLOCK")});
            }

            fuels[3] = new FuelConfiguration("Crafted_Fuel", 5, recipeConfig, shop, item);
        }

        return fuels;
    }

    public static GeneralConfiguration defaultGeneralConfiguration() {
        HelpCommandMessagesConfiguration helpCommand;
        {
            String[] message = {
                    "&3&m--------------------------------",
                    "&3&lJadGens &eby &bJadss&e!",
                    "&3&m--------------------------------",
                    "&3&lCommands&e:",
                    "&3 &7(&b[] &a&m->&a&l Optional &f; &b<> &a&m->&a&l Required&7)",
                    "&3/JadGens &bHelp &7: &eDisplay &c&lTHIS &ehelp message!",
                    "&3/JadGens &bGive &b<Type> &a<ID> &a[Player] &7: &eGives the player a machine or fuel!",
                    "&3/JadGens &bShop &b[Type] &7: &eOpen the &3&lShop&e!",
                    "&3/JadGens &bInfo &7: &eShows &b&linfo &eabout your &3&lMachines&e!",
                    "&3/JadGens &bActions &7: &eDo &b&lMASS ACTIONS &eon &a&lall &eof your &3&lmachines&e!",
                    "&3/JadGens &bVersion &7: &eSee the &bversion &eof the &3&lplugin&e!",
                    "&3&m---------------------------------"
            };

            helpCommand = new HelpCommandMessagesConfiguration(message);
        }

        GiveCommandMessagesConfiguration giveCommand;
        {
            String wrongSyntax = "&c&lWrong syntax! &eUse &b&l/jadgens give <Type> <Id> <Amount> <Player>";
            String[] machineAliases = { "machines", "machine", "m", "mac" };
            String[] fuelAliases = { "fuels", "fuel", "f", "fue" };
            String giveMenuOpened = "&3&lGive menu &eopened!";
            String couldNotFindAnyMatchingAlias = "&eCould not find the type you were looking for!";
            String couldntFindId = "&eCould not find the &3&lID &eyou were looking for!";
            String givenMachine = "&eYou have been &b&lgiven &3%amount% &lmachine(s)&e!";
            String givenMachineTo = "&eYou &b&lgave &3%amount% &lmachine(s)&e to &b%to%&e!";
            String givenFuel = "&eYou have been &b&lgiven &3%amount% &lfuel(s)&e!";
            String givenFuelTo = "&eYou &b&lgave &3%amount% &lfuel(s)&e to &b%to%&e!";
            giveCommand = new GiveCommandMessagesConfiguration(wrongSyntax, machineAliases, fuelAliases, giveMenuOpened, couldNotFindAnyMatchingAlias, couldntFindId, givenMachine, givenMachineTo, givenFuel, givenFuelTo);
        }

        ActionsCommandMessagesConfiguration actionsCommand;
        {
            String wrongSyntax = "&c&lWrong syntax! &eUse &b&l/jadgens actions <type> [player]";
            String[] enableActionAliases = { "enable", "on" };
            String[] disableActionAliases = { "disable", "off" };
            String[] purgeActionAliases = { "purge", "purged", "remove", "clear" };
            String machinesEnabled = "&eYou have &a&lEnabled &ea total of &3%amount% &lmachine(s)&e!";
            String machinesDisabled = "&eYou have &c&lDisabled &ea total of &3%amount% &lmachine(s)&e!";
            String machinesPurged = "&eYou have &c&lPurged &ea total of &3%amount% &lmachine(s)&e!";
            actionsCommand = new ActionsCommandMessagesConfiguration(wrongSyntax, enableActionAliases, disableActionAliases, purgeActionAliases, machinesEnabled, machinesDisabled, machinesPurged);
        }

        ShopCommandMessagesConfiguration shopCommand;
        {
            String wrongSyntax = "&c&lWrong syntax! &eUse &b&l/jadgens shop [type]";
            String openedShop = "&3&lShop &ehas been &aOpened&e!";
            String closedShop = "&3&lShop &ehas been &cClosed&e!";
            String[] machineAliases = { "machines", "machine", "m", "mac" };
            String[] fuelAliases = { "fuels", "fuel", "f", "fue" };
            shopCommand = new ShopCommandMessagesConfiguration(wrongSyntax, openedShop, closedShop, machineAliases, fuelAliases);
        }

        InfoCommandMessagesConfiguration infoCommand;
        {
            String infinite = "&d&lInfinite";
            String[] infoMessage = {
                    "&eYou have &b&l%amount% &3machines &eout of the maximum of &b&l%max%",
                    "&eYou can &bplace &3%remaining% &eto &b&lreach &eyour &3machine maximum",
                    "&eYour &3group&e: %group%"
            };
            infoCommand = new InfoCommandMessagesConfiguration(infoMessage, infinite);
        }

        MachineMenuConfiguration machineMenuConfiguration;
        {
            MachineOwnerItemConfiguration ownerItem = new MachineOwnerItemConfiguration("SKELETON_SKULL", "&bOwner", new String[]{"&eThis &3&lmachine &bowner &eis", "&b&l%owner%&e!"}, "&c&lNone", false, 0);

            MachineFuelItemConfiguration dropsItem;
            {
                String[] lore = new String[]{"&eYour &3&lmachine &ehas:", "&b%remaining% &eout of &b%max% &3Fuel &bremaining&e!", "&e", "&eClick with the &3&lfuels &ein this &bitem &eto", "&b&lfuel &ethis &3&lmachine&e!"};
                String[] infiniteLore = new String[]{"&eYour &3&lmachine &ehas:", "&bInfinite &3Fuel&e!"};
                dropsItem = new MachineFuelItemConfiguration("LIME_DYE", "&3Fuel", lore, infiniteLore, true, 7);
            }

            MachineStatusItemConfiguration statusItem;
            {
                MenuItemConfiguration enabled = new MenuItemConfiguration("EMERALD_BLOCK", "&b&lMachine Status", new String[]{"&eYour &3machine &eis &a&lcurrently&e:", "&7> &a&lEnabled&f!", "&3", "&eClick to &3&lToggle&e!"}, true, 8);
                MenuItemConfiguration disabled = new MenuItemConfiguration("IRON_BLOCK", "&b&lMachine Status", new String[]{"&eYour &3machine &eis &a&lcurrently&e:", "&7> &c&lDisabled&f!", "&3", "&eClick to &3&lToggle&e!"}, false, 8);
                statusItem = new MachineStatusItemConfiguration(enabled, disabled);
            }

            MachineCloseItemConfiguration closeItem = new MachineCloseItemConfiguration("BARRIER", "&c&lClose &eor &b&lRemove", new String[]{"&b&lLeft-click &eto &cClose &ethis &3machine&e.", "&b&lRight-click &eto &c&lRemove &ethis &3machine&e."}, false, false, 4);

            MenuItemConfiguration backgroundItem = new MenuItemConfiguration("CYAN_STAINED_GLASS_PANE", "", new String[] { "" }, false, -1);

            machineMenuConfiguration = new MachineMenuConfiguration("&3&lMachine", 1, backgroundItem, ownerItem, dropsItem, statusItem, closeItem);
        }

        MachineMessagesConfiguration machineMessagesConfiguration;
        {
            String placed = "&eYou've &aplaced &ea &3&lMachine&e!";
            String broken = "&eYou've &abroke &ea &3&lMachine&e!";
            String reachedMaxMachines = "&eYou've &b&lreached &ethe &3&lMaximum &eamount of &3Machines&e!";
            String notOwner = "&eYou're &cnot &ethe &3&lowner &eof this &b&lmachine&e!";
            String openedMenu = "&eYou've &3opened &ethe &b&lMachine Menu&e!";
            String toggledOn = "&eYou've &b&ltoggled &eyour &3machine &3On&e.";
            String toggledOff = "&eYou've &b&ltoggled &eyour &3machine &cOff&e.";
            String noOwner = "&eThis &3machine &ehas &bno owner&e, so you can't change its &b&lstatus&e!";
            machineMessagesConfiguration = new MachineMessagesConfiguration(placed, broken, reachedMaxMachines, notOwner, openedMenu, toggledOn, toggledOff, noOwner);
        }

        FuelMessagesConfiguration fuelMessagesConfiguration;
        {
            String usedFuel = "&eYou've used &b&lfuel &eon your &3&lmachine&e!";
            String usedMultipleFuels = "&eYou've used &3%amount% &b&lfuel&e(&b&ls&e) &eon your &3&lmachine&e!";
            String machineHasInfiniteFuel = "&eThis &3&lmachine &edoesn't use &bfuel&e! Since it's fuel is &3&linfinite&e!";
            String reachedMaxCapacity = "&eThis &3&lmachine &ereached the &a&lmax &b&lFuel&e!";
            String incompatibleFuel = "&eThis &3&lfuel &eis &c&lincompatible &ewith this &b&lmachine&e!";

            fuelMessagesConfiguration = new FuelMessagesConfiguration(usedFuel, usedMultipleFuels, machineHasInfiniteFuel, reachedMaxCapacity, incompatibleFuel);
        }

        ShopMenuConfiguration shopMenuConfiguration;
        {
            MenuItemConfiguration machineChooseItem = new MenuItemConfiguration("DIAMOND_BLOCK", "&3&lMachines", new String[] { "&eClick to &aaccess &ethe &3&lmachines shop&e! "}, true, 11);
            MenuItemConfiguration fuelsChooseItem = new MenuItemConfiguration("DIAMOND", "&3&lFuels", new String[] { "&eClick to access the &3&lFuels shop&e!" }, false, 15);

            String[] economyLore = new String[] { "&3&m----------------", "&eClick to &3buy &efor &b%amount%&a$&e!" };
            String[] experienceLore = new String[] { "&3&m----------------", "&eClick to &3buy &efor &b%amount% &aLevels of EXP&e!" };
            String[] pointsLore = new String[] { "&3&m----------------", "&eClick to &3buy &efor &b%amount% &aPoints&e!" };


            String backgroundItemType = "CYAN_STAINED_GLASS_PANE";

            shopMenuConfiguration = new ShopMenuConfiguration(true, true, "&3&lShop", 3, 3, 3, machineChooseItem, fuelsChooseItem, economyLore, experienceLore, pointsLore, backgroundItemType);
        }

        DropsMenuConfiguration dropsMenuConfiguration = new DropsMenuConfiguration("&3&lDrops", 3, "BLUE_STAINED_GLASS_PANE", new String[] { "&eYou have %amount% &3&lDrops &eof this &btype&e!", "&3&m----------------", "&b&lLeft-Click &eto &3collect &e1 of this.", "&b&lRight-Click &eto &3collect &e64 of these.", "&b&lShift Right-Click to &3collect &b&lALL DROPS&e!" });

        GiveMenuConfiguration giveMenu;
        {
            int rows = 3;
            String title = "&3&lGet machines";
            String backgroundItem = "CYAN_STAINED_GLASS_PANE";
            giveMenu = new GiveMenuConfiguration(rows, title, backgroundItem);
        }

        DropsMessageConfiguration dropsMessageConfiguration;
        {
            String menuOpened = "&eYou've &aopened &ethe &3&lDrops &bMenu&e!";
            String notEnoughDropsToCollect = "&eYou don't have enough &3&lDrops &eof this &btype &eto &3&lcollect &ethis &b&lamount&e!!";
            String collected = "&eYou've collected %amount% Items!";

            dropsMessageConfiguration = new DropsMessageConfiguration(menuOpened, notEnoughDropsToCollect, collected);
        }

        ShopMessagesConfiguration shopMessagesConfiguration;
        {
            String purchaseSuccessful = "&eYour &apurchase &ewas &3&lsuccessful&e!";
            String notEnoughEconomy = "&eYou don't have enough &3&lEconomy &efor this&e!";
            String notEnoughExperience = "&eYou don't have enough &3&lExperience &efor this&e!";
            String notEnoughPoints = "&eYou don't have enough &3&lPoints &efor this&e!";
            String noInventorySlot = "&eYou don't have &3space &ein your &b&linventory &efor this &bpurchase&e!";

            shopMessagesConfiguration = new ShopMessagesConfiguration(purchaseSuccessful, notEnoughEconomy, notEnoughExperience, notEnoughPoints, noInventorySlot);
        }

        GlobalMessagesConfiguration globalMessages;
        {
            String notAPlayer = "&eYou must be a &3&lPlayer &eto use this command!";
            String notANumber = "&eYou must enter a &3&lNumber &eto use this command!";
            String playerNotFound = "&ePlayer &enot found!";
            String noPermission = "&eYou don't have &3&lpermission &eto use this &bcommand&e!";
            globalMessages = new GlobalMessagesConfiguration(notAPlayer, notANumber, playerNotFound, noPermission);
        }

        MessagesConfiguration messages = new MessagesConfiguration(helpCommand, giveCommand, actionsCommand, shopCommand, infoCommand, machineMenuConfiguration, shopMenuConfiguration, dropsMenuConfiguration, giveMenu, machineMessagesConfiguration, fuelMessagesConfiguration, shopMessagesConfiguration, dropsMessageConfiguration, globalMessages);

        return new GeneralConfiguration(ConfigVersions.getLatest().getConfigVersion(), true, true, true, false, true, messages);
    }

    public static Permissions defaultPermissionsConfiguration() {
        String machineBypassPermission = "Jadgens.machines.bypass";

        String giveCommandPermission = "JadGens.give.use";

        String actionsCommandPermission = "JadGens.actions.use";
        String actionsCommandManagerOthersPermission = "JadGens.actions.manager.others";

        String actionsCommandPurgePermission = "JadGens.actions.purse";
        String actionsCommandEnablePermission = "JadGens.actions.enable";
        String actionsCommandDisablePermission = "JadGens.actions.disable";

        String shopCommandPermission = "JadGens.shop.use";

        MachineLimits machineLimits;
        { //I don't think I was that original, but last group is 100% original, change my mind.
            LimitGroup vipGroup = new LimitGroup("&a&lVip", 50, "JadGens.limit.vip");
            LimitGroup mvpGroup = new LimitGroup("&b&lMvp", 150, "JadGens.limit.mvp");
            LimitGroup infinityGroup = new LimitGroup("&b&lInfinity", -1, "JadGens.limit.infinity");

            LimitGroup[] groups = new LimitGroup[] { vipGroup, mvpGroup, infinityGroup};
            machineLimits = new MachineLimits("&7Default", 20, groups);
        }

        return new Permissions(ConfigVersions.getLatest().getConfigVersion(), machineBypassPermission, giveCommandPermission, actionsCommandPermission, actionsCommandManagerOthersPermission, actionsCommandPurgePermission, actionsCommandEnablePermission, actionsCommandDisablePermission, shopCommandPermission, machineLimits);
    }
}
