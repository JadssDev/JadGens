package dev.jadss.jadgens.api.actions.impl;

import dev.jadss.jadgens.api.actions.ActionResult;
import dev.jadss.jadgens.api.config.serializers.MachineInformation;

import java.util.List;

public class EnabledActionResult extends ActionResult {

    public EnabledActionResult(List<MachineInformation> machines) {
        super(machines);
    }
}
