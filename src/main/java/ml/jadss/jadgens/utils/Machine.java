package ml.jadss.jadgens.utils;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.dependencies.Compatibility;
import ml.jadss.jadgens.dependencies.nbt.NBTCompound;
import ml.jadss.jadgens.dependencies.nbt.NBTItem;
import ml.jadss.jadgens.dependencies.nbt.NbtApiException;
import ml.jadss.jadgens.events.MachineProduceEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Machine {

    private String id;
    private Location location;
    private Integer type;
    private String uuid;
    private Integer dropsRemaining;
    private Integer dropsMax;
    private boolean machineEnabled;
    private final ParticleSystem particleSys = new ParticleSystem();

    /**
     * Use the methods in the machine without creating one.
     */
    public Machine() {
        this.id = null;
        this.location = null;
        this.type = null;
        this.uuid = null;
        this.dropsRemaining = null;
        this.dropsMax = null;
        this.machineEnabled = true;
    }

    /**
     * Creates a machine using some stuff
     * @param world the World of the machine
     * @param x the X cord.
     * @param y the Y cord.
     * @param z the Z cord.
     * @param type the machine type
     * @param uuid the owner of the machine.
     */
    public Machine(World world, int x, int y, int z, int type, String uuid) {
        this.id = world.getName() + "_" + x + "_" + y + "_" + z;
        this.location = new Location(world, x, y, z).add(0.5, 0, 0.5);
        this.type = type;
        this.uuid = uuid;
        this.dropsRemaining = 0;
        this.dropsMax = JadGens.getInstance().getConfig().getInt("machines." + type + ".fuels.maxFuel");
        this.machineEnabled = true;

        data().set("machines." + this.getId() + ".owner", this.getOwner());
        data().set("machines." + this.getId() + ".world", this.getLocation().getWorld().getName());
        data().set("machines." + this.getId() + ".x", this.getLocation().getBlockX());
        data().set("machines." + this.getId() + ".y", this.getLocation().getBlockY());
        data().set("machines." + this.getId() + ".z", this.getLocation().getBlockZ());
        data().set("machines." + this.getId() + ".type", this.getType());
        data().set("machines." + this.getId() + ".drops", this.getDropsRemaining());
        data().set("machines." + this.getId() + ".enabled", this.machineEnabled);
        JadGens.getInstance().getDataFile().saveData();
    }

    /**
     * Creates a machine using some stuff
     * @param loc the Location of the machine
     * @param type the machine type
     * @param uuid the owner of the machine.
     */
    public Machine(Location loc, int type, String uuid) {
        this.id = loc.getWorld().getName() + "_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
        this.location = loc.add(0.5, 0, 0.5);;
        this.type = type;
        this.uuid = uuid;
        this.dropsRemaining = 0;
        this.dropsMax = JadGens.getInstance().getConfig().getInt("machines." + type + ".fuels.maxFuel");
        this.machineEnabled = true;

        data().set("machines." + this.getId() + ".owner", this.getOwner());
        data().set("machines." + this.getId() + ".world", this.getLocation().getWorld().getName());
        data().set("machines." + this.getId() + ".x", this.getLocation().getBlockX());
        data().set("machines." + this.getId() + ".y", this.getLocation().getBlockY());
        data().set("machines." + this.getId() + ".z", this.getLocation().getBlockZ());
        data().set("machines." + this.getId() + ".type", this.getType());
        data().set("machines." + this.getId() + ".drops", this.getDropsRemaining());
        data().set("machines." + this.getId() + ".enabled", this.machineEnabled);
        JadGens.getInstance().getDataFile().saveData();
    }

    /**
     * This gets a machine.
     * @param block Block to get the machine from
     */
    public Machine(Block block) {
        String ID = block.getLocation().getWorld().getName() + "_" + block.getLocation().getBlockX() + "_" + block.getLocation().getBlockY() + "_" + block.getLocation().getBlockZ();
        if (data().isConfigurationSection("machines." + ID)) {
            this.id = block.getLocation().getWorld().getName() + "_" + block.getLocation().getBlockX() + "_" + block.getLocation().getBlockY() + "_" + block.getLocation().getBlockZ();
            this.location = new Location(Bukkit.getServer().getWorld(data().getString("machines." + id + ".world")), data().getInt("machines." + id + ".x"), data().getInt("machines." + id + ".y"), data().getInt("machines." + id + ".z"));
            this.type = data().getInt("machines." + id + ".type");
            this.uuid = data().getString("machines." + id + ".owner");
            this.dropsRemaining = data().getInt("machines." + id + ".drops");
            this.dropsMax = JadGens.getInstance().getConfig().getInt("machines." + type + ".fuels.maxFuel");
            this.machineEnabled = data().getBoolean("machines." + id + ".enabled");
            return;
        }

        this.id = null;
        this.location = null;
        this.type = null;
        this.uuid = null;
        this.dropsRemaining = null;
        this.dropsMax = null;
        this.machineEnabled = false;
    }

    /**
     * This gets a machine.
     * @param id Id from the machine.
     */
    public Machine(String id) {
        if (!data().isConfigurationSection("machines." + id)) {
            this.id = null;
            this.location = null;
            this.type = null;
            this.uuid = null;
            this.dropsRemaining = null;
            this.machineEnabled = false;
            return;
        }

        this.id = id;
        this.location = new Location(Bukkit.getServer().getWorld(data().getString("machines." + id + ".world")), data().getInt("machines." + id + ".x"), data().getInt("machines." + id + ".y"), data().getInt("machines." + id + ".z")).add(0.5, 0, 0.5);
        this.type = data().getInt("machines." + id + ".type");
        this.uuid = data().getString("machines." + id + ".owner");
        this.dropsRemaining = data().getInt("machines." + id + ".drops");
        this.dropsMax = JadGens.getInstance().getConfig().getInt("machines." + type + ".fuels.maxFuel");
        this.machineEnabled = data().getBoolean("machines." + id + ".enabled");
    }

    @Deprecated
    public void addToConfig() {
        data().set("machines." + this.getId() + ".owner", this.getOwner());
        data().set("machines." + this.getId() + ".world", this.getLocation().getWorld().getName());
        data().set("machines." + this.getId() + ".x", this.getLocation().getBlockX());
        data().set("machines." + this.getId() + ".y", this.getLocation().getBlockY());
        data().set("machines." + this.getId() + ".z", this.getLocation().getBlockZ());
        data().set("machines." + this.getId() + ".type", this.getType());
        data().set("machines." + this.getId() + ".drops", this.getDropsRemaining());
        data().set("machines." + this.getId() + ".enabled", this.machineEnabled);
        JadGens.getInstance().getDataFile().saveData();
    }

    //
    //
    //
    //Standalone sh*t.
    public boolean isMachineItem(ItemStack item) {
        if (item == null) return false;
        if (!item.hasItemMeta()) return false;
        if (!item.getItemMeta().hasDisplayName()) return false;
        if (!JadGens.getInstance().getCompatibilityMode()) {
            NBTCompound nbtCompound = new NBTItem(item);
            return nbtCompound.hasKey("JadGens_machine");
        } else {
            for (String key : JadGens.getInstance().getConfig().getConfigurationSection("machines").getKeys(false))
                if (ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machines." + key + ".displayName")).equals(item.getItemMeta().getDisplayName())) return true;
            return false;
        }
    }

    public List<Integer> getExistingTypes() {
        Set<String> typesAsString = JadGens.getInstance().getConfig().getConfigurationSection("machines.").getKeys(false);
        List<Integer> types = new ArrayList<>();
        for (String type : typesAsString) { types.add(Integer.parseInt(type)); }

        return types;
    }

    public boolean typeExists(int type) {
        return this.getExistingTypes().contains(type);
    }

    public boolean typeExists(String type) {
        int typeInteger = -1;
        try {
            typeInteger = Integer.parseInt(type);
        } catch(NumberFormatException ignored) {
            return false;
        }

        return this.getExistingTypes().contains(typeInteger);
    }

    //Standalone sh*t.
    //
    //
    //

    public Inventory createGUI() {
        if (this.id == null) return null;
        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machineGui.title")));

        ItemStack backitem = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("machineGui.backItem.material")), 1, (short) JadGens.getInstance().getConfig().getInt("machineGui.backItem.damage"));
        for (int i = 8; i > -1; i--) {
            gui.setItem(i, backitem);
        }

        if (JadGens.getInstance().getConfig().getBoolean("machineGui.dropsItem.enabled")) {
            ItemStack dropsItem = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("machineGui.dropsItem.item.material")),
                    1,
                    (short) JadGens.getInstance().getConfig().getInt("machineGui.dropsItem.item.damage"));

            ItemMeta dropsMeta = dropsItem.getItemMeta();

            dropsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machineGui.dropsItem.displayName")));
            List<String> lore = new ArrayList<>();
            if (JadGens.getInstance().getConfig().getBoolean("machines." + this.type + ".fuels.needsFuelToProduce")) {
                for (String s : JadGens.getInstance().getConfig().getStringList("machineGui.dropsItem.lore")) {
                    //placeholders: %remaining%, %max%
                    lore.add(ChatColor.translateAlternateColorCodes('&',
                            s.replace("%remaining%", String.valueOf(this.getDropsRemaining()))
                                    .replace("%max%", String.valueOf(this.getDropsMax()))));
                }
            } else {
                for (String s : JadGens.getInstance().getConfig().getStringList("machineGui.dropsItem.infiniteLore")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', s));
                }
            }

            dropsMeta.setLore(lore);
            dropsItem.setItemMeta(dropsMeta);

            NBTItem item = new NBTItem(dropsItem);
            item.setBoolean("JadGens_dropsItem", true);
            item.setString("JadGens_machineID", this.getId());


            gui.setItem(JadGens.getInstance().getConfig().getInt("machineGui.dropsItem.slot") - 1, item.getItem());
        }

        if (JadGens.getInstance().getConfig().getBoolean("machineGui.ownerItem.enabled")) {
            ItemStack dropsItem = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("machineGui.ownerItem.item.material")),
                    1,
                    (short) JadGens.getInstance().getConfig().getInt("machineGui.ownerItem.item.damage"));

            ItemMeta dropsMeta = dropsItem.getItemMeta();

            dropsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machineGui.ownerItem.displayName")));
            List<String> lore = new ArrayList<>();

            String owner;
            if (this.getOwner().equalsIgnoreCase("none")) {
                owner = JadGens.getInstance().getLangFile().lang().getString("messages.machinesMessages.noOwnerInGui");
            } else {
                owner = Bukkit.getOfflinePlayer(UUID.fromString(this.getOwner())).getName();
            }

            for (String s : JadGens.getInstance().getConfig().getStringList("machineGui.ownerItem.lore")) {
                //placeholders: %owner%
                lore.add(ChatColor.translateAlternateColorCodes('&', s.replace("%owner%", owner)));
            }

            dropsMeta.setLore(lore);
            dropsItem.setItemMeta(dropsMeta);

            gui.setItem(JadGens.getInstance().getConfig().getInt("machineGui.ownerItem.slot") - 1, dropsItem);
        }

        if (JadGens.getInstance().getConfig().getBoolean("machineGui.checkItem.enabled")) {
            ItemStack enabledItem;
            if (this.isMachineEnabled()) {
                enabledItem = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("machineGui.checkItem.item.enabledItem.material")),
                        1,
                        (short) JadGens.getInstance().getConfig().getInt("machineGui.checkItem.item.enabledItem.damage"));

                ItemMeta enabledMeta = enabledItem.getItemMeta();

                enabledMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machineGui.checkItem.displayName")
                        .replace("%enabled%", this.getMachineStatusText())));

                List<String> lore = new ArrayList<>();
                for (String s : JadGens.getInstance().getConfig().getStringList("machineGui.checkItem.lore")) {
                    //placeholders: %enabled%
                    lore.add(ChatColor.translateAlternateColorCodes('&', s.replace("%enabled%", this.getMachineStatusText())));
                }

                enabledMeta.setLore(lore);
                enabledItem.setItemMeta(enabledMeta);
            } else {
                enabledItem = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("machineGui.checkItem.item.disabledItem.material")),
                        1,
                        (short) JadGens.getInstance().getConfig().getInt("machineGui.checkItem.item.disabledItem.damage"));

                ItemMeta enabledMeta = enabledItem.getItemMeta();

                enabledMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machineGui.checkItem.displayName")
                        .replace("%enabled%", this.getMachineStatusText())));

                List<String> lore = new ArrayList<>();
                for (String s : JadGens.getInstance().getConfig().getStringList("machineGui.checkItem.lore")) {
                    //placeholders: %enabled%
                    lore.add(ChatColor.translateAlternateColorCodes('&', s.replace("%enabled%", this.getMachineStatusText())));
                }

                enabledMeta.setLore(lore);
                enabledItem.setItemMeta(enabledMeta);
            }
            NBTItem item = new NBTItem(enabledItem);
            item.setBoolean("JadGens_toggleItem", true);
            item.setString("JadGens_machineID", this.getId());

            gui.setItem(JadGens.getInstance().getConfig().getInt("machineGui.checkItem.slot") - 1, item.getItem());
        }

        if (JadGens.getInstance().getConfig().getBoolean("machineGui.closeItem.enabled")) {
            ItemStack item = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("machineGui.closeItem.item.material")),
                    1,
                    (short) JadGens.getInstance().getConfig().getInt("machineGui.closeItem.item.damage"));

            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machineGui.closeItem.displayName")));

            List<String> lore = new ArrayList<>();
            for (String s : JadGens.getInstance().getConfig().getStringList("machineGui.closeItem.lore")) {
                lore.add(ChatColor.translateAlternateColorCodes('&', s));
            }
            meta.setLore(lore);

            item.setItemMeta(meta);

            NBTItem nbt = new NBTItem(item);
            nbt.setBoolean("JadGens_closeItem", true);
            nbt.setString("JadGens_machineID", this.getId());

            gui.setItem(JadGens.getInstance().getConfig().getInt("machineGui.closeItem.slot") - 1, nbt.getItem());
        }

        return gui;
    }

    public ItemStack createItem(int id) {
        ItemStack machine = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("machines." + id + ".MachineBlock.material")), 1, (short) JadGens.getInstance().getConfig().getInt("Machines." + id + ".MachineBlock.damage"));
        ItemMeta meta = machine.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machines." + id + ".displayName")));
        List<String> lore = new ArrayList<>();
        for (String s : JadGens.getInstance().getConfig().getStringList("machines." + id + ".lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', s));
        }

        if (!JadGens.getInstance().getCompatibilityMode()) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            if (JadGens.getInstance().getConfig().getBoolean("machines." + id + ".glow")) {
                meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
        }

        meta.setLore(lore);
        machine.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(machine);
        try {
            nbtItem.setBoolean("JadGens_machine", true);
            nbtItem.setInteger("JadGens_machineType", id);
        } catch (NbtApiException e) {
            return null;
        }

        machine = nbtItem.getItem();

        return machine;
    }

    /**
     * Make the machine produce....
     */
    public void produce() {
        if (this.getOwner().equalsIgnoreCase("none")) this.setMachineEnabled(false);

        World wl = Bukkit.getServer().getWorld(data().getString("machines." + this.id + ".world"));
        if (wl == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eThe &3&lMachine &eWith &b&lID &3" + this.id + " &eWas &c&lNot &b&lFound&e!"));
            new MachinePurger().removeMachine(this.getId());
            return;
        }
        //////////////////////////////////////////////

        if (JadGens.getInstance().getConfig().getBoolean("machines." + type + ".fuels.needsFuelToProduce")) {
            if (data().getInt("machines." + id + ".drops") > 0) {
                data().set("machines." + id + ".drops", data().getInt("machines." + id + ".drops")-1);
            } else return;
        }

        if (!this.machineEnabled) return;

        MachineProduceEvent machineProduceEvent = new MachineProduceEvent(this);
        JadGens.getInstance().getServer().getPluginManager().callEvent(machineProduceEvent);
        if (machineProduceEvent.isCancelled()) return;

        if (JadGens.getInstance().getConfig().getBoolean("machinesConfig.stopProducingIfOffline")) {
            Player testOnline = Bukkit.getPlayer(UUID.fromString(data().getString("machines." + this.id + ".owner")));
            if (testOnline == null) return;
        }

        if (JadGens.getInstance().getConfig().getBoolean("machines." + this.type + ".dropItems.enabled")) {
            Location location = this.getLocation();
            location.add(0, 1, 0);

            ItemStack dropItem = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("machines." + this.type + ".dropItems.material")),
                    JadGens.getInstance().getConfig().getInt("machines." + this.type + ".dropItems.amount"),
                    (short) JadGens.getInstance().getConfig().getInt("machines." + this.type + ".dropItems.damage"));

            if (JadGens.getInstance().getConfig().getBoolean("machines." + this.type + ".dropItems.itemMeta.enabled")) {
                ItemMeta meta = dropItem.getItemMeta();

                //set displayName
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machines." + this.type + ".dropItems.itemMeta.displayName")));

                //set lore
                List<String> lore = new ArrayList<>();
                for (String line : JadGens.getInstance().getConfig().getStringList("machines." + this.type + ".dropItems.itemMeta.lore")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', line));
                }
                meta.setLore(lore);

                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

                if (!JadGens.getInstance().getCompatibilityMode() && JadGens.getInstance().getConfig().getBoolean("machines." + this.type + ".dropItems.itemMeta.glow")) {
                    meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }

                //set itemMeta
                dropItem.setItemMeta(meta);
            }


            if (JadGens.getInstance().getConfig().getBoolean("machines." + this.type + ".dropItems.nbt.enabled")) {
                NBTItem nbt = new NBTItem(dropItem);

                for (String key : JadGens.getInstance().getConfig().getConfigurationSection("machines." + this.type + ".dropItems.nbt.booleans").getKeys(false)) {
                    String nbtKey = JadGens.getInstance().getConfig().getString("machines." + this.type + ".dropItems.nbt.booleans." + key + ".key");
                    boolean value = JadGens.getInstance().getConfig().getBoolean("machines." + this.type + ".dropItems.nbt.booleans." + key + ".value");

                    if (nbtKey == null) continue;

                    nbt.setBoolean(nbtKey, value);
                }

                for (String key : JadGens.getInstance().getConfig().getConfigurationSection("machines." + this.type + ".dropItems.nbt.integers").getKeys(false)) {
                    String nbtKey = JadGens.getInstance().getConfig().getString("machines." + this.type + ".dropItems.nbt.integers." + key + ".key");
                    int value = JadGens.getInstance().getConfig().getInt("machines." + this.type + ".dropItems.nbt.integers." + key + ".value");

                    if (nbtKey == null) continue;

                    nbt.setInteger(nbtKey, value);
                }

                for (String key : JadGens.getInstance().getConfig().getConfigurationSection("machines." + this.type + ".dropItems.nbt.strings").getKeys(false)) {
                    String nbtKey = JadGens.getInstance().getConfig().getString("machines." + this.type + ".dropItems.nbt.strings." + key + ".key");
                    String value = JadGens.getInstance().getConfig().getString("machines." + this.type + ".dropItems.nbt.strings." + key + ".value");

                    if (nbtKey == null) continue;

                    nbt.setString(nbtKey, value);
                }

                dropItem = nbt.getItem();
            }

            wl.dropItem(location, dropItem); //itemMeta
        }

        if (JadGens.getInstance().getConfig().getBoolean("machines." + this.type + ".commands.enabled")) {
            Player pl = Bukkit.getPlayer(UUID.fromString(data().getString("machines." + this.id + ".owner")));
            if (!(pl == null)) {
                for (String command : JadGens.getInstance().getConfig().getStringList("machines." + this.type + ".commands.commands")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%owner%", pl.getName()));
                }
            }
        }

        if (JadGens.getInstance().getConfig().getBoolean("machines." + this.type + ".economy.enabled") && JadGens.getInstance().isHookedVault()) {
            OfflinePlayer pl = Bukkit.getOfflinePlayer(UUID.fromString(data().getString("machines." + this.id + ".owner")));
            JadGens.getInstance().getEco().depositPlayer(pl, JadGens.getInstance().getConfig().getDouble("machines." + this.type + ".economy.give"));
        }

        if (JadGens.getInstance().getConfig().getBoolean("machines." + this.type + ".points.enabled") && JadGens.getInstance().isHookedPlayerPoints()) {
            UUID player = UUID.fromString(data().getString("machines." + this.id + ".owner"));
            JadGens.getInstance().getPointsAPI().give(player, JadGens.getInstance().getConfig().getInt("machines." + this.type + ".points.give"));
        }

        if (JadGens.getInstance().getConfig().getBoolean("machines." + this.type + ".exp.enabled")) {
            Player onlinePL = Bukkit.getPlayer(UUID.fromString(data().getString("machines." + this.id + ".owner")));
            if (onlinePL == null) return;
            onlinePL.setLevel(onlinePL.getLevel() + JadGens.getInstance().getConfig().getInt("machines." + this.type + ".exp.givelevels"));
        }

        this.showParticles();
    }

    /**
     * force the machine to produce, no matter what, even if it has no fuel.
     */
    public void forceProduce() {
        if (this.getOwner().equalsIgnoreCase("none")) this.setMachineEnabled(false);

        World wl = Bukkit.getServer().getWorld(data().getString("machines." + this.id + ".world"));
        if (wl == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eThe &3&lMachine &eWith &b&lID &3" + this.id + " &eWas &c&lNot &b&lFound&e!"));
            new MachinePurger().removeMachine(this.getId());
            return;
        }
        //////////////////////////////////////////////

        MachineProduceEvent machineProduceEvent = new MachineProduceEvent(this);
        JadGens.getInstance().getServer().getPluginManager().callEvent(machineProduceEvent);
        if (machineProduceEvent.isCancelled()) return;

        if (JadGens.getInstance().getConfig().getBoolean("machinesConfig.stopProducingIfOffline")) {
            Player testOnline = Bukkit.getPlayer(UUID.fromString(data().getString("machines." + this.id + ".owner")));
            if (testOnline == null) return;
        }

        if (JadGens.getInstance().getConfig().getBoolean("machines." + this.type + ".dropItems.enabled")) {
            Location location = this.getLocation();
            location.add(0, 1, 0);

            ItemStack dropItem = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("machines." + this.type + ".dropItems.material")),
                    JadGens.getInstance().getConfig().getInt("machines." + this.type + ".dropItems.amount"),
                    (short) JadGens.getInstance().getConfig().getInt("machines." + this.type + ".dropItems.damage"));

            if (JadGens.getInstance().getConfig().getBoolean("machines." + this.type + ".dropItems.itemMeta.enabled")) {
                ItemMeta meta = dropItem.getItemMeta();

                //set displayName
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machines." + this.type + ".dropItems.itemMeta.displayName")));

                //set lore
                List<String> lore = new ArrayList<>();
                for (String line : JadGens.getInstance().getConfig().getStringList("machines." + this.type + ".dropItems.itemMeta.lore")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', line));
                }
                meta.setLore(lore);

                //set itemMeta
                dropItem.setItemMeta(meta);
            }

            if (JadGens.getInstance().getConfig().getBoolean("machines." + this.type + "dropItems.nbt.enabled")) {
                NBTItem nbt = new NBTItem(dropItem);

                for (String key : JadGens.getInstance().getConfig().getConfigurationSection("machines." + this.type + ".dropItems.nbt.booleans").getKeys(false)) {
                    String nbtKey = JadGens.getInstance().getConfig().getString("machines." + this.type + ".dropItems.nbt.booleans." + key + ".key");
                    boolean value = JadGens.getInstance().getConfig().getBoolean("machines." + this.type + ".dropItems.nbt.booleans." + key + ".value");

                    nbt.setBoolean(nbtKey, value);
                }

                for (String key : JadGens.getInstance().getConfig().getConfigurationSection("machines." + this.type + ".dropItems.nbt.integers").getKeys(false)) {
                    String nbtKey = JadGens.getInstance().getConfig().getString("machines." + this.type + ".dropItems.nbt.integers." + key + ".key");
                    int value = JadGens.getInstance().getConfig().getInt("machines." + this.type + ".dropItems.nbt.integers." + key + ".value");

                    nbt.setInteger(nbtKey, value);
                }

                for (String key : JadGens.getInstance().getConfig().getConfigurationSection("machines." + this.type + ".dropItems.nbt.strings").getKeys(false)) {
                    String nbtKey = JadGens.getInstance().getConfig().getString("machines." + this.type + ".dropItems.nbt.strings." + key + ".key");
                    String value = JadGens.getInstance().getConfig().getString("machines." + this.type + ".dropItems.nbt.strings." + key + ".value");

                    nbt.setString(nbtKey, value);
                }

                dropItem = nbt.getItem();
            }

            wl.dropItem(location, dropItem); //itemMeta
        }

        if (JadGens.getInstance().getConfig().getBoolean("machines." + this.type + ".commands.enabled")) {
            Player pl = Bukkit.getPlayer(UUID.fromString(data().getString("machines." + this.id + ".owner")));
            if (!(pl == null)) {
                for (String command : JadGens.getInstance().getConfig().getStringList("machines." + this.type + ".commands.commands")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%owner%", pl.getName()));
                }
            }
        }

        if (JadGens.getInstance().getConfig().getBoolean("machines." + this.type + ".economy.enabled") && JadGens.getInstance().isHookedVault()) {
            OfflinePlayer pl = Bukkit.getOfflinePlayer(UUID.fromString(data().getString("machines." + this.id + ".owner")));
            JadGens.getInstance().getEco().depositPlayer(pl, JadGens.getInstance().getConfig().getDouble("machines." + this.type + ".economy.give"));
        }

        if (JadGens.getInstance().getConfig().getBoolean("machines." + this.type + ".points.enabled") && JadGens.getInstance().isHookedPlayerPoints()) {
            UUID player = UUID.fromString(data().getString("machines." + this.id + ".owner"));
            JadGens.getInstance().getPointsAPI().give(player, JadGens.getInstance().getConfig().getInt("machines." + this.type + ".points.give"));
        }

        if (JadGens.getInstance().getConfig().getBoolean("machines." + this.type + ".exp.enabled")) {
            Player onlinePL = Bukkit.getPlayer(UUID.fromString(data().getString("machines." + this.id + ".owner")));
            if (onlinePL == null) return;
            onlinePL.setLevel(onlinePL.getLevel() + JadGens.getInstance().getConfig().getInt("machines." + this.type + ".exp.givelevels"));
        }

        this.showParticles();
    }

    public void showParticles() {
        if (JadGens.getInstance().getConfig().getBoolean("machineParticles.enabled") && JadGens.getInstance().getConfig().getBoolean("machineParticles.conditions.onProduce.enabled")) {
            Location loc = this.getLocation();

            for (int i = 0; i < JadGens.getInstance().getConfig().getInt("machineParticles.particle.rows"); i++) {
                for (int x = 0; x < JadGens.getInstance().getConfig().getInt("machineParticles.particle.times"); x++) {
                    boolean value = particleSys.spawnParticle(loc);
                    if (!value) return;
                }
            }
            loc.setY(loc.getY() + 0.01);
        }
    }


    public String getId() { return this.id; }
    public Location getLocation() { return this.location; }
    public Integer getType() { return this.type; }
    public String getOwner() { return this.uuid; }
    public Integer getDropsRemaining() { return this.dropsRemaining; }
    public Integer getDropsMax() { return this.dropsMax; }
    public boolean isMachineEnabled() { return this.machineEnabled; }
    public void setMachineEnabled(boolean isEnabled) { this.machineEnabled = isEnabled; data().set("machines." + this.id + ".enabled", isEnabled); }
    public void setDropsRemaining(Integer drops) { this.dropsRemaining = drops; data().set("machines." + this.id + ".drops", drops); JadGens.getInstance().getDataFile().saveData(); JadGens.getInstance().getDataFile().reloadData(); }

    public String getDisplayName() { return ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machines." + this.getType() + ".displayName")); }

    public String getMachineStatusText() {
        if (isMachineEnabled())
            return ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machineGui.checkItem.settings.enabledText"));
        else
            return ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machineGui.checkItem.settings.disabledText"));
    }

    public boolean isFuelCompatible(int fuelType) {
        if (JadGens.getInstance().getConfig().getBoolean("machines." + this.getType() + ".fuels.fuelsAccepted.enabled"))
            return JadGens.getInstance().getConfig().getIntegerList("machines." + this.getType() + ".fuels.fuelsAccepted.types").contains(fuelType);
        return true;
    }

    protected FileConfiguration data() { return JadGens.getInstance().getDataFile().data(); }
}
