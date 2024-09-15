package net.minetaria.replaysystem.recording.recordable.recordables;

import com.github.retrooper.packetevents.protocol.player.User;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.util.PlayerAnimation;
import net.minetaria.replaysystem.recording.Recording;
import net.minetaria.replaysystem.recording.recordable.Recordable;
import net.minetaria.replaysystem.recording.recordable.entity.SerializableEntity;
import net.minetaria.replaysystem.replaying.Replay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;

public class PlayerAnimationRecordable extends Recordable {

    private SerializableEntity serializableEntity;
    private PlayerAnimationType playerAnimationType;

    public PlayerAnimationRecordable(Recording recording, SerializableEntity serializableEntity, PlayerAnimationType playerAnimationType) {
        super(recording);
        this.serializableEntity = serializableEntity;
        this.playerAnimationType = playerAnimationType;
    }

    @Override
    public void replay(Replay replay, User user) {
        NPC npc = replay.registerNpc(serializableEntity);
        if (npc.isSpawned() && npc.getEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) npc.getEntity();
            if (playerAnimationType.equals(PlayerAnimationType.ARM_SWING)) {
                livingEntity.swingMainHand();
            } else if (playerAnimationType.equals(PlayerAnimationType.OFF_ARM_SWING)) {
                livingEntity.swingOffHand();
            }
        }
    }
}
