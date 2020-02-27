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
    private final Country country;
    private final Map<Locale, Collection<Product>> data;

    @JsonCreator
    public CountryCollection(@JsonProperty("id") UUID id,
                             @JsonProperty("name") String name,
                             @JsonProperty("country")Country country,
                             @JsonProperty("data") Map<Locale, Collection<Product>> data) {
        super(id);
        this.name = name;
        this.country = country;
        this.data = data;
    }

    @JsonCreator
    public CountryCollection(@JsonProperty("name") String name,
                             @JsonProperty("country") Country country,
                             @JsonProperty("data")Map<Locale, Collection<Product>> data) {
        super();
        this.name = name;
        this.country = country;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public Country getCountry() {
        return country;
    }

    public Map<Locale, Collection<Product>> getData() {
        return data;
    }
}
