package dev.jadss.jadgens.api.actions.impl;

import dev.jadss.jadgens.api.actions.ActionResult;
import dev.jadss.jadgens.api.config.serializers.MachineInformation;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class PurgedActionResult extends ActionResult {

    private final Map<String, Integer> machinesAmount;

    public PurgedActionResult(List<MachineInformation> machines) {
        super(machines);
        machinesAmount = new HashMap<>();

        for (MachineInformation information : machines) {
            machinesAmount.putIfAbsent(information.type, 0);
            machinesAmount.compute(information.type, (id, amount) -> {
                return amount + 1;
            });
        }
    }
}
