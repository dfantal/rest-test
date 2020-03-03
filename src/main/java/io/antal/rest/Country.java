package io.antal.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Locale;
import java.util.Objects;

@JsonSerialize(using = ToStringSerializer.class)

public class Country {

    private final String name;
    private final String iso3;

    @JsonCreator

    public static Country fromIso2(String iso2) {
        Objects.requireNonNull(iso2, "ISO2 country code cannot be null!");
        return fromLocale(new Locale("", iso2));
    }

    public static Country fromLocale(Locale locale) {
        return new Country(locale.getDisplayName(), locale.getISO3Country());
    }

    private Country(String name, String iso3) {
        this.name = name;
        this.iso3 = iso3;
    }

    public String getName() {
        return name;
    }

    public String getIso3() {
        return iso3;
    }

    @Override
    public String toString() {
        return iso3;
    }
}
