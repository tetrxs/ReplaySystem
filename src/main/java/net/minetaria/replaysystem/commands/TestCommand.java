package net.minetaria.replaysystem.commands;

import net.minetaria.replaysystem.ReplaySystem;
import net.minetaria.replaysystem.recording.RecordingManager;
import net.minetaria.replaysystem.replaying.Replay;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (strings.length > 0) {
                switch (strings[0]) {
                    case "start":
                        ReplaySystem.getInstance().getRecordingManager().startRecording(player.getUniqueId(), player.getWorld().getName());
                        break;
                    case "stop":
                        ReplaySystem.getInstance().getRecordingManager().stopRecording(strings[1]);
                        break;
                    case "replay":
                        try {
                            ReplaySystem.getInstance().getReplayManager().startReplay(player,strings[1]);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "copy":
                        try {
                            copyWorld("/home/Minecraft/Cloud/Minetaria/tmp/KitPvP-1/./Lobby", "testWelt");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                }
            }
        }
        return false;
    }

    private World copyWorld(String sourceWorldPath, String targetWorldName) throws IOException {
        /*
        File sourceWorldFolder = new File(sourceWorldPath);
        if (sourceWorldFolder.exists() && sourceWorldFolder.isDirectory()) {
            File targetWorldFolder = new File(Bukkit.getWorldContainer(), targetWorldName);
            System.out.println(targetWorldFolder.getAbsolutePath());
            Files.walk(sourceWorldFolder.toPath()).forEach(sourcePath -> {
                try {
                    Path targetPath = targetWorldFolder.toPath().resolve(sourceWorldFolder.toPath().relativize(sourcePath));
                    File uidFile = new File(sourceWorldFolder,"uid.dat");
                    if (uidFile.exists()) {
                        uidFile.delete();
                    }
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return Bukkit.createWorld(new WorldCreator(targetWorldName));
        } else {
            return null;
        }*/

        File sourceWorldFolder = new File(sourceWorldPath);
        if (!sourceWorldFolder.exists() && !sourceWorldFolder.isDirectory()) {
            System.out.println("FEHLER1");
            return null;
        }
        File targetWorldFolder = new File(Bukkit.getWorldContainer(), targetWorldName);
        FileUtils.copyDirectory(sourceWorldFolder, targetWorldFolder);
        if (!targetWorldFolder.exists() && !targetWorldFolder.isDirectory()) {
            System.out.println("FEHLER2");
            return null;
        }
        File uidFile = new File(targetWorldFolder,"uid.dat");
        if (uidFile.exists()) {
            uidFile.delete();
        }
        return Bukkit.createWorld(new WorldCreator(targetWorldName));
    }
}
