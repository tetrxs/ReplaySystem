package net.minetaria.replaysystem.recording.recordable.entity;

import java.io.Serializable;

public class RecordableEntity implements Serializable {

    private SerializableEntity serializableEntity;

    public RecordableEntity(SerializableEntity serializableEntity) {
        this.serializableEntity = serializableEntity;
    }

    public SerializableEntity getSerializableEntity() {
        return serializableEntity;
    }
}
