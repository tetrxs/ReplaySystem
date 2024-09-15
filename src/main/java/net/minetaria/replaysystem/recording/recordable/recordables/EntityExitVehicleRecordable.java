package net.minetaria.replaysystem.recording.recordable.recordables;

import com.github.retrooper.packetevents.protocol.player.User;
import net.citizensnpcs.api.npc.NPC;
import net.minetaria.replaysystem.recording.Recording;
import net.minetaria.replaysystem.recording.recordable.Recordable;
import net.minetaria.replaysystem.recording.recordable.entity.SerializableEntity;
import net.minetaria.replaysystem.replaying.Replay;
import org.bukkit.entity.LivingEntity;

public class EntityExitVehicleRecordable extends Recordable {

    private SerializableEntity serializableEntity;

    public EntityExitVehicleRecordable(Recording recording, SerializableEntity serializableEntity) {
        super(recording);
        this.serializableEntity = serializableEntity;
    }

    @Override
    public void replay(Replay replay, User user) {
        NPC npc = replay.registerNpc(serializableEntity);
        if (npc.isSpawned() && npc.getEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) npc.getEntity();
            livingEntity.leaveVehicle();
        }
    }
}
