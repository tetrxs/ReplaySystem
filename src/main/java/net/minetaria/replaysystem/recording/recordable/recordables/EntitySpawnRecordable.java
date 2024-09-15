package net.minetaria.replaysystem.recording.recordable.recordables;

import com.github.retrooper.packetevents.protocol.player.User;
import net.citizensnpcs.api.npc.NPC;
import net.minetaria.replaysystem.recording.Recording;
import net.minetaria.replaysystem.recording.recordable.Recordable;
import net.minetaria.replaysystem.recording.recordable.entity.SerializableEntity;
import net.minetaria.replaysystem.recording.recordable.locaiton.SerializableLocation;
import net.minetaria.replaysystem.replaying.Replay;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class EntitySpawnRecordable extends Recordable {

    private SerializableEntity serializableEntity;
    private SerializableLocation serializableLocation;

    public EntitySpawnRecordable(Recording recording, SerializableEntity serializableEntity, Location location) {
        super(recording);
        System.out.println("ENTITY SPAWNED 5");
        this.serializableEntity = serializableEntity;
        this.serializableLocation = new SerializableLocation(location);
    }

    @Override
    public void replay(Replay replay, User user) throws Exception {
        NPC npc = replay.registerNpc(serializableEntity);
        if (!npc.isSpawned()) {
            serializableLocation.refreshWorldName(replay.getReplayWorld().getName());
            npc.spawn(serializableLocation.getLocation());
        }
    }
}
