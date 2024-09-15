package net.minetaria.replaysystem.recording.recordable.recordables;

import com.github.retrooper.packetevents.protocol.player.User;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.minecraft.world.InteractionHand;
import net.minetaria.replaysystem.recording.Recording;
import net.minetaria.replaysystem.recording.recordable.Recordable;
import net.minetaria.replaysystem.recording.recordable.entity.SerializableEntity;
import net.minetaria.replaysystem.replaying.Replay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class PlayerSwitchItemRecordable extends Recordable {

    private SerializableEntity serializableEntity;
    private ItemStack itemStack;

    public PlayerSwitchItemRecordable(Recording recording, SerializableEntity serializableEntity, ItemStack itemStack) {
        super(recording);
        this.serializableEntity = serializableEntity;
        this.itemStack = itemStack;
    }

    @Override
    public void replay(Replay replay, User user) {
        NPC npc = replay.registerNpc(serializableEntity);
        if (npc.isSpawned()) {
            Equipment equipment = npc.getOrAddTrait(Equipment.class);
            equipment.set(Equipment.EquipmentSlot.HAND,itemStack);
            npc.addTrait(equipment);
        }
    }
}
