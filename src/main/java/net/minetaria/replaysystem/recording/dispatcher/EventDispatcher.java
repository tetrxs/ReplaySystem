package net.minetaria.replaysystem.recording.dispatcher;

import net.citizensnpcs.api.trait.trait.Inventory;
import net.minecraft.world.entity.LivingEntity;
import net.minetaria.replaysystem.ReplaySystem;
import net.minetaria.replaysystem.recording.Recording;
import net.minetaria.replaysystem.recording.events.RecordingStopEvent;
import net.minetaria.replaysystem.recording.recordable.entity.RecordableEntity;
import net.minetaria.replaysystem.recording.recordable.recordables.*;
import net.minetaria.replaysystem.recording.recordable.entity.SerializableEntity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

import java.util.HashMap;
import java.util.UUID;

public class EventDispatcher implements Listener {

    private Recording recording;

    public EventDispatcher(Recording recording) {
        this.recording = recording;
        Bukkit.getPluginManager().registerEvents(this, ReplaySystem.getInstance());
        System.out.println("[EVENT DISPATCHER REGISTERED] (" + recording.getId() + ")");
    }

    @EventHandler
    public void onRecordingStop(RecordingStopEvent event) {
        if (event.getRecording().equals(recording)) {
            HandlerList.unregisterAll(this);
            System.out.println("[EVENT DISPATCHER UNREGISTERED] (" + recording.getId() + ")");
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        System.out.println("ENTITY SPAWNED 1");
        if (event.getEntity().getWorld().getName().equals(recording.getWorldName())) {
            System.out.println("ENTITY SPAWNED 2");
            recording.registerRecordableEntityIfNotRegistered(new SerializableEntity(event.getEntity().getUniqueId(),event.getEntityType(),event.getEntity().getName()));
        }
    }

    @EventHandler
    public void onEntityRemove(EntityRemoveEvent event) {
        if (event.getEntity().getWorld().getName().equals(recording.getWorldName())) {
            recording.unregisterRecordableEntityIfRegistered(new SerializableEntity(event.getEntity().getUniqueId(),event.getEntityType(),event.getEntity().getName()));
        }
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Entity entity = (Entity) event.getPlayer();
        RecordableEntity recordableEntity = recording.isRecordableEntityRegistered(new SerializableEntity(entity.getUniqueId(), EntityType.PLAYER, entity.getName()));
        if (recordableEntity != null) {
            new PlayerSneakRecordable(recording, recordableEntity.getSerializableEntity(), event.isSneaking());
        }
    }

    @EventHandler
    public void onPlayerArmSwing(PlayerAnimationEvent event) {
        Entity entity = (Entity) event.getPlayer();
        RecordableEntity recordableEntity = recording.isRecordableEntityRegistered(new SerializableEntity(entity.getUniqueId(), EntityType.PLAYER, entity.getName()));
        if (recordableEntity != null) {
            new PlayerAnimationRecordable(recording, recordableEntity.getSerializableEntity(), event.getAnimationType());
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        RecordableEntity recordableEntity = recording.isRecordableEntityRegistered(new SerializableEntity(entity.getUniqueId(), EntityType.PLAYER, entity.getName()));
        if (recordableEntity != null) {
            new EntityDamageRecordable(recording, recordableEntity.getSerializableEntity());
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = (Entity) event.getEntity();
        RecordableEntity recordableEntity = recording.isRecordableEntityRegistered(new SerializableEntity(entity.getUniqueId(), EntityType.PLAYER, entity.getName()));
        if (recordableEntity != null) {
            new EntityDeathRecordable(recording, recordableEntity.getSerializableEntity());
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Entity entity = (Entity) event.getPlayer();
        RecordableEntity recordableEntity = recording.isRecordableEntityRegistered(new SerializableEntity(entity.getUniqueId(), EntityType.PLAYER, entity.getName()));
        if (recordableEntity != null) {
            new PlayerRespawnRecordable(recording, recordableEntity.getSerializableEntity(), entity.getLocation());
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        new BlockPlaceRecordable(recording, event.getBlock().getType(), event.getBlock().getLocation());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        new BlockBreakRecordable(recording, event.getBlock().getLocation());
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        Entity entity = (Entity) event.getExited();
        RecordableEntity recordableEntity = recording.isRecordableEntityRegistered(new SerializableEntity(entity.getUniqueId(), EntityType.PLAYER, entity.getName()));
        if (recordableEntity != null) {
            new EntityExitVehicleRecordable(recording, recordableEntity.getSerializableEntity());
        }
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        Entity entity = (Entity) event.getEntered();
        RecordableEntity recordableEntity = recording.isRecordableEntityRegistered(new SerializableEntity(entity.getUniqueId(), EntityType.PLAYER, entity.getName()));
        RecordableEntity recordableEntityVehicle = recording.isRecordableEntityRegistered(new SerializableEntity(event.getVehicle().getUniqueId(), event.getVehicle().getType(), event.getVehicle().getName()));
        if (recordableEntity != null && recordableEntityVehicle != null) {
            new EntityEnterVehicleRecordable(recording, recordableEntity.getSerializableEntity(), recordableEntityVehicle.getSerializableEntity());
        }
    }

    @EventHandler
    public void onPlayerSwitchItem(PlayerItemHeldEvent event) {
        Entity entity = (Entity) event.getPlayer();
        RecordableEntity recordableEntity = recording.isRecordableEntityRegistered(new SerializableEntity(entity.getUniqueId(), EntityType.PLAYER, entity.getName()));
        if (recordableEntity != null) {
            new PlayerSwitchItemRecordable(recording, recordableEntity.getSerializableEntity(), event.getPlayer().getInventory().getItem(event.getNewSlot()));
        }
    }

    private boolean isEntityInWorld(UUID bukkitEntityId) {
        if (Bukkit.getEntity(bukkitEntityId) != null) {
            return Bukkit.getEntity(bukkitEntityId).getWorld().getName().equals(recording.getWorldName());
        }
        return false;
    }

}
