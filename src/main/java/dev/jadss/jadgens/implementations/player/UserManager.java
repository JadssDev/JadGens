package dev.jadss.jadgens.implementations.player;

import dev.jadss.jadgens.api.MachinesAPI;
import dev.jadss.jadgens.api.config.generalConfig.limits.LimitGroup;
import dev.jadss.jadgens.api.config.generalConfig.limits.MachineLimits;
import dev.jadss.jadgens.api.machines.MachinesList;
import dev.jadss.jadgens.api.player.MachinesUser;
import dev.jadss.jadgens.api.config.interfaces.LoadedMachineConfiguration;
import dev.jadss.jadgens.api.config.playerConfig.MachineDropsPlayerInformation;
import dev.jadss.jadgens.api.config.playerConfig.PlayerInformation;
import dev.jadss.jadgens.api.machines.MachineInstance;
import dev.jadss.jadgens.api.player.UserMachineDrops;
import dev.jadss.jadgens.implementations.MachinesManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserManager implements MachinesUser {

    private final MachinesManager gensManager;
    private final UUID player;

    private final List<UserMachineDrops> drops;

    private int xpToCollect;

    public UserManager(PlayerInformation information, MachinesManager gensManager) {
        this.gensManager = gensManager;
        this.player = new UUID(information.mostSigUUID, information.leastSigUUID);

        drops = new ArrayList<>();
        for (MachineDropsPlayerInformation drop : information.drops)
            drops.add(new UserMachineDropsInstance(drop, gensManager));

        xpToCollect = information.xpToCollect;

        gensManager.getMachineConfigurations().forEach(this::getDropInformation);
    }

    @Override
    public UUID getPlayer() {
        return player;
    }

    @Override
    public MachinesList getMachines() {
        return MachinesList.newList(gensManager.getMachines().getMachinesInstances()
                .stream()
                .filter(machine -> machine.getMachine().getOwner().equals(player))
                .collect(Collectors.toCollection(ArrayList::new))
        );
    }

    @Override
    public boolean canPlaceMachines() {
        return getMaximumMachines() == -1 || getMachines().getMachinesInstances().size() < getMaximumMachines();
    }

    @Override
    public LimitGroup getLimitGroup() {
        MachineLimits limits = MachinesAPI.getInstance().getGeneralConfiguration().getPermissions().machineLimitsGroups;
        LimitGroup userLimit = null;
        int limit = limits.defaultLimit;
        if (limit == -1)
            return null;

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        if (!offlinePlayer.isOnline())
            return null;

        Player player = offlinePlayer.getPlayer();
        for (LimitGroup group : limits.groups) {
            if (!player.hasPermission(group.permission))
                continue;

            if (group.limit == -1)
                return group;

            if (group.limit > limit) {
                userLimit = group;
                limit = group.limit;
            }
        }

        return userLimit;
    }

    @Override
    public int getMaximumMachines() {
        if (getLimitGroup() != null)
            return getLimitGroup().limit;
        else
            return MachinesAPI.getInstance().getGeneralConfiguration().getPermissions().machineLimitsGroups.defaultLimit;
    }

    @Override
    public MachineInstance createMachine(LoadedMachineConfiguration configuration, Location location) {
        return gensManager.createMachine(configuration, player, location);
    }

    @Override
    public UserMachineDrops getDropInformation(LoadedMachineConfiguration configuration) {
        return drops.stream().filter(drop -> drop.getMachineConfiguration().equals(configuration)).findFirst().orElseGet(() -> {
            UserMachineDrops newDrop = new UserMachineDropsInstance(new MachineDropsPlayerInformation(configuration.getConfigurationName(), 0), gensManager);
            this.drops.add(newDrop);
            return newDrop;
        });
    }

    @Override
    public List<UserMachineDrops> getAllDropsInformation() {
        return new ArrayList<>(drops);
    }

    @Override
    public int getXpToCollect() {
        return xpToCollect;
    }

    @Override
    public int setXpToCollect(int amount) {
        return xpToCollect += amount;
    }

    @Override
    public PlayerInformation save() {
        return new PlayerInformation(player.getLeastSignificantBits(), player.getMostSignificantBits(), this.drops.stream().map(UserMachineDrops::save).toArray(MachineDropsPlayerInformation[]::new), xpToCollect);
    }
}
