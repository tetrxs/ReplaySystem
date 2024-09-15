package net.minetaria.replaysystem.recording.recordable.locaiton;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.Serializable;

public class SerializableLocation implements Serializable {

    private String worldName;
    private double x, y, z;
    private float pitch, yaw;

    public SerializableLocation(Location location) {
        this.worldName = "";
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
    }

    public SerializableLocation(String worldName, double x, double y, double z, float pitch, float yaw) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public void refreshWorldName(String worldName) {
        this.worldName = worldName;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(worldName),x,y,z,yaw,pitch);
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public String getWorldName() {
        return worldName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }
}
