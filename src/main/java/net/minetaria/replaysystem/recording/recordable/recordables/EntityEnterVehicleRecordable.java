package net.minetaria.replaysystem.recording.recordable.recordables;

import com.github.retrooper.packetevents.protocol.player.User;
import net.citizensnpcs.api.npc.NPC;
import net.minetaria.replaysystem.recording.Recording;
import net.minetaria.replaysystem.recording.recordable.Recordable;
import net.minetaria.replaysystem.recording.recordable.entity.SerializableEntity;
import net.minetaria.replaysystem.replaying.Replay;
import org.bukkit.entity.LivingEntity;

public class EntityEnterVehicleRecordable extends Recordable {

    private SerializableEntity serializableEntity;
    private SerializableEntity vehicle;

    public EntityEnterVehicleRecordable(Recording recording, SerializableEntity serializableEntity, SerializableEntity vehicle) {
        super(recording);
        this.serializableEntity = serializableEntity;
        this.vehicle = vehicle;
    }

    @Override
    public void replay(Replay replay, User user) {
        NPC npc = replay.registerNpc(serializableEntity);
        NPC vehicleNpc = replay.registerNpc(vehicle);
        if (npc.isSpawned() && vehicleNpc.isSpawned() && npc.getEntity() instanceof LivingEntity && vehicleNpc.getEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) npc.getEntity();
            LivingEntity livingEntityVehicle = (LivingEntity) vehicleNpc.getEntity();
            livingEntityVehicle.addPassenger(livingEntity);
        }
    }
}
