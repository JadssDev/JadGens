package dev.jadss.jadgens.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import dev.jadss.jadgens.JadGens;
import dev.jadss.jadgens.api.config.interfaces.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;

public class CustomConfig<T> implements Configuration {

    private static final ObjectMapper JSON_PARSER = JsonMapper.builder()
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)
            .configure(MapperFeature.AUTO_DETECT_GETTERS, false)
            .configure(MapperFeature.AUTO_DETECT_FIELDS, true)
            .configure(MapperFeature.AUTO_DETECT_SETTERS, false)
            .build();

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
