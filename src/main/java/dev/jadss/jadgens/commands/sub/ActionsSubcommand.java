package dev.jadss.jadgens.commands.sub;

import dev.jadss.jadgens.api.MachinesAPI;
import dev.jadss.jadgens.api.actions.ActionResult;
import dev.jadss.jadgens.api.actions.MachineActionType;
import dev.jadss.jadgens.api.actions.impl.PurgedActionResult;
import dev.jadss.jadgens.api.config.generalConfig.messages.commands.ActionsCommandMessagesConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActionsSubcommand {

    private static final HashMap<UUID, Long> DELAY_MAP = new HashMap<>();

    public ActionsSubcommand(CommandSender sender, String[] args) {
        ActionsCommandMessagesConfiguration messages = MachinesAPI.getInstance().getGeneralConfiguration().getMessages().actionsCommand;

        if (!sender.hasPermission(MachinesAPI.getInstance().getGeneralConfiguration().getPermissions().actionsCommandPermission)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().globalMessages.noPermission));
            return;
        }

        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!DELAY_MAP.containsKey(player.getUniqueId()) || DELAY_MAP.get(player.getUniqueId()) - System.currentTimeMillis() < 0) {
                    DELAY_MAP.put(player.getUniqueId(), System.currentTimeMillis() + 5000);
                    String actionTypeInString = args[0];

                    boolean isEnable = false;
                    boolean isDisable = false;
                    boolean isPurge = false;

                    for (String actionType : messages.enableActionAliases)
                        if (actionType.equalsIgnoreCase(actionTypeInString))
                            isEnable = true;

                    for (String actionType : messages.disableActionAliases)
                        if (actionType.equalsIgnoreCase(actionTypeInString))
                            isDisable = true;

                    for (String actionType : messages.purgeActionAliases)
                        if (actionType.equalsIgnoreCase(actionTypeInString))
                            isPurge = true;
                    ;

                    //check if 2 or 3 of them are true at the same time.
                    if (isEnable && isDisable || isEnable && isPurge || isDisable && isPurge)
                        throw new RuntimeException("You can't have a enable|disable|purge alias with the same alias.");

                    if (isEnable) {
                        if (!sender.hasPermission(MachinesAPI.getInstance().getGeneralConfiguration().getPermissions().actionsCommandEnablePermission)) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().globalMessages.noPermission));
                            return;
                        }
                        ActionResult result = MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getMachines().executeAction(MachineActionType.ENABLE_MACHINES);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.machinesEnabled.replace("%amount%", "" + result.getMachines().size())));
                    } else if (isDisable) {
                        if (!sender.hasPermission(MachinesAPI.getInstance().getGeneralConfiguration().getPermissions().actionsCommandDisablePermission)) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().globalMessages.noPermission));
                            return;
                        }
                        ActionResult result = MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getMachines().executeAction(MachineActionType.DISABLE_MACHINES);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.machinesDisabled.replace("%amount%", "" + result.getMachines().size())));
                    } else if (isPurge) {
                        if (!sender.hasPermission(MachinesAPI.getInstance().getGeneralConfiguration().getPermissions().actionsCommandPurgePermission)) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().globalMessages.noPermission));
                            return;
                        }
                        ActionResult result = MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getMachines().executeAction(MachineActionType.PURGE_MACHINES);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.machinesPurged.replace("%amount%", "" + result.getMachines().size())));
                        Map<String, Integer> map = ((PurgedActionResult) result).getMachinesAmount();
                        for (String key : map.keySet()) {
                            int amount = map.get(key);
                            MachinesAPI.getInstance().getMachineConfiguration(key).getSuperMachineItem().setAmount(amount).drop(player.getLocation());
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.wrongSyntax));
                    }
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().globalMessages.notAPlayer));
            }
        } else if (args.length == 2) {

            if (!sender.hasPermission(MachinesAPI.getInstance().getGeneralConfiguration().getPermissions().actionsCommandManageOthersPermission)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().globalMessages.noPermission));
                return;
            }

            String actionTypeInString = args[0];

            boolean isEnable = false;
            boolean isDisable = false;
            boolean isPurge = false;

            for (String actionType : messages.enableActionAliases)
                if (actionType.equalsIgnoreCase(actionTypeInString))
                    isEnable = true;

            for (String actionType : messages.disableActionAliases)
                if (actionType.equalsIgnoreCase(actionTypeInString))
                    isDisable = true;

            for (String actionType : messages.purgeActionAliases)
                if (actionType.equalsIgnoreCase(actionTypeInString))
                    isPurge = true;
            ;

            //check if 2 or 3 of them are true at the same time.
            if (isEnable && isDisable || isEnable && isPurge || isDisable && isPurge)
                throw new RuntimeException("You can't have a enable|disable|purge alias with the same alias.");

            String playerName = args[1];
            Player player = Bukkit.getPlayerExact(playerName);

            if (player == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().globalMessages.playerNotFound));
                return;
            }

            if (isEnable) {
                if (!sender.hasPermission(MachinesAPI.getInstance().getGeneralConfiguration().getPermissions().actionsCommandEnablePermission)) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().globalMessages.noPermission));
                    return;
                }
                ActionResult result = MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getMachines().executeAction(MachineActionType.ENABLE_MACHINES);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.machinesEnabled.replace("%amount%", "" + result.getMachines().size())));
            } else if (isDisable) {
                if (!sender.hasPermission(MachinesAPI.getInstance().getGeneralConfiguration().getPermissions().actionsCommandDisablePermission)) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().globalMessages.noPermission));
                    return;
                }
                ActionResult result = MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getMachines().executeAction(MachineActionType.DISABLE_MACHINES);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.machinesDisabled.replace("%amount%", "" + result.getMachines().size())));
            } else if (isPurge) {
                if (!sender.hasPermission(MachinesAPI.getInstance().getGeneralConfiguration().getPermissions().actionsCommandPurgePermission)) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().globalMessages.noPermission));
                    return;
                }
                ActionResult result = MachinesAPI.getInstance().getPlayer(player.getUniqueId()).getMachines().executeAction(MachineActionType.PURGE_MACHINES);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.machinesPurged.replace("%amount%", "" + result.getMachines().size())));
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.wrongSyntax));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.wrongSyntax));
        }
    }
}
