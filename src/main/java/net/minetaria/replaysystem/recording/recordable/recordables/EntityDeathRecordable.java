package net.minetaria.replaysystem.recording.recordable.recordables;

import com.github.retrooper.packetevents.protocol.player.User;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.util.PlayerAnimation;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.world.entity.Entity;
import net.minetaria.replaysystem.recording.Recording;
import net.minetaria.replaysystem.recording.recordable.Recordable;
import net.minetaria.replaysystem.recording.recordable.entity.SerializableEntity;
import net.minetaria.replaysystem.replaying.Replay;
import org.bukkit.EntityEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class EntityDeathRecordable extends Recordable {

    private SerializableEntity serializableEntity;

    public EntityDeathRecordable(Recording recording, SerializableEntity serializableEntity) {
        super(recording);
        this.serializableEntity = serializableEntity;
    }
    @Override
    public void replay(Replay replay, User user) throws Exception {
        NPC npc = replay.registerNpc(serializableEntity);
        if (npc.isSpawned() && npc.getEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) npc.getEntity();
            livingEntity.playEffect(EntityEffect.DEATH);
            livingEntity.getWorld().playSound(npc.getEntity().getLocation(), livingEntity.getDeathSound(), 1.0f, 1.0f);
        }
    }
}
