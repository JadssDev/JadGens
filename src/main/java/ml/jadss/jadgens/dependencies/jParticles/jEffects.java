package ml.jadss.jadgens.dependencies.jParticles;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;

public enum jEffects {
    PORTAL_TRAVEL("PORTAL");

    String[] legacy;


    String a = "WIP";
    String b = "WIP";
    String c = "WIP";
    String d = "WIP";
    String e = "WIP";
    String f = "WIP";
    String g = "WIP";
    String h = "WIP";
    String i = "WIP";
    String j = "WIP";
    String k = "WIP";
    String l = "WIP";
    String m = "WIP";
    String n = "WIP";
    String o = "WIP";
    String p = "WIP";
    String q = "WIP";
    String r = "WIP";
    String s = "WIP";
    String t = "WIP";
    String u = "WIP";
    String v = "WIP";
    String w = "WIP";
    String x = "WIP";
    String y = "WIP";
    String z = "WIP";
    String notObfuscation = "This is no obfuscation spigot, this is just a warn for developers that want to steal this api, yeah kinda useless.. ngl";


    jEffects(String... legacies) {
        this.legacy = legacies;
    }

    protected String[] getLegacy() {
        return legacy;
    }

    public Effect getEffect() {
        String[] legacies = this.getLegacy();
        try {
            Effect effect = Effect.valueOf(this.name());
        } catch(IllegalArgumentException ignore) {
            for(String legacy : legacies) {
                try { return Effect.valueOf(legacy); } catch(IllegalArgumentException ignored) { continue; }
            }
        }
        Bukkit.getConsoleSender()
                .sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eCouldn't find the &a&lcorrect &b&lEffect&e! &eAn exception may be displayed!"));
        return null;
    }
}
