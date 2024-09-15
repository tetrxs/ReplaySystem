package net.minetaria.replaysystem.recording;

import net.minetaria.replaysystem.ReplaySystem;
import net.minetaria.replaysystem.recording.dispatcher.EventDispatcher;
import net.minetaria.replaysystem.recording.dispatcher.TickDispatcher;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.checkerframework.checker.units.qual.A;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

public class RecordingManager implements Listener {

    private ArrayList<Recording> activeRecordings;

    public RecordingManager() {
        activeRecordings = new ArrayList<>();
        Bukkit.getPluginManager().registerEvents(this, ReplaySystem.getInstance());
    }

    public Recording startRecording(UUID creator, String worldName) {
        Recording recording = new Recording(creator, worldName);
        new TickDispatcher(recording);
        new EventDispatcher(recording);
        recording.start();
        activeRecordings.add(recording);
        return recording;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ArrayList<Recording> temp = new ArrayList<>(activeRecordings);
        for (Recording recording:temp) {
            if (recording.getCreator().equals(event.getPlayer().getUniqueId())) {
                recording.stop();
            }
        }
    }

    public void stopRecording(String id) {
        ArrayList<Recording> temp = new ArrayList<>(activeRecordings);
        for (Recording recording:temp) {
            if (recording.getId().equals(id)) {
                recording.stop();
                activeRecordings.remove(recording);
                Bukkit.getScheduler().runTaskAsynchronously(ReplaySystem.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); BukkitObjectOutputStream oos = new BukkitObjectOutputStream(baos)) {
                            oos.writeObject(recording);
                            oos.flush();
                            byte[] objectBytes = baos.toByteArray();
                            Bukkit.getScheduler().runTask(ReplaySystem.getInstance(), new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        byte[] compressed = ReplaySystem.compressByteArray(objectBytes);
                                        String saveString = Base64.getEncoder().encodeToString(compressed);
                                        try {
                                            ReplaySystem.getInstance().getSqlUtil().executeUpdate("INSERT INTO `replaySystem_replays` (`uuid`, `id`, `replay`) VALUES ('" + recording.getCreator() + "', '" + recording.getId() + "', '" + saveString + "')");
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

            }
        }
    }

    public ArrayList<Recording> getActiveRecordings() {
        return activeRecordings;
    }

}
