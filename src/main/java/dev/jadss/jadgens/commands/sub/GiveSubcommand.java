package dev.jadss.jadgens.commands.sub;

import dev.jadss.jadapi.JadAPIPlugin;
import dev.jadss.jadapi.bukkitImpl.item.ItemNBT;
import dev.jadss.jadapi.bukkitImpl.item.JInventory;
import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.management.JQuickEvent;
import dev.jadss.jadgens.JadGens;
import dev.jadss.jadgens.api.MachinesAPI;
import dev.jadss.jadgens.api.config.generalConfig.messages.commands.GiveCommandMessagesConfiguration;
import dev.jadss.jadgens.api.config.interfaces.LoadedFuelConfiguration;
import dev.jadss.jadgens.api.config.interfaces.LoadedMachineConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GiveSubcommand {

    private static final MachinesAPI API = MachinesAPI.getInstance();

    public GiveSubcommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission(MachinesAPI.getInstance().getGeneralConfiguration().getPermissions().giveCommandPermission)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().globalMessages.noPermission));
            return;
        }

        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().giveCommand.giveMenuOpened));
                JInventory giveInventory = new JInventory(6, API.getGeneralConfiguration().getMessages().giveMenu.title);

                JMaterial material = JMaterial.getRegistryMaterials().find(API.getGeneralConfiguration().getMessages().giveMenu.backgroundItem);
                JItemStack item = new JItemStack(material == null ? JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.STONE) : material);

                giveInventory.fill(item);

                int next = 0;
                for (LoadedMachineConfiguration machineConfiguration : API.getMachineConfigurations()) {
                    JItemStack machineItem = machineConfiguration.getSuperMachineItem()
                            .setAmount(1)
                            .getNBT()
                            .setBoolean("Give_Machine", true)
                            .setString("Give_Machine_Configuration", machineConfiguration.getConfigurationName())
                            .getItem();
                    giveInventory.setItem(next, machineItem);
                    next++;
                }

                for (LoadedFuelConfiguration fuelConfiguration : API.getFuelConfigurations()) {
                    JItemStack machineItem = fuelConfiguration.getSuperItem()
                            .setAmount(1)
                            .getNBT()
                            .setBoolean("Give_Fuel", true)
                            .setString("Give_Fuel_Configuration", fuelConfiguration.getConfigurationName())
                            .getItem();
                    giveInventory.setItem(next, machineItem);
                    next++;
                }

                String id1 = JQuickEvent.generateID();
                String id2 = JQuickEvent.generateID();
                String id3 = JQuickEvent.generateID();

                new JQuickEvent<>(JadAPIPlugin.get(JadGens.class), InventoryClickEvent.class, EventPriority.MONITOR, event -> {
                    event.setCancelled(true);
                    if (event.getClickedInventory() != null && event.getCurrentItem() != null && event.getCurrentItem().getType() != JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.AIR).getMaterial(JMaterial.Type.ITEM).getKey()) {
                        JItemStack itemClick = new JItemStack(event.getCurrentItem());
                        ItemNBT<JItemStack> nbt = itemClick.getNBT();
                        if (nbt.getBoolean("Give_Machine")) {
                            LoadedMachineConfiguration machine = API.getMachineConfiguration(nbt.getString("Give_Machine_Configuration"));
                            if (event.getClick() == ClickType.LEFT) {
                                player.getInventory().addItem(machine.getSuperMachineItem().setAmount(1).getBukkitItem());
                            } else if (event.getClick() == ClickType.RIGHT) {
                                player.getInventory().addItem(machine.getSuperMachineItem().setAmount(64).getBukkitItem());
                            }
                        } else if (nbt.getBoolean("Give_Fuel")) {
                            LoadedFuelConfiguration fuel = API.getFuelConfiguration(nbt.getString("Give_Fuel_Configuration"));
                            if (event.getClick() == ClickType.LEFT) {
                                player.getInventory().addItem(fuel.getSuperItem().setAmount(1).getBukkitItem());
                            } else if (event.getClick() == ClickType.RIGHT) {
                                player.getInventory().addItem(fuel.getSuperItem().setAmount(64).getBukkitItem());
                            }
                        }
                    }
                }, -1, -1, e -> !e.isCancelled() && player.getUniqueId().equals(e.getWhoClicked().getUniqueId()), id1).register(true);

                new JQuickEvent<>(JadAPIPlugin.get(JadGens.class), InventoryCloseEvent.class, EventPriority.LOWEST, event -> {
                    JQuickEvent.getQuickEvent(id1).register(false);
                    JQuickEvent.getQuickEvent(id2).register(false);
                    JQuickEvent.getQuickEvent(id3).register(false);
                }, -1, -1, e -> player.getUniqueId().equals(e.getPlayer().getUniqueId()), id2).register(true);

                new JQuickEvent<>(JadAPIPlugin.get(JadGens.class), PlayerQuitEvent.class, EventPriority.LOWEST, e -> {
                    JQuickEvent.getQuickEvent(id1).register(false);
                    JQuickEvent.getQuickEvent(id2).register(false);
                    JQuickEvent.getQuickEvent(id3).register(false);
                }, -1, -1, e -> player.getUniqueId().equals(e.getPlayer().getUniqueId()), id3).register(true);

                player.openInventory(giveInventory.getBukkitInventory());
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', API.getGeneralConfiguration().getMessages().globalMessages.notAPlayer));
            }
        } else if (args.length == 3 || args.length == 4) {
            //Parse type.
            String type = args[0];

            boolean isMachine = false;
            boolean isFuel = false;

            GiveCommandMessagesConfiguration messages = API.getGeneralConfiguration().getMessages().giveCommand;

            for (String machineAlias : messages.machineAliases)
                if (type.equalsIgnoreCase(machineAlias)) {
                    isMachine = true;
                    break;
                }

            for (String fuelAlias : messages.fuelAliases)
                if (type.equalsIgnoreCase(fuelAlias)) {
                    isFuel = true;
                    break;
                }
            ;

            if (isFuel && isMachine)
                throw new RuntimeException("You can't have a machine alias and a fuel alias that are the same!");

            //End of parsing!

            //Parse amount!

            int amount;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (Exception ignored) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', API.getGeneralConfiguration().getMessages().globalMessages.notANumber));
                return;
            }

            //End of parsing amount!

            //Parse configuration ids and give results!

            String configurationId = args[1];

            if (isMachine) {
                if (args.length == 3) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        LoadedMachineConfiguration machineConfig = API.getMachineConfiguration(configurationId);
                        if (machineConfig == null) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.couldntFindId));
                            return;
                        }

                        player.getInventory().addItem(machineConfig.getSuperMachineItem().setAmount(amount).getBukkitItem());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.givenMachine
                                .replace("%amount%", "" + amount)));
                    } else {
                        sender.sendMessage("This command can only be used by a player.");
                    }
                } else {
                    Player player = Bukkit.getPlayer(args[3]);
                    if (player != null) {

                        LoadedMachineConfiguration machineConfig = API.getMachineConfiguration(configurationId);
                        if (machineConfig == null) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.couldntFindId));
                            return;
                        }

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.givenMachineTo
                                .replace("%amount%", "" + amount)
                                .replace("%to%", player.getName())));
                        player.getInventory().addItem(machineConfig.getSuperMachineItem().setAmount(amount).getBukkitItem());
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', API.getGeneralConfiguration().getMessages().globalMessages.playerNotFound));
                    }
                }
            } else if (isFuel) {
                if (args.length == 3) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        LoadedFuelConfiguration fuelConfig = API.getFuelConfiguration(configurationId);
                        if (fuelConfig == null) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.couldntFindId));
                            return;
                        }
                        player.getInventory().addItem(fuelConfig.getSuperItem().setAmount(amount).getBukkitItem());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.givenMachine.replace("%amount%", "" + amount)));
                    } else {
                        sender.sendMessage("This command can only be used by a player.");
                    }
                } else {
                    Player player = Bukkit.getPlayer(args[3]);
                    if (player != null) {

                        LoadedFuelConfiguration fuelConfig = API.getFuelConfiguration(configurationId);
                        if (fuelConfig == null) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.couldntFindId));
                            return;
                        }

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.givenFuelTo
                                .replace("%amount%", "" + amount)
                                .replace("%to%", player.getName())));

                        player.getInventory().addItem(fuelConfig.getSuperItem().setAmount(amount).getBukkitItem());
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', API.getGeneralConfiguration().getMessages().globalMessages.playerNotFound));
                    }
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', messages.couldNotFindAnyMatchingAlias)));
            }

        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', API.getGeneralConfiguration().getMessages().giveCommand.wrongSyntax));
        }
    }
}
