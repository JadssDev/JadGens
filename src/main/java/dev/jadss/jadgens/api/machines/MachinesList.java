package dev.jadss.jadgens.api.machines;

import dev.jadss.jadgens.api.actions.ActionResult;
import dev.jadss.jadgens.api.actions.MachineActionType;
import dev.jadss.jadgens.api.actions.impl.*;
import dev.jadss.jadgens.implementations.machines.MachinesListImpl;

import java.util.List;

public interface MachinesList {

    public static MachinesList newList(List<MachineInstance> instances) {
        return new MachinesListImpl(instances);
    }

    /**
     * Get the machines of this list.
     * @return the machines of this list!
     */
    List<Machine> getMachines();

    /**
     * Get the machines of this list in the {@link MachineInstance} format!
     * @return the machines of this list in the {@link MachineInstance} format!
     */
    List<MachineInstance> getMachinesInstances();


    /**
     * Adds the machines specified to this list of machines!
     * @param machine The machines to add.
     */
    void addMachines(Machine... machine);

    /**
     * Adds the machines specified to this list of machines!
     * @param machine the machines to add.
     */
    void addMachines(MachineInstance... machine);


    /**
     * Remove a machine from this list of machines.
     * @param id the id of the machine to remove.
     * @deprecated Preferably use {@link MachinesList#removeMachines(Machine[])} it's more effective.
     */
    @Deprecated
    void removeMachines(String... id);

    /**
     * Removes the machines specified from this list of machines.
     * @param machine the machines to remove.
     */
    void removeMachines(Machine... machine);

    /**
     * Removes the machines specified from this list of machines.
     * @param machine the machines to remove.
     */
    void removeMachines(MachineInstance... machine);


    /**
     * Execute an action in all of these machines.
     * @param action the action to do.
     * @return the result of this action in an Object depending on which type of Action!
     * @see MachineActionType
     * @see ActionResult
     * @see EnabledActionResult
     * @see DisabledActionResult
     * @see PurgedActionResult
     */
    ActionResult executeAction(MachineActionType action);
}
