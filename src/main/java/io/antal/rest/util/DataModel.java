package io.antal.rest.util;

import java.util.Objects;
import java.util.UUID;

public abstract class DataModel {
    private final UUID id;

    protected DataModel(UUID id) {
        Objects.requireNonNull(id, "id cannot be null!");
        this.id = id;
    }

    protected DataModel() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return this.id;
    }
}
