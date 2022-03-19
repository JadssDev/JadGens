package dev.jadss.jadgens.api.config;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RecipeConfiguration implements Configuration {

    public final boolean enabled;

    public final String row1;
    public final String row2;
    public final String row3;

    public final CraftingIngredient[] ingredients;

    public RecipeConfiguration() {
        this(false, null, null, null, null);
    }
}
