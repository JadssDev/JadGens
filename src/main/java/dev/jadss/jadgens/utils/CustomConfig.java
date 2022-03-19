package dev.jadss.jadgens.utils;

import dev.jadss.jadgens.JadGens;
import dev.jadss.jadgens.api.config.interfaces.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.File;

public class CustomConfig<T> implements Configuration {

    private final File file;
    private final ObjectMapper mapper;
    private final Class<T> clazz;

    //Defines if the file was created during this instance, OR NOT.
    private final boolean isNew;

    public CustomConfig(String fileName, JadGens plugin, Class<T> clazz) {
        this.file = new File(plugin.getDataFolder(), fileName);

        this.clazz = clazz;

        this.mapper = new ObjectMapper();
        this.mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);

        if (!file.exists()) {
            isNew = false;
            try {
                if (file.createNewFile())
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Created Configuration file.");
            } catch (Exception ex) {
                ex.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eAn error occurred while trying to &acreate &b" + file.getAbsolutePath() + "."));
                return;
            }
        } else {
            isNew = true;
        }
    }

    public T get() {
        try {
            return mapper.readValue(file, clazz);
        } catch (Exception ex) {
            if (isNew)
                ex.printStackTrace();
            return null;
        }
    }

    public T save(T object) {
        try {
            mapper.writeValue(file, object);
            return object;
        } catch (Exception ex) {
            if (isNew)
                ex.printStackTrace();
            return null;
        }
    }
}
