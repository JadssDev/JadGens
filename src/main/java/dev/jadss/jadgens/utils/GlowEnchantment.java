package dev.jadss.jadgens.utils;

import dev.jadss.jadapi.JadAPIPlugin;
import dev.jadss.jadapi.bukkitImpl.enchantments.EnchantmentInstance;
import dev.jadss.jadapi.bukkitImpl.enchantments.JEnchantmentInfo;
import dev.jadss.jadgens.JadGens;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class GlowEnchantment implements JEnchantmentInfo {

    private EnchantmentInstance enchInstance;

    public GlowEnchantment() { }

    @Override
    public boolean isRegistered() {
        return enchInstance != null;
    }

    @Override
    public EnchantmentInstance getRegistered() {
        return enchInstance;
    }

    @Override
    public void setRegistered(EnchantmentInstance enchantmentInstance) {
        this.enchInstance = enchantmentInstance;
    }

    @Override
    public String getName() {
        return "Glow";
    }

    @Override
    public String getDisplayName() {
        return "&3&lGlow";
    }

    @Override
    public int getId() {
        return 1002;
    }

    @Override
    public JadAPIPlugin getEnchantmentOwner() {
        return JadAPIPlugin.get(JadGens.class);
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String buildLevelText(int i) {
        return null;
    }

    @Override
    public EnchantmentTarget getTarget() {
        return EnchantmentTarget.ALL;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return true;
    }
}
