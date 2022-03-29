package dev.jadss.jadgens.utils;

import dev.jadss.jadapi.bukkitImpl.misc.JWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utilities {

    public static String[] replace(String[] array, String toReplace, String replaceWith) {
        return Arrays.stream(array).map(s -> s.replace(toReplace, replaceWith)).toArray(String[]::new);
    }

    public static List<String> replace(List<String> list, String placeholder, String replacement) {
        List<String> replacedList = new ArrayList<>();
        for (String line : list)
            replacedList.add(line.replace(placeholder, replacement));

        return replacedList;
    }

    public static String fromLocation(Location location) {
        if(location.getWorld() == null) throw new RuntimeException("World cannot be null!");
        return location.getWorld().getName() + "_J_" + location.getBlockX() + "_J_" + location.getBlockY() + "_J_" + location.getBlockZ();
    }

    //careful, will cause exception if executed onEnable!
    public static Location fromId(String id) {
        if(id == null)
            throw new RuntimeException("Id cannot be null!");

        String[] lines;

        if (!id.contains("_J_")) {
            //Old format
            lines = id.split("_");
        } else {
            lines = id.split("_J_");
        }

        JWorld world = JWorld.getJWorlds().stream().filter(w -> w.getName().equalsIgnoreCase(lines[0])).findFirst().orElse(null);
        if (world == null)
            throw new RuntimeException("Could not find the world specified.");

        return new Location(world.getWorld(), Integer.parseInt(lines[1]), Integer.parseInt(lines[2]), Integer.parseInt(lines[3]));
    }
}
