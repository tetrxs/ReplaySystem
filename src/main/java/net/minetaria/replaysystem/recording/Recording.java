package net.minetaria.replaysystem.recording;

import net.minetaria.replaysystem.ReplaySystem;
import net.minetaria.replaysystem.recording.events.RecordingStartEvent;
import net.minetaria.replaysystem.recording.events.RecordingStopEvent;
import net.minetaria.replaysystem.recording.recordable.Recordable;
import net.minetaria.replaysystem.recording.recordable.entity.RecordableEntity;
import net.minetaria.replaysystem.recording.recordable.recordables.EntityDespawnRecordable;
import net.minetaria.replaysystem.recording.recordable.recordables.EntitySpawnRecordable;
import net.minetaria.replaysystem.recording.recordable.entity.SerializableEntity;
import net.minetaria.replaysystem.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Recording extends BukkitRunnable implements Serializable {

    public Recording instance;

    private String id;
    private UUID creator;

    private String worldName;
    private String replayWorldPath;

    private ItemStack replayItem;

    private HashMap<Long, ArrayList<Recordable>> recordables;
    private ArrayList<RecordableEntity> recordableEntities;

    private long recordingTick = 0;

    public Recording(UUID creator, String worldName) {
        instance = this;

        this.id = ReplaySystem.generateID();
        this.creator = creator;
        this.worldName = worldName;
        this.replayWorldPath = Bukkit.getWorld(worldName).getWorldFolder().getAbsolutePath();
        this.recordables = new HashMap<>();
        this.recordableEntities = new ArrayList<>();
        this.replayItem = new ItemUtil(Material.ENDER_EYE).setName("§5§lReplay").setLore(new String[]{"§1","§7§lID","§8└ §a" + id,"§2"}).build();
    }

    public void start() {
        if (replayWorldPath != null) {
            for (Entity entity:Bukkit.getWorld(worldName).getEntities()) {
                registerRecordableEntityIfNotRegistered(new SerializableEntity(entity.getUniqueId(),entity.getType(),entity.getName()));
            }
            runTaskTimer(ReplaySystem.getInstance(),0,1L);
            Bukkit.getPluginManager().callEvent(new RecordingStartEvent(instance));
            System.out.println("[RECORDING STARTED] (" + id + ")");
        } else {
            ReplaySystem.getInstance().getRecordingManager().getActiveRecordings().remove(instance);
            stop();
        }
    }

    public void manipulateReplayWorldPath(String replayWorldPath) {
        this.replayWorldPath = replayWorldPath;
    }

    @Override
    public void run() {
        recordingTick++;
    }

    public void stop() {
        cancel();
        Bukkit.getPluginManager().callEvent(new RecordingStopEvent(instance));
        System.out.println("[RECORDING STOPPED] (" + id + ")");
    }

    public RecordableEntity isRecordableEntityRegistered(SerializableEntity serializableEntity) {
        for (RecordableEntity recordableEntity:recordableEntities) {
            if (recordableEntity.getSerializableEntity().getBukkitEntityId().equals(serializableEntity.getBukkitEntityId())) {
                return recordableEntity;
            }
        }
        return null;
    }
    public void registerRecordableEntityIfNotRegistered(SerializableEntity serializableEntity) {
        System.out.println("ENTITY SPAWNED 3");
        if (Bukkit.getEntity(serializableEntity.getBukkitEntityId()) != null && isRecordableEntityRegistered(serializableEntity) == null) {
            System.out.println("ENTITY SPAWNED 4");
            recordableEntities.add(new RecordableEntity(serializableEntity));
            new EntitySpawnRecordable(instance,serializableEntity,Bukkit.getEntity(serializableEntity.getBukkitEntityId()).getLocation());
        }
    }
    public void unregisterRecordableEntityIfRegistered(SerializableEntity serializableEntity) {
        RecordableEntity recordableEntity = isRecordableEntityRegistered(serializableEntity);
        if (recordableEntity != null) {
            recordableEntities.remove(recordableEntity);
            new EntityDespawnRecordable(instance,serializableEntity);
        }
    }

    public void record(Recordable recordable) {
        ArrayList<Recordable> temp = recordables.getOrDefault(recordingTick,new ArrayList<>());
        temp.add(recordable);
        recordables.put(recordingTick,temp);
    }

    public Recording getInstance() {
        return instance;
    }

    public String getId() {
        return id;
    }

    public UUID getCreator() {
        return creator;
    }

    public String getWorldName() {
        return worldName;
    }

    public String getReplayWorldPath() {
        return replayWorldPath;
    }

    public HashMap<Long, ArrayList<Recordable>> getRecordables() {
        return recordables;
    }

    public ArrayList<RecordableEntity> getRecordableEntities() {
        return recordableEntities;
    }

    public ItemStack getReplayItem() {
        return replayItem;
    }
}
