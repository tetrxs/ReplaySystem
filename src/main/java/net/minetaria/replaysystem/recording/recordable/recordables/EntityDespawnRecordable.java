package net.minetaria.replaysystem.recording.recordable.recordables;

import com.github.retrooper.packetevents.protocol.player.User;
import net.citizensnpcs.api.npc.NPC;
import net.minetaria.replaysystem.recording.Recording;
import net.minetaria.replaysystem.recording.recordable.Recordable;
import net.minetaria.replaysystem.recording.recordable.entity.SerializableEntity;
import net.minetaria.replaysystem.recording.recordable.locaiton.SerializableLocation;
import net.minetaria.replaysystem.replaying.Replay;

public class EntityDespawnRecordable extends Recordable {

    private SerializableEntity serializableEntity;

    public EntityDespawnRecordable(Recording recording, SerializableEntity serializableEntity) {
        super(recording);
        this.serializableEntity = serializableEntity;
    }

    @Override
    public void replay(Replay replay, User user) throws Exception {
        NPC npc = replay.registerNpc(serializableEntity);
        if (npc.isSpawned()) {
            npc.despawn();
        }
    }
}
