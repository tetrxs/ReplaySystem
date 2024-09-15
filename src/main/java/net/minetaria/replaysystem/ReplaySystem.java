package net.minetaria.replaysystem;

import net.minetaria.replaysystem.commands.TestCommand;
import net.minetaria.replaysystem.listener.PlayerDeathListener;
import net.minetaria.replaysystem.recording.RecordingManager;
import net.minetaria.replaysystem.replaying.ReplayManager;
import net.minetaria.replaysystem.utils.SQLUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class ReplaySystem extends JavaPlugin {

    public static ReplaySystem instance;

    private SQLUtil sqlUtil;

    public final static String prefix = "§5§lReplay §8• §7";

    private RecordingManager recordingManager;
    private ReplayManager replayManager;

    @Override
    public void onEnable() {
        instance = this;

        sqlUtil = new SQLUtil("jdbc:mysql://116.202.235.165:3306/ReplaySystem", "minetaria", "2S3jYDYC9rfLi4P7");
        try {
            sqlUtil.executeUpdate("CREATE TABLE IF NOT EXISTS `replaySystem_replays` (`uuid` VARCHAR(100) NOT NULL , `id` VARCHAR(100) NOT NULL , `replay` LONGTEXT NOT NULL) ENGINE = InnoDB;");
            Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    try {
                        sqlUtil.executeQuery("SELECT 1");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, 0, 15 * 20);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        recordingManager = new RecordingManager();
        replayManager = new ReplayManager();

        getCommand("test").setExecutor(new TestCommand());

        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static String generateID()  {
        String id;
        boolean isUnique;
        do {
            id = generateRandomID();
            isUnique = isIdUnique(id);
        } while (!isUnique);

        return id;
    }

    private static String generateRandomID() {
        return RandomStringUtils.randomAlphanumeric(17).toUpperCase();
    }

    public static boolean isIdUnique(String id) {
        String query = "SELECT COUNT(*) FROM `replaySystem_replays` WHERE id = ?";
        try (PreparedStatement stmt = getInstance().getSqlUtil().getConnection().prepareStatement(query)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    public static byte[] compressByteArray(final byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return new byte[0];
        }
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final OutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(bytes);
        }
        return out.toByteArray();
    }

    public static byte[] decompressByteArray(final byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return new byte[0];
        }
        try (final GZIPInputStream ungzip = new GZIPInputStream(new ByteArrayInputStream(bytes))) {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final byte[] data = new byte[8192];
            int nRead;
            while ((nRead = ungzip.read(data)) != -1) {
                out.write(data, 0, nRead);
            }
            return out.toByteArray();
        }
    }

    public static ReplaySystem getInstance() {
        return instance;
    }

    public SQLUtil getSqlUtil() {
        return sqlUtil;
    }

    public RecordingManager getRecordingManager() {
        return recordingManager;
    }

    public ReplayManager getReplayManager() {
        return replayManager;
    }
}
