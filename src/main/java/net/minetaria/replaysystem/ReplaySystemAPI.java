package net.minetaria.replaysystem;

import net.minetaria.replaysystem.recording.Recording;

import java.util.UUID;

public class ReplaySystemAPI {

    public static Recording startRecording(UUID creator, String worldName) {
        return ReplaySystem.getInstance().getRecordingManager().startRecording(creator, worldName);
    }

    public static void stopRecording(String id) {
        ReplaySystem.getInstance().getRecordingManager().stopRecording(id);
    }

}
