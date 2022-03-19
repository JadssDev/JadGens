package dev.jadss.jadgens.implementations.machines;

import dev.jadss.jadgens.api.config.interfaces.LoadedMachineConfiguration;
import dev.jadss.jadgens.api.config.serializers.MachineInformation;
import dev.jadss.jadgens.api.machines.Machine;
import dev.jadss.jadgens.api.machines.MachineInstance;
import dev.jadss.jadgens.implementations.MachinesManager;
import dev.jadss.jadgens.utils.Utilities;
import org.bukkit.Location;

import java.util.UUID;

public class MachineImpl implements Machine {

    private final MachineInstance instance;

    private final LoadedMachineConfiguration configuration;
    private final Location location;
    private final String id;
    private final UUID owner;

    public MachineImpl(MachineInformation information, MachinesManager manager) {
        this.configuration = manager.getMachineConfiguration(information.type);
        this.location = Utilities.fromId(information.id);
        this.id = information.id;
        this.owner = information.owner;

        this.instance = new MachineInstanceImpl(information, this);
    }

    @Override
    public MachineInstance getInstance() {
        return instance;
    }

    @Override
    public LoadedMachineConfiguration getMachineConfiguration() {
        return configuration;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public Location getLocation() {
        return location.clone();
    }

    @Override
    public boolean isValid() {
        return this.location != null && this.location.getWorld() != null && (!isLoaded() || this.location.getBlock().getType().equals(this.configuration.getBlockMaterial()));
    }

    @Override
    public boolean isLoaded() {
        return this.location.getWorld().isChunkLoaded(this.location.getBlockX() >> 4, this.location.getBlockZ() >> 4);
    }

    @Override
    public MachineInformation save() {
        return new MachineInformation(this.id, this.configuration.getConfigurationName(), this.owner, this.instance.isEnabled(), this.instance.getTicksToProduce(), this.instance.getFuelAmount());
    }
}
