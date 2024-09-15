package net.minetaria.replaysystem.recording.recordable.recordables;

import com.github.retrooper.packetevents.protocol.player.User;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Inventory;
import net.minetaria.replaysystem.recording.Recording;
import net.minetaria.replaysystem.recording.recordable.Recordable;
import net.minetaria.replaysystem.recording.recordable.entity.SerializableEntity;
import net.minetaria.replaysystem.recording.recordable.locaiton.SerializableLocation;
import net.minetaria.replaysystem.replaying.Replay;
import net.minetaria.replaysystem.utils.EquipmentUtil;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryChangeRecordable extends Recordable {

    private SerializableEntity serializableEntity;
    private ItemStack[] contents;
    private ItemStack[] armorContents;

    public InventoryChangeRecordable(Recording recording, SerializableEntity serializableEntity, ItemStack[] contents, ItemStack[] armorContents) {
        super(recording);
        this.serializableEntity = serializableEntity;
        this.contents = contents;
        this.armorContents = armorContents;
    }

    @Override
    public void replay(Replay replay, User user) {
        NPC npc = replay.registerNpc(serializableEntity);
        if (npc.isSpawned()) {
            Equipment equipment = npc.getOrAddTrait(Equipment.class);
            equipment.set(Equipment.EquipmentSlot.HELMET, EquipmentUtil.getAnyHelmetFromItemStackArray(armorContents));
            equipment.set(Equipment.EquipmentSlot.CHESTPLATE, EquipmentUtil.getAnyChestplateFromItemStackArray(armorContents));
            equipment.set(Equipment.EquipmentSlot.LEGGINGS, EquipmentUtil.getAnyLeggingsFromItemStackArray(armorContents));
            equipment.set(Equipment.EquipmentSlot.BOOTS, EquipmentUtil.getAnyBootsFromItemStackArray(armorContents));
            npc.addTrait(equipment);
            Inventory inventory = npc.getOrAddTrait(Inventory.class);
            inventory.setContents(contents);
            npc.addTrait(inventory);
        }
    }

}
