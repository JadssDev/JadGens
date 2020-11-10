package ml.jadss.jadgens.utils;

import com.google.common.base.Enums;
import ml.jadss.jadgens.JadGens;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class ParticleSystem {

    public boolean spawnParticle(Location location) {
        if (JadGens.getInstance().getParticleType() == ParticleType.PARTICLE) {

            Class<Enum> particlesClass;
            try {
                particlesClass = (Class<Enum>) Class.forName("org.bukkit.Particle");
            } catch (ClassNotFoundException ignored) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eA &c&lSEVERE &4&lError &b&lOccurred &ewith the &3particle system&e! (Particles class not found)?"));
                return false;
            }

            Method spawnParticleMethod;
            try {
                spawnParticleMethod = location.getWorld().getClass().getMethod("spawnParticle", particlesClass, Location.class, int.class, double.class, double.class, double.class);
            } catch (NoSuchMethodException ignored) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eA &c&lSEVERE &4&lError &b&lOccurred &ewith the &3particle system&e! (Method not found)?"));
                return false;
            }


            Object particle = Enums.getIfPresent(particlesClass, JadGens.getInstance().getConfig().getString("machineParticles.particle.type")).orNull();
            if (particle == null) return false;

            try {
                spawnParticleMethod.invoke(location.getWorld(), particle, location, JadGens.getInstance().getConfig().getInt("machineParticles.particle.particleCount"), 0, 0, 0);
            } catch (InvocationTargetException | IllegalAccessException ignored) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eA &c&lSEVERE &4&lError &b&lOccurred &ewith the &3particle system&e! (Method, couldn't execute)?"));
                return false;
            }

            return true;
        } else {
            Effect effect = Enums.getIfPresent(Effect.class, JadGens.getInstance().getConfig().getString("machineParticles.particle.type")).orNull();
            if (effect == null) return false;

            location.getWorld().spigot().playEffect(location, effect,
                    0, 1, 0, 0, 0,
                    JadGens.getInstance().getConfig().getInt("machineParticles.particle.speed"),
                    JadGens.getInstance().getConfig().getInt("machineParticles.particle.particleCount"),
                    JadGens.getInstance().getConfig().getInt("machineParticles.particle.radius"));

            return true;
        }
    }

    private Object getParticle() {
        try {
            Class<Enum> particlesClass = (Class<Enum>) Class.forName("org.bukkit.Particle");
            JadGens.getInstance().setParticleType(ParticleType.PARTICLE);

            Object value = Enums.getIfPresent(particlesClass, JadGens.getInstance().getConfig().getString("machineParticles.particle.type")).orNull();
            if (value == null) return null; else return value;
        } catch (ClassNotFoundException ignored) {
            JadGens.getInstance().setParticleType(ParticleType.EFFECT);
            Object value = Enums.getIfPresent(Effect.class, JadGens.getInstance().getConfig().getString("machineParticles.particle.type")).orNull();
            if (value == null) return null; else return value;
        }
    }

    @SuppressWarnings("all")
    public boolean checkParticle() { return getParticle() != null;}
}
