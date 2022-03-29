package dev.jadss.jadgens.implementations.machines;

import dev.jadss.jadapi.JadAPIPlugin;
import dev.jadss.jadapi.bukkitImpl.enums.JParticle;
import dev.jadss.jadapi.bukkitImpl.item.JInventory;
import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.bukkitImpl.misc.JHologram;
import dev.jadss.jadapi.bukkitImpl.misc.JWorld;
import dev.jadss.jadapi.management.JQuickEvent;
import dev.jadss.jadgens.JadGens;
import dev.jadss.jadgens.api.MachinesAPI;
import dev.jadss.jadgens.api.config.generalConfig.messages.menu.MachineMenuConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.messages.menu.MenuItemConfiguration;
import dev.jadss.jadgens.api.config.interfaces.LoadedFuelConfiguration;
import dev.jadss.jadgens.api.config.interfaces.LoadedHologramConfiguration;
import dev.jadss.jadgens.api.config.interfaces.LoadedMachineProductionConfiguration;
import dev.jadss.jadgens.api.config.interfaces.LoadedParticleConfiguration;
import dev.jadss.jadgens.api.config.serializers.MachineInformation;
import dev.jadss.jadgens.api.machines.Machine;
import dev.jadss.jadgens.api.machines.MachineInstance;
import dev.jadss.jadgens.api.player.MachinesUser;
import dev.jadss.jadgens.events.MachineProduceEvent;
import dev.jadss.jadgens.events.MachineToggledEvent;
import dev.jadss.jadgens.hooks.Hook;
import dev.jadss.jadgens.implementations.MachinesManager;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import static dev.jadss.jadgens.utils.Utilities.replace;

public class MachineInstanceImpl implements MachineInstance {

    private final Machine machine;

    private boolean enabled;
    private int fuelAmount;
    private int ticksToGenerate;

    private boolean isValid = true;

    private JHologram hologram;

    private JWorld world;
    private OfflinePlayer player;

    public MachineInstanceImpl(MachineInformation information, Machine machine) {
        this.machine = machine;

        this.enabled = information.enabled;
        this.fuelAmount = information.fuelAmount;
        this.ticksToGenerate = information.ticksToGenerate;
    }

    @Override
    public Machine getMachine() {
        return machine;
    }

