package dev.jadss.jadgens.hooks;

import dev.jadss.jadgens.api.MachinesAPI;
import dev.jadss.jadgens.api.config.interfaces.LoadedMachineConfiguration;
import dev.jadss.jadgens.api.player.MachinesUser;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * PlaceholderAPI hook.
 */
public class PlaceholderAPIHook implements Hook {

    private static String version;

    private boolean triedHooking;
    private PlaceholderExpansion expansion;
    private boolean isAvailable;

    @Override
    public String getName() {
        return "placeholderapi";
    }

    @Override
    public String getDisplayName() {
        return "PlaceholderAPI";
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public PlaceholderExpansion getHook() {
        return expansion;
    }

    @Override
    public boolean hook(Server server) {
        if (triedHooking) return false;
        triedHooking = true;

        version = server.getPluginManager().getPlugin("JadGens").getDescription().getVersion();

        if(server.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            expansion = new Expansion();
            expansion.register();
            isAvailable = true;
        }

        return true;
    }

    @Override
    public boolean unhook(Server server) {
        try {
            expansion.unregister();
            this.triedHooking = false;
            this.isAvailable = false;
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static class Expansion extends PlaceholderExpansion {

        private static final String INVALID_PLACEHOLDER = "Invalid action! Please check your manual!";
        private static final String NOT_ENOUGH_ARGUMENTS = "Not enough arguments! Please check your manual!";
        private static final String COULD_NOT_FIND_MACHINE = "Could not find machine you were looking for! Please check your manual!";

        @Override
        public @NotNull String getIdentifier() {
            return "jadgens";
        }

        @Override
        public @NotNull String getAuthor() {
            return "jadss";
        }

        @Override
        public @NotNull String getVersion() {
            return version;
        }

        @Override
        public String onPlaceholderRequest(final Player player, @NotNull final String arguments) {
            String[] params = arguments.split("_");

            if (params.length == 0) {
                return INVALID_PLACEHOLDER;
            }

            String targetType = params[0];
            if (targetType.equalsIgnoreCase("me")) {
                if (params.length > 1) {
                    String action = params[1];
                    if (action.equalsIgnoreCase("total")) {
                        return "" + MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getMachines().getMachinesInstances().size();
                    } else if (action.equalsIgnoreCase("limit")) {
                        return "" + MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getLimitGroup().limit;
                    } else if (action.equalsIgnoreCase("limitgroup")) {
                        return MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getLimitGroup().limitGroupName;
                    } else if (action.equalsIgnoreCase("remaining")) {
                        int limit = MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getLimitGroup().limit;
                        if (limit == -1)
                            return MachinesAPI.getInstance().getGeneralConfiguration().getMessages().infoCommand.infinite;
                        else
                            return "" + (limit - MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getMachines().getMachinesInstances().size());
                    } else if (action.equalsIgnoreCase("type")) {
                        String machineType = Arrays.stream(params).skip(2L).collect(Collectors.joining("_"));
                        LoadedMachineConfiguration machineConfiguration = MachinesAPI.getInstance().getMachineConfiguration(machineType);
                        if (machineConfiguration == null)
                            return COULD_NOT_FIND_MACHINE;

                        return "" + MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getMachines().getMachines().stream()
                                .filter(machine -> machine.getMachineConfiguration().equals(machineConfiguration))
                                .count();
                    }
                } else {
                    return NOT_ENOUGH_ARGUMENTS;
                }
            } else if (targetType.equalsIgnoreCase("server")) {
                if (params.length > 1) {
                    String action = params[1];
                    if (action.equalsIgnoreCase("total")) {
                        return "" + (long) MachinesAPI.getInstance().getMachines().getMachines().size();
                    } else if (action.equalsIgnoreCase("type")) {
                        String machineType = Arrays.stream(params).skip(2L).collect(Collectors.joining("_"));
                        LoadedMachineConfiguration machineConfiguration = MachinesAPI.getInstance().getMachineConfiguration(machineType);
                        if (machineConfiguration == null)
                            return COULD_NOT_FIND_MACHINE;

                        return "" + MachinesAPI.getInstance().getMachines().getMachines().stream()
                                .filter(machine -> machine.getMachineConfiguration().equals(machineConfiguration))
                                .count();
                    }
                } else {
                    return NOT_ENOUGH_ARGUMENTS;
                }
            } else {
                //Todo: implement a player target placeholder, for now just return Invalid placeholder.
                return INVALID_PLACEHOLDER;
            }
            return "N/A";
        }
    }
}
