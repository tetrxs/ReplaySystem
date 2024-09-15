package net.minetaria.replaysystem.recording.recordable.entity;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.w3c.dom.Entity;

import java.io.Serializable;
import java.util.UUID;

public class SerializableEntity implements Serializable {

    UUID bukkitEntityId;
    EntityType entityType;
    String entityName;

    public SerializableEntity(UUID bukkitEntityId, EntityType entityType, String entityName) {
        this.bukkitEntityId = bukkitEntityId;
        this.entityType = entityType;
        this.entityName = entityName;
    }

    public UUID getBukkitEntityId() {
        return bukkitEntityId;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getEntityName() {
        return entityName;
    }
}
