package net.minetaria.replaysystem.recording.dispatcher;

import net.minetaria.replaysystem.ReplaySystem;
import net.minetaria.replaysystem.recording.Recording;
import net.minetaria.replaysystem.recording.events.RecordingStopEvent;
import net.minetaria.replaysystem.recording.recordable.entity.RecordableEntity;
import net.minetaria.replaysystem.recording.recordable.recordables.InventoryChangeRecordable;
import net.minetaria.replaysystem.recording.recordable.recordables.LocationChangeRecordable;
import net.minetaria.replaysystem.recording.recordable.entity.SerializableEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

public class TickDispatcher extends BukkitRunnable implements Listener {

    private Recording recording;

    public TickDispatcher(Recording recording) {
        this.recording = recording;
        Bukkit.getPluginManager().registerEvents(this,ReplaySystem.getInstance());
        runTaskTimer(ReplaySystem.getInstance(),0,1L);
        System.out.println("[TICK DISPATCHER STARTED] (" + recording.getId() + ")");
    }

    @EventHandler
    public void onRecordingStop(RecordingStopEvent event) {
        if (event.getRecording().equals(recording)) {
            cancel();
            HandlerList.unregisterAll(this);
            System.out.println("[TICK DISPATCHER STOPPED & UNREGISTERED] (" + recording.getId() + ")");
        }
    }

    public void run() {
        if (!recording.isCancelled()) {
            entityLocationCheck();
            playerInventoryCheck();
        }
    }

    private void entityLocationCheck() {
        for (RecordableEntity recordableEntity:recording.getRecordableEntities()) {
            if (Bukkit.getEntity(recordableEntity.getSerializableEntity().getBukkitEntityId()) != null) {
                new LocationChangeRecordable(recording,recordableEntity.getSerializableEntity(), Bukkit.getEntity(recordableEntity.getSerializableEntity().getBukkitEntityId()).getLocation());
            }
        }
    }

    private final HashMap<UUID, ArrayList<ItemStack>> savedInventories = new HashMap<>();
    private void playerInventoryCheck() {
        for (RecordableEntity recordableEntity:recording.getRecordableEntities()) {
            if (Bukkit.getEntity(recordableEntity.getSerializableEntity().getBukkitEntityId()) != null) {
                if (recordableEntity.getSerializableEntity().getEntityType() == EntityType.PLAYER) {
                    Player player = (Player) Bukkit.getEntity(recordableEntity.getSerializableEntity().getBukkitEntityId());
                    ArrayList<ItemStack> contents = new ArrayList<>();
                    contents.addAll(Arrays.asList(player.getInventory().getContents()));
                    contents.addAll(Arrays.asList(player.getInventory().getArmorContents()));
                    ArrayList<ItemStack> loaded = savedInventories.getOrDefault(player.getUniqueId(), new ArrayList<>());
                    if (contents != loaded) {
                        savedInventories.put(player.getUniqueId(),contents);
                        new InventoryChangeRecordable(recording,recordableEntity.getSerializableEntity(),player.getInventory().getContents(), player.getInventory().getArmorContents());
                    }
                }
            }
        }
    }
}
