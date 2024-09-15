package net.minetaria.replaysystem.replaying;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import net.minetaria.replaysystem.ReplaySystem;
import net.minetaria.replaysystem.recording.Recording;
import net.minetaria.replaysystem.recording.recordable.Recordable;
import net.minetaria.replaysystem.recording.recordable.entity.SerializableEntity;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Replay extends BukkitRunnable implements Listener {

    public Replay instance;

    private Player viewer;
    private User user;
    private Recording recording;

    private long replayingTick = 0;
    private long replayed = 0;

    private World replayWorld;

    private ArrayList<NPC> replayNpcs;

    public Replay(Player viewer, Recording recording) {
        instance = this;

        this.viewer = viewer;
        this.user = PacketEvents.getAPI().getPlayerManager().getUser(viewer);
        this.recording = recording;

        try {
            replayWorld = copyWorld(recording.getReplayWorldPath(), recording.getWorldName() + recording.getId());
            if (replayWorld != null) {
                replayNpcs = new ArrayList<>();
                Bukkit.getPluginManager().registerEvents(instance, ReplaySystem.getInstance());
                viewer.teleport(replayWorld.getSpawnLocation());
                runTaskTimer(ReplaySystem.getInstance(),0,1L);
                System.out.println("[REPLAY STARTED] (" + recording.getId() + ")");
            } else {
                stop();
            }
        } catch (IOException e) {
            stop();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        ArrayList<Recordable> recordables = recording.getRecordables().get(replayingTick);
        if (replayed>=recording.getRecordables().keySet().size()) {
            stop();
        } else {
            if (recordables == null) {
                replayingTick++;
            } else {
                for (Recordable recordable:recordables) {
                    try {
                        recordable.replay(instance,user);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                replayed++;
                replayingTick++;
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (viewer.getUniqueId().equals(event.getPlayer().getUniqueId())) {
            HandlerList.unregisterAll(this);
            stop();
        }
    }

    public void stop() {
        ArrayList<NPC> loadedNpcs = new ArrayList<>(replayNpcs);
        for (NPC npc:loadedNpcs) {
            npc.destroy();
            replayNpcs.remove(npc);
        }
        File worldFolder = new File(Bukkit.getWorldContainer(), recording.getWorldName() + recording.getId());
        if (worldFolder.exists() && Bukkit.getWorld(recording.getWorldName() + recording.getId()) != null) {
            for (Player all:Bukkit.getWorld(recording.getWorldName() + recording.getId()).getPlayers()) {
                all.kickPlayer("[REPLAY FINISHED] (" + recording.getId() + ")");
            }
            Bukkit.unloadWorld(Bukkit.getWorld(recording.getWorldName() + recording.getId()), false);
            try {
                FileUtils.deleteDirectory(worldFolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cancel();
        System.out.println("[REPLAY FINISHED] (" + recording.getId() + ")");
    }

    public NPC registerNpc(SerializableEntity serializableEntity) {
        NPC npc = CitizensAPI.getNPCRegistry().getByUniqueId(serializableEntity.getBukkitEntityId());
        if (npc == null) {
            npc = CitizensAPI.getNPCRegistry().createNPC(serializableEntity.getEntityType(),serializableEntity.getBukkitEntityId(), 0, serializableEntity.getEntityName());
            if (serializableEntity.getEntityType() == EntityType.PLAYER) {
                SkinTrait skinTrait = npc.getOrAddTrait(SkinTrait.class);
                skinTrait.setSkinName("tetrxs");
                skinTrait.setShouldUpdateSkins(true);
                npc.addTrait(skinTrait);
            }
            replayNpcs.add(npc);
        }
        return npc;
    }

    private World copyWorld(String sourceWorldPath, String targetWorldName) throws IOException {
        File sourceWorldFolder = new File(sourceWorldPath);
        if (!sourceWorldFolder.exists() && !sourceWorldFolder.isDirectory()) {
            stop();
            return null;
        }
        File targetWorldFolder = new File(Bukkit.getWorldContainer(), targetWorldName);
        FileUtils.copyDirectory(sourceWorldFolder, targetWorldFolder);
        if (!targetWorldFolder.exists() && !targetWorldFolder.isDirectory()) {
            stop();
            return null;
        }
        File uidFile = new File(targetWorldFolder,"uid.dat");
        if (uidFile.exists()) {
            uidFile.delete();
        }
        return Bukkit.createWorld(new WorldCreator(targetWorldName));
    }

    public Replay getInstance() {
        return instance;
    }

    public Player getViewer() {
        return viewer;
    }

    public Recording getRecording() {
        return recording;
    }

    public World getReplayWorld() {
        return replayWorld;
    }
}
