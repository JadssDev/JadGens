package ml.jadss.jadgens.utils;

import com.cryptomorin.xseries.XMaterial;
import ml.jadss.jadgens.JadGens;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class RecipeManager {

    Machine machine = new Machine();
    Fuel fuel = new Fuel();

    Class<?> nameSpacedKey;
    Constructor<?> nameSpacedKeyConstructor;
    Constructor<?> shapedRecipeConstructor;

    boolean updateMethods = false;

    public RecipeManager() {
        try {
            updateMethods = true;
            nameSpacedKey = Class.forName("org.bukkit.NamespacedKey");
            nameSpacedKeyConstructor = nameSpacedKey.getConstructor(Plugin.class, String.class);
            shapedRecipeConstructor = ShapedRecipe.class.getConstructor(nameSpacedKey, ItemStack.class);
        } catch(ClassNotFoundException | NoSuchMethodException ignored) {
            updateMethods = false;
            ignored.printStackTrace();
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens&e(&b&lDebug&e) &7>> &eUsing &3Updated methods &e-> &b&l" + updateMethods + "!"));
    }

    public void setupCrafts() {

        for(String type : JadGens.getInstance().getConfig().getConfigurationSection("machines").getKeys(false)) {

            if(JadGens.getInstance().getConfig().isSet("machines." + type + ".crafts.enabled") &&
                    JadGens.getInstance().getConfig().getBoolean("machines." + type + ".crafts.enabled")) {
                ItemStack result = machine.createItem(Integer.parseInt(type));

                if (updateMethods) {
                    Object nameSpaceObj;
                    try { nameSpaceObj = nameSpacedKeyConstructor.newInstance(JadGens.getInstance(), "JadGens_MachineCraft_" + type);
                    } catch(IllegalAccessException | InvocationTargetException | InstantiationException ex) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eAn error with the crafting, please report."));
                        ex.printStackTrace();
                        return;
                    }

                    Object recipe;
                    try {
                        recipe = shapedRecipeConstructor.newInstance(nameSpaceObj, result);
                    } catch(IllegalAccessException | InvocationTargetException | InstantiationException ex) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eAn error with the crafting, please report."));
                        ex.printStackTrace();
                        return;
                    }

                    ((ShapedRecipe) recipe).shape(JadGens.getInstance().getConfig().getString("machines." + type + ".crafts.row1"),
                            JadGens.getInstance().getConfig().getString("machines." + type + ".crafts.row2"),
                            JadGens.getInstance().getConfig().getString("machines." + type + ".crafts.row3"));

                    for(String ingredient : JadGens.getInstance().getConfig().getConfigurationSection("machines." + type + ".crafts.ingredients").getKeys(false)) {
                        char c = ingredient.charAt(0);
                        Material ingredientMat = XMaterial.valueOf(JadGens.getInstance().getConfig().getString("machines." + type + ".crafts.ingredients." + ingredient)).parseMaterial();
                        ((ShapedRecipe) recipe).setIngredient(c, ingredientMat);
                    }

                    Bukkit.addRecipe((ShapedRecipe) recipe);
                } else {
                    ShapedRecipe recipe = new ShapedRecipe(result);

                    recipe.shape(JadGens.getInstance().getConfig().getString("machines." + type + ".crafts.row1"),
                            JadGens.getInstance().getConfig().getString("machines." + type + ".crafts.row2"),
                            JadGens.getInstance().getConfig().getString("machines." + type + ".crafts.row3"));

                    for(String ingredient : JadGens.getInstance().getConfig().getConfigurationSection("machines." + type + ".crafts.ingredients").getKeys(false)) {
                        char c = ingredient.charAt(0);
                        Material ingredientMat = XMaterial.valueOf(JadGens.getInstance().getConfig().getString("machines." + type + ".crafts.ingredients." + ingredient)).parseMaterial();
                        recipe.setIngredient(c, ingredientMat);
                    }

                    Bukkit.addRecipe(recipe);
                }
            }
        }

        for(String type : JadGens.getInstance().getConfig().getConfigurationSection("fuels").getKeys(false)) {

            if(JadGens.getInstance().getConfig().isSet("fuels." + type + ".crafts.enabled") &&
                    JadGens.getInstance().getConfig().getBoolean("fuels." + type + ".crafts.enabled")) {
                ItemStack result = fuel.createItem(Integer.parseInt(type));

                if (updateMethods) {
                    Object nameSpaceObj;
                    try { nameSpaceObj = nameSpacedKeyConstructor.newInstance(JadGens.getInstance(), "JadGens_Machine_" + type);
                    } catch(IllegalAccessException | InvocationTargetException | InstantiationException ex) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eAn error with the crafting, please report."));
                        ex.printStackTrace();
                        return;
                    }

                    Object recipe;
                    try {
                        recipe = shapedRecipeConstructor.newInstance(nameSpaceObj, result);
                    } catch(IllegalAccessException | InvocationTargetException | InstantiationException ex) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eAn error with the crafting, please report."));
                        ex.printStackTrace();
                        return;
                    }

                    ((ShapedRecipe) recipe).shape(JadGens.getInstance().getConfig().getString("fuels." + type + ".crafts.row1"),
                            JadGens.getInstance().getConfig().getString("fuels." + type + ".crafts.row2"),
                            JadGens.getInstance().getConfig().getString("fuels." + type + ".crafts.row3"));

                    for(String ingredient : JadGens.getInstance().getConfig().getConfigurationSection("fuels." + type + ".crafts.ingredients").getKeys(false)) {
                        char c = ingredient.charAt(0);
                        Material ingredientMat = XMaterial.valueOf(JadGens.getInstance().getConfig().getString("fuels." + type + ".crafts.ingredients." + ingredient)).parseMaterial();
                        ((ShapedRecipe) recipe).setIngredient(c, ingredientMat);
                    }

                    Bukkit.addRecipe((ShapedRecipe) recipe);
                } else {
                    ShapedRecipe recipe = new ShapedRecipe(result);

                    recipe.shape(JadGens.getInstance().getConfig().getString("fuels." + type + ".crafts.row1"),
                            JadGens.getInstance().getConfig().getString("fuels." + type + ".crafts.row2"),
                            JadGens.getInstance().getConfig().getString("fuels." + type + ".crafts.row3"));

                    for(String ingredient : JadGens.getInstance().getConfig().getConfigurationSection("fuels." + type + ".crafts.ingredients").getKeys(false)) {
                        char c = ingredient.charAt(0);
                        Material ingredientMat = XMaterial.valueOf(JadGens.getInstance().getConfig().getString("fuels." + type + ".crafts.ingredients." + ingredient)).parseMaterial();
                        recipe.setIngredient(c, ingredientMat);
                    }

                    Bukkit.addRecipe(recipe);
                }
            }
        }
    }
}
