package io.antal.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.antal.rest.util.DataModel;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class Product extends DataModel {
    private final Map<String, String> attributes;

    @JsonCreator
    public Product(@JsonProperty("attributes") Map<String, String> attributes) {
        super();
        this.attributes = Collections.unmodifiableMap(attributes);
    }

    @JsonCreator
    public Product(@JsonProperty("id") UUID id, @JsonProperty("attributes") Map<String, String> attributes) {
        super(id);
        this.attributes = Collections.unmodifiableMap(attributes);
    }
}
