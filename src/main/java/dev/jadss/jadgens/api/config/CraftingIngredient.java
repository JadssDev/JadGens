package dev.jadss.jadgens.api.config;

import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CraftingIngredient implements Configuration {

    public final char key;
    public final String materialType;

    public CraftingIngredient() {
        this(' ', null);
    }
}
