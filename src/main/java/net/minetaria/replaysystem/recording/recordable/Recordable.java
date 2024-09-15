package net.minetaria.replaysystem.recording.recordable;

import com.github.retrooper.packetevents.protocol.player.User;
import net.minetaria.replaysystem.recording.Recording;
import net.minetaria.replaysystem.replaying.Replay;

import java.io.Serializable;

public abstract class Recordable implements Serializable {

    private Recording recording;

    public Recordable(Recording recording) {
        this.recording = recording;
        recording.record(this);
    }

    public abstract void replay(Replay replay, User user) throws ClassNotFoundException, Exception;

    public Recording getRecording() {
        return recording;
    }
}
