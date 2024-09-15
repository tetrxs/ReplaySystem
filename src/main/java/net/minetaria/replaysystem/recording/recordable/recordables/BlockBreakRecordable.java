package net.minetaria.replaysystem.recording.recordable.recordables;

import com.github.retrooper.packetevents.protocol.player.User;
import net.minetaria.replaysystem.recording.Recording;
import net.minetaria.replaysystem.recording.recordable.Recordable;
import net.minetaria.replaysystem.recording.recordable.locaiton.SerializableLocation;
import net.minetaria.replaysystem.replaying.Replay;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;

public class BlockBreakRecordable extends Recordable {

    private SerializableLocation serializableLocation;

    public BlockBreakRecordable(Recording recording, Location location) {
        super(recording);
        this.serializableLocation = new SerializableLocation(location);
    }

    @Override
    public void replay(Replay replay, User user) throws Exception {
        serializableLocation.refreshWorldName(replay.getReplayWorld().getName());
        serializableLocation.getWorld().playEffect(serializableLocation.getLocation(), Effect.STEP_SOUND, serializableLocation.getLocation().getBlock().getBlockData());
        serializableLocation.getWorld().playSound(serializableLocation.getLocation(), serializableLocation.getLocation().getBlock().getBlockData().getSoundGroup().getBreakSound(), 1.0f, 1.0f);
        serializableLocation.getLocation().getBlock().setType(Material.AIR);
    }
}
