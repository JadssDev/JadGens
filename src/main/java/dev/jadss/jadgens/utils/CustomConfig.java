package dev.jadss.jadgens.utils;

import dev.jadss.jadgens.JadGens;
import dev.jadss.jadgens.api.config.interfaces.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.File;

public class CustomConfig<T> implements Configuration {

    private static final ObjectMapper JSON_PARSER = new ObjectMapper().configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
    public static final ObjectMapper PUBLIC_JSON_PARSER = new ObjectMapper().configure(SerializationConfig.Feature.INDENT_OUTPUT, true);

    private final File file;
    private final Class<T> clazz;

    public final boolean didExist;

    public CustomConfig(String fileName, JadGens plugin, Class<T> clazz) {
        this.file = new File(plugin.getDataFolder(), fileName);

        this.clazz = clazz;

        if (!file.exists()) {
            didExist = false;
            try {
                if (file.createNewFile())
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &b&lCreated &3" + fileName + "&e!"));
            } catch (Exception ex) {
                ex.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eAn error occurred while trying to &acreate &b" + file.getAbsolutePath() + "."));
                return;
            }
        } else {
            didExist = true;
        }
    }

    public File getFile() {
        return file;
    }

    public T get() {
        try {
            return JSON_PARSER.readValue(file, clazz);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public T save(T object) {
        try {
            JSON_PARSER.writeValue(file, object);
            return object;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
