package ml.jadss.jadgens.management;

import ml.jadss.jadgens.JadGens;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LangFile {

    private File langFile;
    private FileConfiguration lang;
    public FileConfiguration lang() {
        return lang;
    }

    public void setupLangFile() {
        langFile = new File(JadGens.getInstance().getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            try {
                langFile.createNewFile();
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &3Lang&e.&ayml &aCreated&e! If it &cwasn't &acreated &eplease be sure to &3&krestart &ethe &b&lserver&e!"));
            }
        }
        lang = YamlConfiguration.loadConfiguration(langFile);

        createFields();

        saveLang();

        reloadLang();
    }

    public void saveLang() {
        try {
            lang.save(langFile);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &c&lCannot &esave &3Lang&e.&ayml&e!"));
        }
    }

    public void reloadLang() {
        lang = YamlConfiguration.loadConfiguration(langFile);
    }

    protected void createFields() {
        //help messages
        List<String> list = new ArrayList<>();
        list.add("&3&lJadGens &eby &bJadss_pt&e!");
        list.add("&3&lCommands&e:");
        list.add("&3");
        list.add("&3/JadGens &bHelp &7: &eDisplay &c&lTHIS &ehelp message!");
        list.add("&3/JadGens &bGive &b<Type> &a<ID> &a<Player> &7: &eGives the player a machine or fuel!");
        list.add("&3/JadGens &bPurge &7: &ePurges &c&lALL &3Machines&e.");
        list.add("&3/JadGens &bReload &7: &eReload the &3plugin &c(not recommended, just restart the server)");
        list.add("&3/JadGens &bShop &b<Type> &7: &eOpen the &3&lShop&e!");
        list.add("&3/JadGens &bInfo &7: &eShows &b&linfo &eabout your &3&lMachines&e!");
        list.add("&3/JadGens &bVersion &7: &eSee the &bversion &eof the &3&lplugin&e!");
        list.add("&e");
        lang().addDefault("messages.helpMessages", list);

        //give Messages
        lang().addDefault("messages.giveMessages.permission", "JadGens.give");
        lang().addDefault("messages.giveMessages.usage", "&cBad Usage! &eUse: &3&3/JadGens &bgive &b&l<fuel/machine> &a<ID> &a<Player>");
        lang().addDefault("messages.giveMessages.givenMachine", "&eYou have been &bgiven &ea &3&lmachine&e!");
        lang().addDefault("messages.giveMessages.givenFuel", "&eYou have been &bgiven &ea &3&lFuel&e!");
        lang().addDefault("messages.giveMessages.idNotFound", "&eThe &3&lID &aspecified &cdoesn't &bexist&e!");
        lang().addDefault("messages.giveMessages.typeNotFound", "&eTHe &3&lType &aspecified &cdoesn't &bexist&e!");

        //purge Messages
        lang().addDefault("messages.purgeMessages.purgingMachines", "&ePurging &3&lmachines&e...");

        //reload Messages
        lang().addDefault("messages.reloadMessages.permission", "JadGens.reload");
        lang().addDefault("messages.reloadMessages.pluginReloaded", "&eThe &3&lPlugin &ehas been &b&lReloaded&e! &e(You should &3restart &eso every change applies)");

        //info messages
        lang().addDefault("messages.infoMessages.infinite", "&b&lInfinite");
        List<String> list2 = new ArrayList<>();
        list2.add("&3");
        list2.add("&3JadGens &7>> &eYou have &3%has% &b&lMachines&e, your &3&lmax &eis &b%max%&e!");
        list2.add("&3");
        lang().addDefault("messages.infoMessages.msg", list2);

        //machines messages
        lang().addDefault("messages.machinesMessages.placed", "&eYou've &aplaced &ea &3&lMachine&e!");
        lang().addDefault("messages.machinesMessages.broken", "&eYou've &abroke &ea &3&lMachine&e!");
        lang().addDefault("messages.machinesMessages.limitReached", "&eYou &c&lcannot &bplace &amore &3&lMachines!");
        lang().addDefault("messages.machinesMessages.notTheOwner", "&eYou're &cnot &ethe &3&lowner &eof this &b&lmachine&e!");

        //fuel Messages
        lang().addDefault("messages.fuelMessages.used", "&eYou've used &b&lfuel &eon your &3&lmachine&e!");
        lang().addDefault("messages.fuelMessages.machineNotAcceptingFuel", "&eThis &3&lmachine &edoesn't use &bfuel&e!");
        lang().addDefault("messages.fuelMessages.notAMachine", "&eThis is &c&lnot &ea &3&lmachine&e!");
        lang().addDefault("messages.fuelMessages.doesntAcceptMoreFuel", "&eThis &3&lmachine &ereached the &a&lmax &b&lFuel&e!");
        lang().addDefault("messages.fuelMessages.notRightFuel", "&eThis &3&lfuel &cdoesn't &bwork &eon this &b&lmachine&e!");

        //Global messages
        lang().addDefault("messages.noPermission", "&eNo &3&lPermission&e!");
        lang().addDefault("messages.playerNotFound", "&eThe &3player &aspecified &ewas &c&lnot &efound!");
        lang().addDefault("messages.notPLayer", "&eYou're not a player! You cannot use this command!");
        lang().addDefault("messages.onlyConsole", "&eYou &ccannot &aexecute &ethis! Only the &b&lconsole&e!");
        lang().addDefault("messages.noInventorySpace", "&eNo &3&linventory &aspace &eto &a&lplace &ethe &b&lMachine&e!");
        lang().addDefault("messages.noPistonMoving", "&eYou &c&lcannot &euse &3pistons &eto &a&lmove &b&lMachines&e!");
        lang().addDefault("messages.bypassPermission", "JadGens.bypassBreak");

        //Update checker
        lang().addDefault("messages.updateChecker.enabled", true);
        lang().addDefault("messages.updateChecker.notifyOnJoin", true);
        lang().addDefault("messages.updateChecker.permission", "JadGens.UpdateChecker.Notify");
        lang().addDefault("messages.updateChecker.message", "&3JadGens &7>> &eA &bnew &3&lUpdate &eis &bAvailable&e!");

        //Debug booleans
        lang().addDefault("messages.debugAPI", false);

        lang().options().copyDefaults(true);
    }
}
