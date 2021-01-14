package ml.jadss.jadgens.listeners;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.dependencies.Compatibility;
import ml.jadss.jadgens.dependencies.nbt.NBTCompound;
import ml.jadss.jadgens.dependencies.nbt.NBTItem;
import ml.jadss.jadgens.utils.Fuel;
import ml.jadss.jadgens.utils.Machine;
import ml.jadss.jadgens.utils.Shop;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ShopListeners implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null &&
                e.getClickedInventory() != null &&
                new Compatibility().getTitle(e.getClickedInventory(), e.getView()).equals(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("shop.shopTitle")))) {
            e.setCancelled(true);

            NBTCompound nbt = new NBTItem(e.getCurrentItem());
            Player player = (Player) e.getWhoClicked();

            if (nbt.getBoolean("JadGens_MainShop")) {
                if (nbt.getString("JadGens_ShopType").equalsIgnoreCase("machines")) {
                    player.openInventory(new Shop().getShopInventory("machines"));
                } else if (nbt.getString("JadGens_ShopType").equalsIgnoreCase("fuels")) {
                    player.openInventory(new Shop().getShopInventory("fuels"));
                } else {
                    player.sendMessage(ChatColor.RED + "An internal error occurred, the nbt tag was not found!!");
                }
            }

            if (nbt.getString("JadGens_BuyType").equalsIgnoreCase("machines")) {

                int buyID = nbt.getInteger("JadGens_BuyID");
                String economy = JadGens.getInstance().getConfig().getString("machines." + buyID + ".shop.currency");
                int price = JadGens.getInstance().getConfig().getInt("machines." + buyID + ".shop.price");

                if (economy.equalsIgnoreCase("ECO")) {
                    if (JadGens.getInstance().getEco().getBalance(player) >= price) {
                        if (player.getInventory().firstEmpty() != -1) {
                            JadGens.getInstance().getEco().withdrawPlayer(player, price);
                            player.getInventory().addItem(new Machine().createItem(buyID));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("shop.purchaseSuccesfull")));
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.noInventorySpace")));
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("shop.noMoney")));
                    }
                } else if (economy.equalsIgnoreCase("POINTS")) {
                    if (JadGens.getInstance().getPointsAPI().look(player.getUniqueId()) >= price) {
                        if (player.getInventory().firstEmpty() != -1) {
                            JadGens.getInstance().getPointsAPI().take(player.getUniqueId(), price);
                            player.getInventory().addItem(new Machine().createItem(buyID));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("shop.purchaseSuccesfull")));
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.noInventorySpace")));
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("shop.noMoney")));
                    }
                } else if (economy.equalsIgnoreCase("EXP")) {
                    if (player.getLevel() >= price) {
                        if (player.getInventory().firstEmpty() != -1) {
                            player.setLevel(player.getLevel()-price);
                            player.getInventory().addItem(new Machine().createItem(buyID));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("shop.purchaseSuccesfull")));
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.noInventorySpace")));
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("shop.noMoney")));
                    }
                }
            }

            if (nbt.getString("JadGens_BuyType").equalsIgnoreCase("fuels")) {

                int buyID = nbt.getInteger("JadGens_BuyID");
                String economy = JadGens.getInstance().getConfig().getString("fuels." + buyID + ".shop.currency");
                int price = JadGens.getInstance().getConfig().getInt("fuels." + buyID + ".shop.price");

                if (economy.equalsIgnoreCase("ECO")) {
                    if (JadGens.getInstance().getEco().getBalance(player) >= price) {
                        if (player.getInventory().firstEmpty() != -1) {
                            JadGens.getInstance().getEco().withdrawPlayer(player, price);
                            player.getInventory().addItem(new Fuel().createItem(buyID));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("shop.purchaseSuccesfull")));
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.noInventorySpace")));
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("shop.noMoney")));
                    }
                } else if (economy.equalsIgnoreCase("POINTS")) {
                    if (JadGens.getInstance().getPointsAPI().look(player.getUniqueId()) >= price) {
                        if (player.getInventory().firstEmpty() != -1) {
                            JadGens.getInstance().getPointsAPI().take(player.getUniqueId(), price);
                            player.getInventory().addItem(new Fuel().createItem(buyID));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("shop.purchaseSuccesfull")));
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.noInventorySpace")));
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("shop.noMoney")));
                    }
                } else if (economy.equalsIgnoreCase("EXP")) {
                    if (player.getLevel() >= price) {
                        if (player.getInventory().firstEmpty() != -1) {
                            player.setLevel(player.getLevel()-price);
                            player.getInventory().addItem(new Fuel().createItem(buyID));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("shop.purchaseSuccesfull")));
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang().getString("messages.noInventorySpace")));
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("shop.noMoney")));
                    }
                }
            }
        }
    }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
