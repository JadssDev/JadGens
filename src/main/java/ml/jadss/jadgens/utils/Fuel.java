package ml.jadss.jadgens.utils;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.dependencies.Compatibility;
import ml.jadss.jadgens.dependencies.nbt.NBTCompound;
import ml.jadss.jadgens.dependencies.nbt.NBTItem;
import ml.jadss.jadgens.dependencies.nbt.NbtApiException;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Fuel {

    private Integer drops;
    private Integer type;

    public Fuel() {
        this.drops = null;
        this.type = null;
    }

    public Fuel(ItemStack item) {
        if (!item.hasItemMeta()) { this.type = null; this.drops = null; return; }
        NBTCompound nbtCompound = new NBTItem(item);
        if (!JadGens.getInstance().getCompatibilityMode()) {
            if (nbtCompound.getBoolean("JadGens_fuel")) {
                this.type = nbtCompound.getInteger("JadGens_fuelType");
                this.drops = nbtCompound.getInteger("JadGens_drops");
                return;
            } else {
                this.drops = null;
                this.type = null;
                return;
            }
        } else {
            for (String key : JadGens.getInstance().getConfig().getConfigurationSection("fuels").getKeys(false)) {
                if (ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("fuels." + key + ".displayName")).equals(item.getItemMeta().getDisplayName())) {
                    this.type = Integer.parseInt(key);
                    this.drops = JadGens.getInstance().getConfig().getInt("fuels." + key + ".drops");
                    return;
                }
            }
            this.type = null;
            this.drops = null;
        }
    }

    public Integer getDrops() {
        return drops;
    }

    public Integer getType() {
        return type;
    }

    public boolean isFuel(ItemStack item) {
        if (item == null) return false;
        if (!item.hasItemMeta()) return false;
        if (!item.getItemMeta().hasDisplayName()) return false;
        if (!JadGens.getInstance().getCompatibilityMode()) {
            NBTCompound nbtCompound = new NBTItem(item);
            return nbtCompound.hasKey("JadGens_fuel");
        } else {
            for (String key : JadGens.getInstance().getConfig().getConfigurationSection("fuels").getKeys(false))
                if (ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("fuels." + key + ".displayName")).equals(item.getItemMeta().getDisplayName())) return true;
            return false;
        }
    }

    public ItemStack createItem(int id) {
        ItemStack fuel = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("fuels." + id + ".item.material")), 1, (short) JadGens.getInstance().getConfig().getInt("fuels." + id + ".item.damage"));
        ItemMeta meta = fuel.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("fuels." + id + ".displayName")));
        List<String> lore = new ArrayList<>();
        for (String s : JadGens.getInstance().getConfig().getStringList("fuels." + id + ".lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', s));
        }

        if (!JadGens.getInstance().getCompatibilityMode()) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            if (JadGens.getInstance().getConfig().getBoolean("fuels." + id + ".glow")) {
                meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
        }

        meta.setLore(lore);
        fuel.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(fuel);
        try {
            nbtItem.setBoolean("JadGens_fuel", true);
            nbtItem.setInteger("JadGens_fuelType", id);
            nbtItem.setInteger("JadGens_drops", JadGens.getInstance().getConfig().getInt("fuels." + id + ".drops"));
        } catch (NbtApiException e) {
            return null;
        }

        fuel = nbtItem.getItem();
        return fuel;
    }
}
