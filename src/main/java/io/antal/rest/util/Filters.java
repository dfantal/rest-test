package io.antal.rest.util;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class Filters<MODEL> implements Predicate<Map.Entry<UUID, MODEL>> {

    public Filters(Collection<String> filters) {
        
    }

    @Override
    public boolean test(Map.Entry<UUID, MODEL> uuidmodelEntry) {
        return true;
    }
}
