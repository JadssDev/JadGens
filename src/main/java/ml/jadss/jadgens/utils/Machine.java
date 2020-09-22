package ml.jadss.jadgens.utils;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.nbt.NBTItem;
import ml.jadss.jadgens.nbt.NbtApiException;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Machine {

    private String id;
    private Location location;
    private Integer type;
    private String uuid;
    private Integer dropsRemaining;

    public Machine() {
        this.id = null;
        this.location = null;
        this.type = null;
        this.uuid = null;
        this.dropsRemaining = null;
    }

    public Machine(World world, int x, int y, int z, int type, String uuid) {
        this.id = world.getName() + "_" + x + "_" + y + "_" + z;
        this.location = new Location(world, x, y, z);
        this.type = type;
        this.uuid = uuid;
        this.dropsRemaining = 0;
    }

    public Machine(Location loc, int type, String uuid) {
        this.id = loc.getWorld().getName() + "_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
        this.location = loc;
        this.type = type;
        this.uuid = uuid;
        this.dropsRemaining = 0;
    }

    public Machine(Block block) {
        String configID = block.getLocation().getWorld().getName() + "_" + block.getLocation().getBlockX() + "_" + block.getLocation().getBlockY() + "_" + block.getLocation().getBlockZ();
        if (data().isConfigurationSection("machines." + configID)) {
            this.id = block.getLocation().getWorld().getName() + "_" + block.getLocation().getBlockX() + "_" + block.getLocation().getBlockY() + "_" + block.getLocation().getBlockZ();
            this.location = new Location(Bukkit.getServer().getWorld(data().getString("machines." + id + ".world")), data().getInt("machines." + id + ".x"), data().getInt("machines." + id + ".y"), data().getInt("machines." + id + ".z"));
            this.type = data().getInt("machines." + id + ".type");
            this.uuid = data().getString("machines." + id + ".owner");
            this.dropsRemaining = data().getInt("machines." + id + ".drops");
            return;
        }

        this.id = null;
        this.location = null;
        this.type = null;
        this.uuid = null;
        this.dropsRemaining = null;
    }

    public Machine(String id) {
        if (!data().isConfigurationSection("machines." + id)) {
            this.id = null;
            this.location = null;
            this.type = null;
            this.uuid = null;
            this.dropsRemaining = null;
            return;
        }

        this.id = id;
        this.location = new Location(Bukkit.getServer().getWorld(data().getString("machines." + id + ".world")), data().getInt("machines." + id + ".x"), data().getInt("machines." + id + ".y"), data().getInt("machines." + id + ".z"));
        this.type = data().getInt("machines." + id + ".type");
        this.uuid = data().getString("machines." + id + ".owner");
        this.dropsRemaining = data().getInt("machines." + id + ".drops");
    }

    public void addToConfig() {
        data().set("machines." + this.getId() + ".owner", this.getOwner());
        data().set("machines." + this.getId() + ".world", this.getLocation().getWorld().getName());
        data().set("machines." + this.getId() + ".x", this.getLocation().getBlockX());
        data().set("machines." + this.getId() + ".y", this.getLocation().getBlockY());
        data().set("machines." + this.getId() + ".z", this.getLocation().getBlockZ());
        data().set("machines." + this.getId() + ".type", this.getType());
        data().set("machines." + this.getId() + ".drops", this.getDropsRemaining());
        JadGens.getInstance().getDataFile().saveData();
    }

    public void removefromConfig() {
        data().set("machines." + this.getId(), null);
        JadGens.getInstance().getDataFile().saveData();
    }

    public void setDropsRemaining(Integer drops) { this.dropsRemaining = drops; }

    public Inventory createGUI() {
        if (this.id == null) return null;
        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machineGui.title")));

        ItemStack backitem = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("machineGui.backItem.material")), 1, (short) JadGens.getInstance().getConfig().getInt("machineGui.backItem.damage"));
        for (int i = 8; i > -1; i--) {
            gui.setItem(i, backitem);
        }

        if (JadGens.getInstance().getConfig().getBoolean("machineGui.dropsCheckItem.enabled")) {
            ItemStack dropsItem = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("machineGui.dropsCheckItem.item.material")),
                    1,
                    (short) JadGens.getInstance().getConfig().getInt("machineGui.dropsCheckItem.item.damage"));

            ItemMeta dropsMeta = dropsItem.getItemMeta();

            dropsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machineGui.dropsCheckItem.displayName")));
            List<String> lore = new ArrayList<>();
            if (JadGens.getInstance().getConfig().getBoolean("machines." + this.type + ".needsFuelToProduce")) {
                for (String s : JadGens.getInstance().getConfig().getStringList("machineGui.dropsCheckItem.lore")) {
                    //placeholders: %remaining%, %max%
                    lore.add(ChatColor.translateAlternateColorCodes('&',
                            s.replace("%remaining%", String.valueOf(this.getDropsRemaining()))
                                    .replace("%max%", String.valueOf(JadGens.getInstance().getConfig().getInt("machines." + this.type + ".maxFuel")))));
                }
            } else {
                for (String s : JadGens.getInstance().getConfig().getStringList("machineGui.dropsCheckItem.infiniteLore")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', s));
                }
            }

            dropsMeta.setLore(lore);
            dropsItem.setItemMeta(dropsMeta);

            gui.setItem(JadGens.getInstance().getConfig().getInt("machineGui.dropsCheckItem.slot") - 1, dropsItem);
        }

        if (JadGens.getInstance().getConfig().getBoolean("machineGui.ownerCheckItem.enabled")) {
            ItemStack dropsItem = new ItemStack(new Compatibility().matParser(JadGens.getInstance().getConfig().getString("machineGui.ownerCheckItem.item.material")),
                    1,
                    (short) JadGens.getInstance().getConfig().getInt("machineGui.ownerCheckItem.item.damage"));

            ItemMeta dropsMeta = dropsItem.getItemMeta();

            dropsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("machineGui.ownerCheckItem.displayName")));
            List<String> lore = new ArrayList<>();
            for (String s : JadGens.getInstance().getConfig().getStringList("machineGui.ownerCheckItem.lore")) {
                //placeholders: %owner%
                lore.add(ChatColor.translateAlternateColorCodes('&', s.replace("%owner%", Bukkit.getOfflinePlayer(UUID.fromString(this.getOwner())).getName())));
            }

            dropsMeta.setLore(lore);
            dropsItem.setItemMeta(dropsMeta);

            gui.setItem(JadGens.getInstance().getConfig().getInt("machineGui.ownerCheckItem.slot") - 1, dropsItem);
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

    public String getId() {
        return id;
    }
    public Location getLocation() { return location; }
    public Integer getType() {
        return type;
    }
    public String getOwner() { return uuid; }
    public Integer getDropsRemaining() {
        return dropsRemaining;
    }

    protected FileConfiguration data() {
        return JadGens.getInstance().getDataFile().data();
    }
}
