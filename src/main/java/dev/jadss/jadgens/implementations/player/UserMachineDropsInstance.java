package dev.jadss.jadgens.implementations.player;

import dev.jadss.jadgens.api.config.interfaces.LoadedMachineConfiguration;
import dev.jadss.jadgens.api.config.playerConfig.MachineDropsPlayerInformation;
import dev.jadss.jadgens.api.player.UserMachineDrops;
import dev.jadss.jadgens.implementations.MachinesManager;

public class UserMachineDropsInstance implements UserMachineDrops {

    private final LoadedMachineConfiguration machineConfiguration;
    private long amount;

    public UserMachineDropsInstance(MachineDropsPlayerInformation info, MachinesManager gensManager) {
        this.machineConfiguration = gensManager.getMachineConfiguration(info.machineID);
        this.amount = info.amount;
    }

    @Override
    public boolean isValid() {
        return amount > 0 && machineConfiguration != null;
    }

    @Override
    public String getMachineConfigurationName() {
        return machineConfiguration.getConfigurationName();
    }

    @Override
    public LoadedMachineConfiguration getMachineConfiguration() {
        return machineConfiguration;
    }

    @Override
    public boolean hasAtLeast(long amount) {
        return this.amount >= amount;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public long addAmount(long amount) {
        return this.amount += amount;
    }

    @Override
    public long removeAmount(long amount) {
        return this.amount -= amount;
    }

    @Override
    public MachineDropsPlayerInformation save() {
        return new MachineDropsPlayerInformation(machineConfiguration.getConfigurationName(), amount);
    }
}
