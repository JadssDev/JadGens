package dev.jadss.jadgens;

import dev.jadss.jadapi.JadAPIPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class JGens extends JadAPIPlugin {

    @Override
    public JavaPlugin getJavaPlugin() {
        return JadGens.getInstance();
    }
}
