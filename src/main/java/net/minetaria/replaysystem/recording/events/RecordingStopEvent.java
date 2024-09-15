package net.minetaria.replaysystem.recording.events;

import net.minetaria.replaysystem.recording.Recording;
import net.minetaria.replaysystem.recording.RecordingManager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RecordingStopEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private Recording recording;

    public RecordingStopEvent(Recording recording) {
        this.recording = recording;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Recording getRecording() {
        return recording;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}