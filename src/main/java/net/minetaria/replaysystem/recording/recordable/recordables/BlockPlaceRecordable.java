package net.minetaria.replaysystem.recording.recordable.recordables;

import com.github.retrooper.packetevents.protocol.player.User;
import net.citizensnpcs.api.npc.NPC;
import net.minetaria.replaysystem.recording.Recording;
import net.minetaria.replaysystem.recording.recordable.Recordable;
import net.minetaria.replaysystem.recording.recordable.entity.SerializableEntity;
import net.minetaria.replaysystem.recording.recordable.locaiton.SerializableLocation;
import net.minetaria.replaysystem.replaying.Replay;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.LivingEntity;

public class BlockPlaceRecordable extends Recordable {

    private Material material;
    private SerializableLocation serializableLocation;

    public BlockPlaceRecordable(Recording recording, Material material, Location location) {
        super(recording);
        this.material = material;
        this.serializableLocation = new SerializableLocation(location);
    }

    @Override
    public void replay(Replay replay, User user) throws Exception {
        serializableLocation.refreshWorldName(replay.getReplayWorld().getName());
        serializableLocation.getLocation().getBlock().setType(material);
        serializableLocation.getLocation().getWorld().playSound(serializableLocation.getLocation(), serializableLocation.getLocation().getBlock().getBlockData().getSoundGroup().getPlaceSound(), 1.0f, 1.0f);
    }
}
