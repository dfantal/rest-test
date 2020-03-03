package io.antal.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.antal.rest.util.DataModel;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class CountryCollection extends DataModel {
    private final String name;
    private final Map<Country, Collection<Product>> data;


    @JsonCreator
    public CountryCollection(@JsonProperty("name") String name,
                             @JsonProperty("data") Map<Country, Collection<Product>> data) {
        super();
        this.name = name;
        this.data = data;
    }

    @JsonCreator
    public CountryCollection(@JsonProperty("id") String id,
            @JsonProperty("name") String name,
                             @JsonProperty("data") Map<Country, Collection<Product>> data) {
        super(UUID.fromString(id));
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public Map<Country, Collection<Product>> getData() {
        return data;
    }
}
