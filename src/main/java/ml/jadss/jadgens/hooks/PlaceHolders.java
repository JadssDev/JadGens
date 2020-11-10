package ml.jadss.jadgens.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.utils.MachineLookup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceHolders extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "jadgens";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Jadss";
    }

    @Override
    public @NotNull String getVersion() {
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

        if (ident.equalsIgnoreCase("machines")) {
            if (type.equalsIgnoreCase("total")) {
                return String.valueOf(new MachineLookup().getMachines(p.getUniqueId()));
            } else {
                try {
                    iType = Integer.parseInt(type);
                    return String.valueOf(new MachineLookup().getMachines(p.getUniqueId(), iType));
                } catch (NumberFormatException ex) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens&e(&bPAPI&e) &7>> &eA plugin supplied a invalid value!"));
                    return "Invalid";
                }
            }
        }

        return null;
    }
}
