package ml.jadss.jadgens.management;

import ml.jadss.jadgens.JadGens;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UpdateChecker {

    private URL url;
    private boolean updated;
    private boolean checked;
    private int pluginID;
    private String currentVersion;
    private String updatedVersion;


    public UpdateChecker() {
        this.updated = false;
        this.checked = false;
        this.pluginID = 82805;
        this.currentVersion = JadGens.getInstance().getDescription().getVersion();
        try { url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.pluginID); } catch(MalformedURLException ignored) {}
    }

    public void check() {
        if (lang().getBoolean("messages.updateChecker.enabled")) {
            if (this.url == null) try { this.url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.pluginID); } catch (MalformedURLException ignored) { this.checked = false; return; }

            Bukkit.getScheduler().runTaskAsynchronously( JadGens.getInstance(), () -> {

                //read page.
                InputStream input;
                String readed = null;
                try {
                    input = url.openStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(input));
                    readed = br.readLine();
                } catch (IOException ex) {
                    return;
                }
                if (readed == null)  {checked = false; updated = false; return; }

                List<Integer> spigot = getNumbers(readed.toCharArray());
                List<Integer> own = getNumbers(JadGens.getInstance().getDescription().getVersion().toCharArray());

                int outdated = isUpToDate(spigot, own);

                if (outdated == 0) {
                    this.checked = true;
                    this.updated = true;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eRunning &b&llatest available &3version&e!"));
                        }
                    }.runTaskLaterAsynchronously(JadGens.getInstance(), 10 * 20);
                } else if (outdated == 1) {
                    this.checked = true;
                    this.updated = true;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eRunning &b&llatest &c(secret/development) &3version&e!"));
                        }
                    }.runTaskLaterAsynchronously(JadGens.getInstance(), 10 * 20);
                } else if (outdated == 2) {
                    this.checked = true;
                    this.updated = false;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eA &b&lnew Update &eis &3available&e!"));
                        }
                    }.runTaskLaterAsynchronously(JadGens.getInstance(), 10 * 20);
                }
            });
        }
    }

    private List<Integer> getNumbers(char[] line) {
        List<Integer> list = new ArrayList<>();
        boolean lastDefined = false;
        for (char c : line) {
            try {
                if (!lastDefined) {
                    list.add(Integer.parseInt(String.valueOf(c)));
                } else {
                    list.set(list.size()-1, Integer.parseInt(list.get(list.size() - 1) + String.valueOf(c)));
                }
                lastDefined = true;
            } catch(NumberFormatException ignored) { lastDefined = false; }
        }
        return list;
    }

    /**
     *
     * @param spigot spigot updated version
     * @param own our version
     * @return 0 = running latest;<p> 1 = running updated/development build;<p> 2 = running outdated;
     */
    @SuppressWarnings({"never gonna let you down! Never gonna run around and desert you!", "all"})
    private int isUpToDate(List<Integer> spigot, List<Integer> own) {
        int spigotLength = spigot.size()-1;
        int ownLength = own.size()-1;
        final int max = Math.max(spigotLength, ownLength);

        int outdated = 0;

        for (int i = 0; i < max+1; i++) {
            if (spigotLength < i) { outdated = 1;break; }
            if (ownLength < i) { outdated = 2;break; }

            if (spigot.get(i).intValue() == own.get(i).intValue()) continue;

            if (spigot.get(i) > own.get(i)) {
                outdated = 2; //outdated
                break;
            } else if (own.get(i) > spigot.get(i)) {
                outdated = 1; //very updated. =)
                break;
            }
        }

        return outdated;
    }

    public boolean isUpdated() { return updated; }
    public boolean hasChecked() { return checked; }
    public String getCurrentVersion() { return currentVersion; }
    public String getUpdatedVersion() { return updatedVersion; }
    public int getPluginID() { return pluginID; }

    protected FileConfiguration lang() { return JadGens.getInstance().getLangFile().lang(); }
}
