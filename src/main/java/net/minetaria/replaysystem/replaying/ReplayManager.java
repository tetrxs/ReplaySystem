package net.minetaria.replaysystem.replaying;

import net.minetaria.replaysystem.ReplaySystem;
import net.minetaria.replaysystem.recording.Recording;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.io.BukkitObjectInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class ReplayManager {

    public ReplayManager() {

    }

    public void startReplay(Player viewer, String id) throws IOException {
        String loadString = null;
        try {
            ResultSet rs = ReplaySystem.getInstance().getSqlUtil().executeQuery("SELECT `replay` FROM `replaySystem_replays` WHERE `id` = '" + id + "';");
            if (rs.next()) {
                loadString = rs.getString("replay");
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (loadString != null) {
            byte[] loaded = Base64.getDecoder().decode(loadString);
            byte[] temp = ReplaySystem.decompressByteArray(loaded);
            try (ByteArrayInputStream bais = new ByteArrayInputStream(temp); BukkitObjectInputStream ois = new BukkitObjectInputStream(bais)) {
                Object object = ois.readObject();
                Bukkit.getScheduler().runTask(ReplaySystem.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        if (object instanceof Recording) {
                            Recording loadedRecording = (Recording) object;
                            if (loadedRecording.getId().equals(id)) {
                                new Replay(viewer, loadedRecording);
                            }
                        }
                    }
                });
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