    @Override
    public void openMachineMenu(Player player) {
        MachineMenuConfiguration menuConfig = MachinesAPI.getInstance().getGeneralConfiguration().getMessages().machineMenu;
        JInventory inventory = new JInventory(menuConfig.rows, menuConfig.title);

        OfflinePlayer owner = Bukkit.getOfflinePlayer(machine.getOwner());

        inventory.fill(new JItemStack(JMaterial.getRegistryMaterials().find(menuConfig.backgroundItem.itemType))
                .setDisplayName(menuConfig.backgroundItem.displayName)
                .setLore(menuConfig.backgroundItem.lore));

        int ownerItemSlot = menuConfig.ownerItem.slot;
        JItemStack ownerItem = new JItemStack(JMaterial.getRegistryMaterials().find(menuConfig.ownerItem.itemType))
                .setDisplayName(menuConfig.ownerItem.displayName)
                .setLore(replace(menuConfig.ownerItem.lore, "%owner%", (owner == null ? menuConfig.ownerItem.noOwnerPlaceholderText : owner.getName())));
        if (menuConfig.ownerItem.glow)
            ownerItem.addEnchantment(JadGens.getInstance().getGlowEnchantment().asEnchantment(), 69);

        MenuItemConfiguration itemConfig = this.isEnabled() ? menuConfig.statusItem.enabled : menuConfig.statusItem.disabled;
        int statusItemSlot = itemConfig.slot;
        JItemStack statusItem = new JItemStack(JMaterial.getRegistryMaterials().find(itemConfig.itemType))
                .setDisplayName(itemConfig.displayName)
                .setLore(itemConfig.lore)
                .setNBTBoolean("JadGens_Status_Item", true);
        if (itemConfig.glow)
            statusItem.addEnchantment(JadGens.getInstance().getGlowEnchantment().asEnchantment(), 69);

        int fuelItemSlot = menuConfig.fuelItem.slot;
        JItemStack fuelItem = new JItemStack(JMaterial.getRegistryMaterials().find(menuConfig.fuelItem.itemType))
                .setDisplayName(menuConfig.fuelItem.displayName)
                .setLore(machine.getMachineConfiguration().getSuperConfiguration().fuels.needsFuelToProduce ? replace(replace(menuConfig.fuelItem.lore, "%remaining%", "" + this.getFuelAmount()), "%max%", String.valueOf(machine.getMachineConfiguration().getMaxFuelAmount())) : menuConfig.fuelItem.infiniteFuelLore)
                .setNBTBoolean("JadGens_Fuel_Item", true);
        if (menuConfig.fuelItem.glow)
            fuelItem.addEnchantment(JadGens.getInstance().getGlowEnchantment().asEnchantment(), 69);

        int closeItemSlot = menuConfig.closeItem.slot;
        JItemStack closeItem = new JItemStack(JMaterial.getRegistryMaterials().find(menuConfig.closeItem.itemType))
                .setDisplayName(menuConfig.closeItem.displayName)
                .setLore(menuConfig.closeItem.lore)
                .setNBTBoolean("JadGens_Close_Item", true);
        if (menuConfig.closeItem.glow)
            fuelItem.addEnchantment(JadGens.getInstance().getGlowEnchantment().asEnchantment(), 69);

        inventory.setItem(ownerItemSlot, ownerItem);
        inventory.setItem(statusItemSlot, statusItem);
        inventory.setItem(fuelItemSlot, fuelItem);
        inventory.setItem(closeItemSlot, closeItem);

        JadAPIPlugin plugin = JadAPIPlugin.get(JadGens.class);

        String quickEventId1 = JQuickEvent.generateID();
        String quickEventId2 = JQuickEvent.generateID();
        String quickEventId3 = JQuickEvent.generateID();

        player.openInventory(inventory.getInventory());

        new JQuickEvent(plugin, InventoryClickEvent.class, e -> {
            if (e.getWhoClicked().getUniqueId().equals(player.getUniqueId())) {
                e.setCancelled(true);

                if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
                    return;

                if (!e.getClickedInventory().equals(inventory.buildInventory()))
                    return;

                JItemStack item = new JItemStack(e.getCurrentItem());

                if (item.getNBTBoolean("JadGens_Status_Item")) {
                    inventory.setItem(e.getSlot(), (ItemStack) null);

                    MachineToggledEvent machineToggleEvent = new MachineToggledEvent(machine.getInstance(), player, !this.isEnabled());
                    Bukkit.getPluginManager().callEvent(machineToggleEvent);
                    if (machineToggleEvent.isCancelled()) {
                        e.setCancelled(true); // idk why
                        return;
                    } else {
                        this.setEnabled(machineToggleEvent.isEnabled());
                    }


                    //Update item..
                    MenuItemConfiguration updatedItemConfig = this.isEnabled() ? menuConfig.statusItem.enabled : menuConfig.statusItem.disabled;

                    int updatedStatusItemSlot = updatedItemConfig.slot;
                    JItemStack updatedStatusItem = new JItemStack(JMaterial.getRegistryMaterials().find(updatedItemConfig.itemType))
                            .setDisplayName(updatedItemConfig.displayName)
                            .setLore(updatedItemConfig.lore)
                            .setNBTBoolean("JadGens_Status_Item", true);
                    if (updatedItemConfig.glow)
                        updatedStatusItem.addEnchantment(JadGens.getInstance().getGlowEnchantment().asEnchantment(), 69);

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.isEnabled() ? MachinesAPI.getInstance().getGeneralConfiguration().getMessages().machineMessages.toggledOn : MachinesAPI.getInstance().getGeneralConfiguration().getMessages().machineMessages.toggledOff));

                    inventory.setItem(updatedStatusItemSlot, updatedStatusItem);
                } else if (item.getNBTBoolean("JadGens_Fuel_Item")) { //todo: Remove this functionality, it's too buggy.
                    if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {
                        if (MachinesAPI.getInstance().isFuel(e.getView().getCursor())) {
                            LoadedFuelConfiguration fuel = MachinesAPI.getInstance().getFuelConfigurationByItem(e.getView().getCursor());

                            int count = 0;
                            while (this.getFuelAmount() + fuel.getFuelAmount() <= machine.getMachineConfiguration().getMaxFuelAmount() && e.getWhoClicked().getItemInHand().getType() != Material.AIR) {
                                count++;

                                this.setFuelAmount(this.getFuelAmount() + fuel.getFuelAmount());
                                e.getView().getCursor().setAmount(e.getView().getCursor().getAmount() - 1);
                            }
                            e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', MachinesAPI.getInstance().getGeneralConfiguration().getMessages().fuelMessages.usedMultipleFuels.replace("%amount%", String.valueOf(count))));
                        }
                    }
                } else if (item.getNBTBoolean("JadGens_Close_Item")) {
                    if (e.getClick() == (MachinesAPI.getInstance().getGeneralConfiguration().getMessages().machineMenu.closeItem.invertClicks ? ClickType.RIGHT : ClickType.LEFT)) {
                        e.getWhoClicked().closeInventory();
                    } else if (e.getClick() == (MachinesAPI.getInstance().getGeneralConfiguration().getMessages().machineMenu.closeItem.invertClicks ? ClickType.LEFT : ClickType.RIGHT)) {
                        if (e.getWhoClicked().getInventory().addItem(machine.getMachineConfiguration().getMachineItem()).size() == 0)
                            this.remove();

                        player.closeInventory();
                    }
                }
            }
        }, EventPriority.MONITOR, -1, -1, quickEventId1).register(true);

        new JQuickEvent(plugin, InventoryCloseEvent.class, e -> {
            if (e.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                JQuickEvent.getQuickEvent(quickEventId1).register(false);
                JQuickEvent.getQuickEvent(quickEventId2).register(false);
                JQuickEvent.getQuickEvent(quickEventId3).register(false);
            }
        }, EventPriority.MONITOR, -1, -1, quickEventId2).register(true);

        new JQuickEvent(plugin, PlayerQuitEvent.class, e -> {
            if (e.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                JQuickEvent.getQuickEvent(quickEventId1).register(false);
                JQuickEvent.getQuickEvent(quickEventId2).register(false);
                JQuickEvent.getQuickEvent(quickEventId3).register(false);
            }
        }, EventPriority.MONITOR, -1, -1, quickEventId3).register(true);
    }

    @Override
    public int getTicksToProduce() {
        return ticksToGenerate;
    }

    @Override
    public void setTicksToProduce(int ticks) {
        this.ticksToGenerate = ticks;
    }

    //explanation: prevent a machine from ticking for the first 5 ticks it has been placed to prevent a lag spike and auto-invalidation of the machine
    public int dontCareTicks = 5;

    private boolean warnedAboutInvalid = false;

    @Override
    public void tick() {
        if (dontCareTicks > 0) {
            dontCareTicks--;
            return;
        }

        if (this.machine.getOwner() != null && player == null)
            player = Bukkit.getOfflinePlayer(this.machine.getOwner());

        if (this.machine.getOwner() == null) {
            this.machine.getInstance().setEnabled(false);
            return; //Prevent some sneaky exceptions..
        }

        if (!this.machine.isLoaded())
            if (MachinesAPI.getInstance().getGeneralConfiguration().stopProducingInUnloadedChunks())
                return;
            else
                machine.getLocation().getWorld().loadChunk(machine.getLocation().getChunk());

        if (!this.machine.isValid()) {
            if (MachinesAPI.getInstance().getGeneralConfiguration().removeInvalidMachines()) {
                remove();
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eDetected an &3invalid machine&e. ID -> " + this.machine.getId()));
                return;
            } else {
                if (!warnedAboutInvalid) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadGens &7>> &eDetected an &3invalid machine&e. ID -> " + this.machine.getId()));
                    warnedAboutInvalid = true;
                }
            }
        }

        //Update hologram
        tickHologram();

        if (canProduce()) {
            if (this.ticksToGenerate <= 0) {
                produce(false);
                this.ticksToGenerate = this.machine.getMachineConfiguration().getTicksDelay();
                return;
            }
            this.ticksToGenerate--;
        }
    }

    private int hologramTickDelay = 40;

    @Override
    public void tickHologram() {
        LoadedHologramConfiguration hologramConfiguration = this.machine.getMachineConfiguration().getHologramConfiguration();
        if (hologram == null) {
            Location location = this.machine.getLocation().clone().add(0.5, 0, 0.5);
            hologram = new JHologram(location, true, hologramConfiguration.parseHologramLines(machine));
        }

        if(hologramTickDelay <= 0) {
            hologramTickDelay = 40;
            hologram.edit(hologramConfiguration.parseHologramLines(machine));
            return;
        }
        hologramTickDelay--;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!this.enabled)
            this.ticksToGenerate = machine.getMachineConfiguration().getTicksDelay();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public int getFuelAmount() {
        return fuelAmount;
    }

    @Override
    public void setFuelAmount(int fuelAmount) {
        this.fuelAmount = fuelAmount;
    }

    @Override
    public void addFuelAmount(int fuelAmount) {
        this.fuelAmount += fuelAmount;
    }

    @Override
    public boolean canProduce() {
        return this.enabled && this.fuelAmount > 0 && (MachinesAPI.getInstance().getGeneralConfiguration().produceEvenIfOffline() || player.isOnline());
    }

    @Override
    public void produce(boolean forcefully) {
        if (!canProduce() && !forcefully)
            return;

        if (world == null)
            world = new JWorld(machine.getLocation().getWorld());

        MachineProduceEvent machineProduceEvent = new MachineProduceEvent(this, forcefully);
        Bukkit.getPluginManager().callEvent(machineProduceEvent);
        if (machineProduceEvent.isCancelled()) {
            if (!forcefully)
                return;
            //forcefully cannot be cancelled.
        }

        LoadedParticleConfiguration particleConfig = machine.getMachineConfiguration().getParticleConfiguration();
        if (particleConfig.isParticlesEnabled() && particleConfig.showOnProduce()) {
            JParticle particle = machine.getMachineConfiguration().getParticleConfiguration().getParticle();
            Location location = machine.getLocation();

            for (int i = 0; i < particleConfig.getParticleRows(); i++) {
                for (int j = 0; j < particleConfig.getParticleCount(); j++) {
                    world.spawnParticle(location, particle, particleConfig.getParticleSpeed(), particleConfig.getParticleCount());
                }
            }
            location.add(0, 0.01, 0);
        }

        if (!forcefully) {
            this.fuelAmount--;
        }

        tickHologram();

        LoadedMachineProductionConfiguration procConfig = this.machine.getMachineConfiguration().getProductionConfiguration();

        if (procConfig.producesEconomy() && player != null) {
            Hook hook = JadGens.getInstance().getHookByName("vault");
            if (hook.isAvailable())
                ((Economy) hook.getHook()).depositPlayer(player, procConfig.getEconomyAmount());
        }

        if (procConfig.producesPoints() && player != null) {
            Hook hook = JadGens.getInstance().getHookByName("playerpoints");
            if (hook.isAvailable())
                ((PlayerPointsAPI) hook.getHook()).give(player.getUniqueId(), procConfig.getPointsAmount());
        }

        if (procConfig.producesExperience() && player != null) {
            if (player.isOnline()) {
                player.getPlayer().setLevel(procConfig.getExperienceAmount() + player.getPlayer().getLevel());
            } else {
                MachinesUser user = MachinesAPI.getInstance().getPlayer(player.getUniqueId());
                user.setXpToCollect(procConfig.getExperienceAmount() + user.getXpToCollect());
            }
        }

        if (procConfig.producesItem()) {
            if (!procConfig.sendItemToMenu())
                procConfig.getProduceItem().drop(this.machine.getLocation().add(0, 0, 0)).setDisplayName(procConfig.getProduceItem().buildItemStack().getItemMeta().getDisplayName());
            else {
                MachinesAPI.getInstance().getPlayer(machine.getOwner()).getDropInformation(machine.getMachineConfiguration()).addAmount(procConfig.getProduceItem().buildItemStack().getAmount());
            }
        }
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void remove() {
        if (hologram != null)
            hologram.delete();

        Location location = this.machine.getLocation();
        if (location != null && location.getWorld() != null) {
            Block block = location.getWorld().getBlockAt(location);
            block.setType(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.AIR).getMaterial(JMaterial.Type.BLOCK).getKey());
        }

        isValid = false;

        ((MachinesManager) MachinesAPI.getInstance()).removeMachine(this);
    }
}
