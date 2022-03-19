# JadGens
JadGens is a Machines Plugin with the idea of making players have more fun with custom Machines that could generate Experience, Economy or Points!

## How to Use JadGens API?
To use JadGens API you just have to get the MachinesAPI class which can be obtained using `MachinesAPI.getInstance()` static method!
The rest is just digging through the Javadocs!

Some examples:

### Get the machines of JadGens and disable them all.

```java
public class ExampleClass {

    public void disableAllMachines() {
        for (MachinesInstance instance : MachinesAPI.getInstance().getMachines().getMachinesInstances()) {
            instance.setEnabled(false);
        }
    }
}
```

### Check how many machines are enabled.

```java
public class ExampleClass {
    
    public int countEnabledMachines() {
        int count = 0;
        for (MachinesInstance instance : MachinesAPI.getInstance().getMachines().getMachinesInstances())
            if (instance.isEnabled())
                count++;
        return count;
    }
}
```

### Get the player's drops.

Note: this only works if "sendToMenu" in the machine configuration item drop is set to true, else the machine will just drop the Item into the ground and will not be added to the menu, not being added any value to the drops Instance.
```java
public class ExampleClass {

    public void getPLayerDrops(UUID playerUUID) {
        int count = 0;
        LoadedMachineConfiguration machine = null; //define which MachineConfiguration Drops we want!
        UserMachineDrops drops = MachinesAPI.getInstance().getPlayer(playerUUID).getDropInformation(machine);
        int amount = drops.getAmount(); //how many items of this machine we got!
        drops.addAmount(amount * 0.5); //add 50% of the amount of items, for example.
        if (drops.hasAtLeast(10000)) {
            //check if the player has at least 10000.
        }
    }
}
```

Did not find how to do a specific action in JadGens?
### Contact me on discord! I'll try to help!