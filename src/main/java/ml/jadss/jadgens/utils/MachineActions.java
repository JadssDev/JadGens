package ml.jadss.jadgens.utils;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MachineActions {

    private final MachineLookup lookUp = new MachineLookup();
    private int count = 0;
    private HashMap<Integer, Integer> machines = new HashMap<>();

    public void toAllMachines(MachineAction action) {
        List<Machine> machines = lookUp.getAllMachines();

        if (action == MachineAction.ENABLE) {

            for (Machine machine : machines) {
                machine.setMachineEnabled(true);
                count++;
                int newValue = 0;
                try { newValue = this.machines.get(machine.getType())+1; } catch(NullPointerException ignored) { newValue = 1; }
                this.machines.put(machine.getType(), newValue);
            }

        } else if (action == MachineAction.DISABLE) {

            for (Machine machine : machines) {
                machine.setMachineEnabled(false);
                count++;
                int newValue = 0;
                try { newValue = this.machines.get(machine.getType())+1; } catch(NullPointerException ignored) { newValue = 1; }
                this.machines.put(machine.getType(), newValue);
            }

        } else if (action == MachineAction.PURGE) {

            for (Machine machine : machines) {
                new MachinePurger().removeMachine(machine.getId());
                this.count++;
                int newValue = 0;
                try { newValue = this.machines.get(machine.getType())+1; } catch(NullPointerException ignored) { newValue = 1; }
                this.machines.put(machine.getType(), newValue);
            }

        }
    }

    public void toMachinesFrom(MachineAction action, UUID player, boolean purgeGiveBack) {
        List<Machine> machines = lookUp.getPlayerMachines(player);

        if (action == MachineAction.ENABLE) {

            for (Machine machine : machines) {
                machine.setMachineEnabled(true);
                this.count++;
                int newValue = 0;
                try { newValue = this.machines.get(machine.getType())+1; } catch(NullPointerException ignored) { newValue = 1; }
                this.machines.put(machine.getType(), newValue);
            }

        } else if (action == MachineAction.DISABLE) {

            for (Machine machine : machines) {
                machine.setMachineEnabled(false);
                this.count++;
                int newValue = 0;
                try { newValue = this.machines.get(machine.getType())+1; } catch(NullPointerException ignored) { newValue = 1; }
                this.machines.put(machine.getType(), newValue);
            }

//            List<ItemStack> types = new ArrayList<>();
//            for (int ID : acts.getMachinesRemoved().keySet()) {
//                types.add(new Machine().createItem(ID));
//            }
//
//            for(int type : acts.getMachinesRemoved().keySet()) {
//                ItemStack item = types.get(type - 1);
//                item.setAmount(acts.getMachinesRemoved().get(type));
//                ((Player) sender).getLocation().getWorld().dropItemNaturally(((Player) sender).getLocation(), item);
//            }

        } else if (action == MachineAction.PURGE) {
            new MachinePurger().purgeMachines(player, purgeGiveBack);
            for (int value : this.machines.values()) {
                this.count = this.count+value;
            }
        }
    }

    public int getCount() { return this.count; }
    public HashMap<Integer, Integer> getMachines() { return this.machines; }
    public void reset() { this.count = 0; this.machines.clear(); }
}
