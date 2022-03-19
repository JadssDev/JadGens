package dev.jadss.jadgens.implementations.machines;

import dev.jadss.jadgens.api.MachinesAPI;
import dev.jadss.jadgens.api.actions.ActionResult;
import dev.jadss.jadgens.api.actions.MachineActionType;
import dev.jadss.jadgens.api.machines.Machine;
import dev.jadss.jadgens.api.machines.MachineInstance;
import dev.jadss.jadgens.api.machines.MachinesList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MachinesListImpl implements MachinesList {

    private final List<MachineInstance> machines;

    public MachinesListImpl(List<MachineInstance> list) {
        machines = new ArrayList<>(list);
    }

    @Override
    public List<Machine> getMachines() {
        return machines.stream().map(MachineInstance::getMachine).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<MachineInstance> getMachinesInstances() {
        return new ArrayList<>(machines);
    }

    @Override
    public void addMachines(Machine... machines) {
        Arrays.stream(machines).map(Machine::getInstance).forEach(this.machines::add);
    }

    @Override
    public void addMachines(MachineInstance... machines) {
        Arrays.stream(machines).forEach(this.machines::add);
    }

    @Override
    @Deprecated
    public void removeMachines(String... id) {
        MachinesAPI api = MachinesAPI.getInstance();
        Arrays.stream(id)
                .map(api::getMachine)
                .forEach(this.machines::remove);
    }

    @Override
    public void removeMachines(Machine... machines) {
        Arrays.stream(machines)
                .map(Machine::getInstance)
                .forEach(this.machines::remove);
    }

    @Override
    public void removeMachines(MachineInstance... machines) {
        Arrays.stream(machines)
                .forEach(this.machines::remove);
    }

    @Override
    public ActionResult executeAction(MachineActionType action) {
        if (action == MachineActionType.ENABLE_MACHINES) {
            ActionResult enabledActionResult = ActionResult.newEnabledAction(machines.stream().map(machine -> machine.getMachine().save()).collect(Collectors.toList()));
            machines.forEach(machine -> machine.setEnabled(true));
            return enabledActionResult;
        } else if (action == MachineActionType.DISABLE_MACHINES) {
            ActionResult disableActionResult = ActionResult.newDisabledAction(machines.stream().map(machine -> machine.getMachine().save()).collect(Collectors.toList()));
            machines.forEach(machine -> machine.setEnabled(false));
            return disableActionResult;
        } else if (action == MachineActionType.PURGE_MACHINES) {
            ActionResult purgeActionResult = ActionResult.newPurgedAction(machines.stream().map(machine -> machine.getMachine().save()).collect(Collectors.toList()));
            machines.forEach(MachineInstance::remove);
            return purgeActionResult;
        } else {
            throw new RuntimeException("Unknown action type: " + action);
        }
    }
}
