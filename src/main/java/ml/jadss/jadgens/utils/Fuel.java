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
import java.util.Set;

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
                this.drops = JadGens.getInstance().getConfig().getInt("fuels." + this.type + ".drops");
            } else {
                this.drops = null;
                this.type = null;
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

    public Fuel(int type) {
        this.type = type;
        this.drops = JadGens.getInstance().getConfig().getInt("fuels." + this.type + ".drops");
    }

    public Integer getDrops() {
        return drops;
    }

    public Integer getType() {
        return type;
    }


    //
    //
    //
    //Standalone sh*t.
    public boolean isFuelItem(ItemStack item) {
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

    public List<Integer> getExistingTypes() {
        Set<String> typesAsString = JadGens.getInstance().getConfig().getConfigurationSection("fuels.").getKeys(false);
        List<Integer> types = new ArrayList<>();
        for (String type : typesAsString) { types.add(Integer.parseInt(type)); }

        return types;
    }

    public boolean typeExists(int type) {
        return this.getExistingTypes().contains(type);
    }

    public boolean typeExists(String type) {
        int typeInteger = -1;
        try {
            typeInteger = Integer.parseInt(type);
        } catch(NumberFormatException ignored) {
            return false;
        }

        return this.getExistingTypes().contains(typeInteger);
    }

    //Standalone sh*t.
    //
    //
    //

    public ItemStack createItem(int type) {
        ItemStack fuel = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("fuels." + type + ".item.material")), 1, (short) JadGens.getInstance().getConfig().getInt("fuels." + type + ".item.damage"));
        ItemMeta meta = fuel.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("fuels." + type + ".displayName")));
        List<String> lore = new ArrayList<>();
        for (String s : JadGens.getInstance().getConfig().getStringList("fuels." + type + ".lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', s));
        }

        if (!JadGens.getInstance().getCompatibilityMode()) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            if (JadGens.getInstance().getConfig().getBoolean("fuels." + type + ".glow")) {
                meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
        }

        meta.setLore(lore);
        fuel.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(fuel);
        try {
            nbtItem.setBoolean("JadGens_fuel", true);
            nbtItem.setInteger("JadGens_fuelType", type);
        } catch (NbtApiException e) {
            return null;
        }

        fuel = nbtItem.getItem();
        return fuel;
    }
}
