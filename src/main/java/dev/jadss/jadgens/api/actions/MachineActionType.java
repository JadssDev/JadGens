package dev.jadss.jadgens.api.actions;

import dev.jadss.jadgens.api.actions.impl.DisabledActionResult;
import dev.jadss.jadgens.api.actions.impl.EnabledActionResult;
import dev.jadss.jadgens.api.actions.impl.PurgedActionResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MachineActionType {
    ENABLE_MACHINES(EnabledActionResult.class),
    DISABLE_MACHINES(DisabledActionResult.class),
    PURGE_MACHINES(PurgedActionResult.class);

    private final Class<? extends ActionResult> actionClass;
}
