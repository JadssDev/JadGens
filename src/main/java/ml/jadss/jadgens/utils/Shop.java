package ml.jadss.jadgens.utils;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.dependencies.Compatibility;
import ml.jadss.jadgens.dependencies.nbt.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Shop {

    public Shop() { return; }

    public Inventory getShopInventory(String shopInv) {
        if (shopInv.equalsIgnoreCase("machines")) {
            return createMachinesShop();
        } else if (shopInv.equalsIgnoreCase("fuels")) {
            return createFuelsShop();
        } else if (shopInv.equalsIgnoreCase("mainshop"))
            return createMainShop();
        return null;
    }

    // shop.shopMainMenu.machinesItem .fuelsItem

    private Inventory createMainShop() {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("shop.shopTitle")));

        //fill the gui with stained glass.
        ItemStack itemBack = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("shop.backgroundItem.material")), 1, (short) JadGens.getInstance().getConfig().getInt("shop.backgroundItem.damage"));
        for(int i = 0;i < 27; i++) {
            inv.setItem(i, itemBack);
        }

        ItemStack machinesItem = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("shop.shopMainMenu.machinesItem.material")), 1, (short) JadGens.getInstance().getConfig().getInt("shop.shopMainMenu.machinesItem.damage"));
        ItemStack fuelsItem = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("shop.shopMainMenu.fuelsItem.material")), 1, (short) JadGens.getInstance().getConfig().getInt("shop.shopMainMenu.fuelsItem.damage"));
        ItemMeta machinesMeta = machinesItem.getItemMeta();
        ItemMeta fuelsMeta = fuelsItem.getItemMeta();

        machinesMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("shop.shopMainMenu.machinesItem.name")));
        fuelsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("shop.shopMainMenu.fuelsItem.name")));

        //defining Lists.
        List<String> machinesLore = new ArrayList<>();
        List<String> fuelsLore = new ArrayList<>();

        //Setting lores.
        for (String loreString : JadGens.getInstance().getConfig().getStringList("shop.shopMainMenu.machinesItem.lore")) {
            machinesLore.add(ChatColor.translateAlternateColorCodes('&', loreString));
        }
        for (String loreString : JadGens.getInstance().getConfig().getStringList("shop.shopMainMenu.fuelsItem.lore")) {
            fuelsLore.add(ChatColor.translateAlternateColorCodes('&', loreString));
        }
        machinesMeta.setLore(machinesLore);
        fuelsMeta.setLore(fuelsLore);

        machinesItem.setItemMeta(machinesMeta);
        fuelsItem.setItemMeta(fuelsMeta);

        NBTItem machinesNBT = new NBTItem(machinesItem);
        NBTItem fuelsNBT = new NBTItem(fuelsItem);

        machinesNBT.setBoolean("JadGens_MainShop", true);
        fuelsNBT.setBoolean("JadGens_MainShop", true);
        machinesNBT.setString("JadGens_ShopType", "machines");
        fuelsNBT.setString("JadGens_ShopType", "fuels");

        machinesItem = machinesNBT.getItem();
        fuelsItem = fuelsNBT.getItem();

        inv.setItem(JadGens.getInstance().getConfig().getInt("shop.shopMainMenu.machinesItem.slot")-1, machinesItem);
        inv.setItem(JadGens.getInstance().getConfig().getInt("shop.shopMainMenu.fuelsItem.slot")-1, fuelsItem);

        return inv;
    }



    private Inventory createMachinesShop() {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("shop.shopTitle")));

        //fill the gui with stained glass.
        ItemStack itemBack = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("shop.backgroundItem.material")), 1, (short) JadGens.getInstance().getConfig().getInt("shop.backgroundItem.damage"));
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, itemBack);
        }

        Set<String> keys = JadGens.getInstance().getConfig().getConfigurationSection("machines").getKeys(false);
        for (String key : keys) {
            if (!JadGens.getInstance().getConfig().getBoolean("machines." + key + ".shop.displayOnShop")) continue;
            ItemStack item = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("machines." + key + ".MachineBlock.material")), 1, (short) JadGens.getInstance().getConfig().getInt("machines." + key + ".MachineBlock.damage"));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machines." + key + ".displayName")));

            List<String> lore = new ArrayList<>();
            if (JadGens.getInstance().getConfig().getString("machines." + key + ".shop.currency").equalsIgnoreCase("ECO")) {
                for (String s : JadGens.getInstance().getConfig().getStringList("shop.ecoCurrency.lore")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', s).replace("%price%", String.valueOf(JadGens.getInstance().getConfig().getInt("machines." + key + ".shop.price"))));
                }
            } else if (JadGens.getInstance().getConfig().getString("machines." + key + ".shop.currency").equalsIgnoreCase("POINTS")) {
                for (String s : JadGens.getInstance().getConfig().getStringList("shop.pointsCurrency.lore")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', s).replace("%price%", String.valueOf(JadGens.getInstance().getConfig().getInt("machines." + key + ".shop.price"))));
                }
            } else if (JadGens.getInstance().getConfig().getString("machines." + key + ".shop.currency").equalsIgnoreCase("EXP")) {
                for (String s : JadGens.getInstance().getConfig().getStringList("shop.expCurrency.lore")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', s).replace("%price%", String.valueOf(JadGens.getInstance().getConfig().getInt("machines." + key + ".shop.price"))));
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eAn &cinvalid &bcurrency &ewas &3&lspecified&e!"));
                return null;
            }

            meta.setLore(lore);
            item.setItemMeta(meta);

            NBTItem nbtItem = new NBTItem(item);
            nbtItem.setBoolean("JadGens_BuyableItem", true);
            nbtItem.setString("JadGens_BuyType", "machines");
            nbtItem.setInteger("JadGens_BuyID", Integer.parseInt(key));
            item = nbtItem.getItem();

            inv.setItem(JadGens.getInstance().getConfig().getInt("machines." + key + ".shop.slot")-1, item);
        }

        return inv;
    }

    private Inventory createFuelsShop() {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("shop.shopTitle")));

        //fill the gui with stained glass.
        ItemStack itemBack = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("shop.backgroundItem.material")), 1, (short) JadGens.getInstance().getConfig().getInt("shop.backgroundItem.damage"));
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, itemBack);
        }

        Set<String> keys = JadGens.getInstance().getConfig().getConfigurationSection("fuels").getKeys(false);
        for (String key : keys) {
            if (!JadGens.getInstance().getConfig().getBoolean("fuels." + key + ".shop.displayOnShop")) continue;
            ItemStack item = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("fuels." + key + ".item.material")), 1, (short) JadGens.getInstance().getConfig().getInt("machines." + key + ".MachineBlock.damage"));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("fuels." + key + ".displayName")));

            List<String> lore = new ArrayList<>();
            if (JadGens.getInstance().getConfig().getString("fuels." + key + ".shop.currency").equalsIgnoreCase("ECO")) {
                for (String s : JadGens.getInstance().getConfig().getStringList("shop.ecoCurrency.lore")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', s).replace("%price%", String.valueOf(JadGens.getInstance().getConfig().getInt("fuels." + key + ".shop.price"))));
                }
            } else if (JadGens.getInstance().getConfig().getString("fuels." + key + ".shop.currency").equalsIgnoreCase("EXP")) {
                for (String s : JadGens.getInstance().getConfig().getStringList("shop.expCurrency.lore")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', s).replace("%price%", String.valueOf(JadGens.getInstance().getConfig().getInt("fuels." + key + ".shop.price"))));
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eAn &cinvalid &bcurrency &ewas &3&lspecified&e!"));
                return null;
            }

            meta.setLore(lore);
            item.setItemMeta(meta);

            NBTItem nbtItem = new NBTItem(item);
            nbtItem.setBoolean("JadGens_BuyableItem", true);
            nbtItem.setString("JadGens_BuyType", "fuels");
            nbtItem.setInteger("JadGens_BuyID", Integer.parseInt(key));
            item = nbtItem.getItem();

            inv.setItem(JadGens.getInstance().getConfig().getInt("fuels." + key + ".shop.slot")-1, item);
        }
        return inv;
    }
}