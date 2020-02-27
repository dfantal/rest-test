package io.antal.rest.util;

import io.antal.rest.PagedResponse;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

public interface RestDao<T extends DataModel> {

    T getById(final UUID id) throws IOException;

    PagedResponse<T> get(final Collection<String> filters, final String anchor, final int count) throws IOException;

    T create(T model) throws IOException;

    T replace(final UUID id, T model) throws IOException;

    void delete(final UUID id) throws IOException;
}
