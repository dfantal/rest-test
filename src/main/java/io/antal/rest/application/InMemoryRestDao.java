package io.antal.rest.application;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import io.antal.rest.PagedResponse;
import io.antal.rest.util.DataModel;
import io.antal.rest.util.Filters;
import io.antal.rest.util.RestDao;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

public class InMemoryRestDao<MODEL extends DataModel> implements RestDao<MODEL> {

    private final Map<UUID, MODEL> datastore;
    private final String path;

    public InMemoryRestDao(String path, Map<UUID, MODEL> initialData) {
        this.path = path;
        this.datastore = new ConcurrentSkipListMap<>(initialData);
    }

    public InMemoryRestDao(String path) {
        this(path, Collections.emptyMap());
    }

    @Override
    public MODEL getById(UUID id) throws IOException {
        return datastore.get(id);
    }

    @Override
    public PagedResponse<MODEL> get(Collection<String> filtersString, String anchor, int count) throws IOException {
        Filters<MODEL> filters = new Filters<>(filtersString);
        boolean passedAnchor = anchor == null || anchor.isEmpty();
        UUID anchorUUID = UUID.fromString(anchor);
        List<MODEL> data = new ArrayList<>(count);
        UUID lastId = null;
        for (Map.Entry<UUID, MODEL> entry : datastore.entrySet()) {
            if (passedAnchor && filters.test(entry)) {
                data.add(entry.getValue());
                lastId = entry.getKey();
            } else if (anchorUUID.equals(entry.getKey())) {
                passedAnchor = true;
            }

            if (data.size() >= count) {
                break;
            }
        }

        String nextPage = "";
        if (count == data.size() && lastId != null) {
            nextPage = this.path;
            nextPage += "?" + filtersString.stream().map(filt -> "filter=" + Base64.encode(filt.getBytes(StandardCharsets.UTF_8))).collect(Collectors.joining("&"));
            nextPage += "&count=" + count;
            nextPage += "&anchor=" + lastId.toString();
        }

        return new PagedResponse<>(data, new PagedResponse.Pages(nextPage));
    }

    @Override
    public MODEL create(MODEL model) throws IOException {
        this.datastore.put(model.getId(), model);
        return model;
    }

    @Override
    public MODEL replace(UUID id, MODEL model) throws IOException {
        this.datastore.put(id, model);
        return model;
    }

    @Override
    public void delete(UUID id) throws IOException {
        this.datastore.remove(id);
    }
}
