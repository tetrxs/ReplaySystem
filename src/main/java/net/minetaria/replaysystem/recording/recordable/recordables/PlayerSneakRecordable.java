package net.minetaria.replaysystem.recording.recordable.recordables;

import com.github.retrooper.packetevents.protocol.player.User;
import net.citizensnpcs.api.npc.NPC;
import net.minetaria.replaysystem.recording.Recording;
import net.minetaria.replaysystem.recording.recordable.Recordable;
import net.minetaria.replaysystem.recording.recordable.entity.SerializableEntity;
import net.minetaria.replaysystem.replaying.Replay;

public class PlayerSneakRecordable extends Recordable {

    private SerializableEntity serializableEntity;
    private boolean sneak;

    public PlayerSneakRecordable(Recording recording, SerializableEntity serializableEntity, boolean sneak) {
        super(recording);
        this.serializableEntity = serializableEntity;
        this.sneak = sneak;
    }

    @Override
    public void replay(Replay replay, User user) {
        NPC npc = replay.registerNpc(serializableEntity);
        if (npc.isSpawned()) {
            npc.setSneaking(sneak);
        }
    }
}
