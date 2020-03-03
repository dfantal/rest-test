package io.antal.rest;

import io.antal.rest.application.InMemoryRestDao;
import io.antal.rest.util.RestController;
import io.antal.rest.util.RestDao;

import javax.ws.rs.Path;

@Path("product")
public class CountryCollectionController extends RestController<CountryCollection> {

    public CountryCollectionController() {
        super("product", new InMemoryRestDao<>("product"));
    }
}
