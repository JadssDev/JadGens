package dev.jadss.jadgens.api.actions;

import dev.jadss.jadgens.api.actions.impl.DisabledActionResult;
import dev.jadss.jadgens.api.actions.impl.EnabledActionResult;
import dev.jadss.jadgens.api.actions.impl.PurgedActionResult;
import dev.jadss.jadgens.api.config.serializers.MachineInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Represents actions that have been performed in a specific list of machines.
 * <p><b>Note: All the methods here won't do anything by themselves.</b></p>
 */
@Getter
@AllArgsConstructor
public abstract class ActionResult {

    private final List<MachineInformation> machines;

    public static ActionResult newDisabledAction(List<MachineInformation> machines) {
        return new DisabledActionResult(machines);
    }

    public static ActionResult newEnabledAction(List<MachineInformation> machines) {
        return new EnabledActionResult(machines);
    }

    public static ActionResult newPurgedAction(List<MachineInformation> machines) {
        return new PurgedActionResult(machines);
    }
}
