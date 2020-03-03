package io.antal.rest.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.antal.rest.PagedResponse;
import io.antal.rest.util.DataModel;
import io.antal.rest.util.Filters;
import io.antal.rest.util.RestDao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class InMemoryRestDao<MODEL extends DataModel> implements RestDao<MODEL> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = Logger.getLogger(InMemoryRestDao.class.getName());

    private class DiskSaver implements Runnable {

        private final Path path;

        private DiskSaver(Path path) {
            this.path = path;
        }

        @Override
        public void run() {
            try (FileWriter writer = new FileWriter(this.path.toFile())) {
                OBJECT_MAPPER.writeValue(writer, InMemoryRestDao.this.datastore);
            } catch (IOException io) {
                LOGGER.log(Level.SEVERE, io.getMessage(), io);
            }
        }
    }

    private final Map<UUID, MODEL> datastore;
    private final String path;
    private final ExecutorService executorService;
    private final DiskSaver saver;

    public InMemoryRestDao(String path, Map<UUID, MODEL> initialData) {
        this.path = path;
        this.datastore = new ConcurrentSkipListMap<>(initialData);
        this.executorService = Executors.newSingleThreadExecutor();
        this.saver = new DiskSaver(getCache(path));
    }

    public InMemoryRestDao(String path) {
        this(path, loadFromCache(path));
    }

    @Override
    public MODEL getById(UUID id) throws IOException {
        return datastore.get(id);
    }

    @Override
    public PagedResponse<MODEL> get(Collection<String> filtersString, String anchor, int count) throws IOException {
        Filters<MODEL> filters = new Filters<>(filtersString);
        boolean passedAnchor = anchor == null || anchor.isEmpty();
        UUID anchorUUID = anchor != null ? UUID.fromString(anchor) : null;
        List<MODEL> data = new ArrayList<>(count);
        UUID lastId = null;
        for (Map.Entry<UUID, MODEL> entry : datastore.entrySet()) {
            if (passedAnchor && filters.test(entry)) {
                data.add(entry.getValue());
                lastId = entry.getKey();
            } else if (entry.getKey().equals(anchorUUID)) {
                passedAnchor = true;
            }

            if (data.size() >= count) {
                break;
            }
        }

        String nextPage = "";
        if (count == data.size() && lastId != null) {
            nextPage = this.path;
            nextPage += "?" + filtersString.stream().map(filt -> "filter=" + filt).collect(Collectors.joining("&"));
            nextPage += "&count=" + count;
            nextPage += "&anchor=" + lastId.toString();
        }

        return new PagedResponse<>(data, new PagedResponse.Pages(nextPage));
    }

    @Override
    public MODEL create(MODEL model) throws IOException {
        this.datastore.put(model.getId(), model);
        this.executorService.submit(saver);
        return model;
    }

    @Override
    public MODEL replace(UUID id, MODEL model) throws IOException {
        this.datastore.put(id, model);
        this.executorService.submit(saver);
        return model;
    }

    @Override
    public void delete(UUID id) throws IOException {
        this.datastore.remove(id);
        this.executorService.submit(saver);
    }

    private static <MODEL> Map<UUID, MODEL> loadFromCache(String pathString) {
        try {
            Path path = getCache(pathString);
            if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
                return new ObjectMapper().readValue(Files.readAllBytes(path), new TypeReference<Map<UUID, MODEL>>() {
                });
            } else {
                return new HashMap<>();
            }
        } catch (IOException io) {
            throw new IllegalArgumentException("Failed to load cache! " + io.getMessage(), io);
        }
    }

    private static Path getCache(String pathString) {
        return new File(new String(Base64.getEncoder().encode(pathString.getBytes(StandardCharsets.UTF_8)))).toPath();
    }
}
