package ml.jadss.jadgens.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.utils.MachineLimiter;
import ml.jadss.jadgens.utils.MachineLookup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlaceHolders extends PlaceholderExpansion {

    MachineLookup lookup = new MachineLookup();
    MachineLimiter limiter = new MachineLimiter();

    @Override
    public String getIdentifier() {
        return "jadgens";
    }

    @Override
    public String getAuthor() {
        return "Jadss";
    }

    @Override
    public String getVersion() {
        return JadGens.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        if (p == null) { return null; }

        String[] splitParams = params.split("_");
        if (splitParams.length != 2) return null;

        String ident = splitParams[0];
        String type = splitParams[1];
        int iType;

        if (ident.equalsIgnoreCase("me")) {
            if (type.equalsIgnoreCase("total")) {
                return String.valueOf(lookup.getPlayerMachineCount(p.getUniqueId()));
            } else if (type.equalsIgnoreCase("limit")) {
                if (limiter.getMaxLimit(p) != -1) {
                    return "Infinite";
                } else {
                    return String.valueOf(limiter.getMaxLimit(p));
                }
            } else {
                try {
                    iType = Integer.parseInt(type);
                    return String.valueOf(lookup.getPlayerMachineCount(p.getUniqueId(), iType));
                } catch (NumberFormatException ex) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens&e(&bPlaceholderAPI hook&e) &7>> &eA plugin supplied a invalid value!"));
                    return "Invalid";
                }
            }
        } else if (ident.equalsIgnoreCase("server")) {
            if (type.equalsIgnoreCase("total")) {
                return String.valueOf(lookup.getAllMachinesCount());
            } else {
                try {
                    iType = Integer.parseInt(type);
                    return String.valueOf(lookup.getAllMachinesCount(iType));
                } catch (NumberFormatException ex) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens&e(&bPlaceholderAPI hook&e) &7>> &eA plugin supplied a invalid value!"));
                    return "Invalid";
                }
            }
        } else {
            return "Invalid TOKEN.";
        }
    }
}
