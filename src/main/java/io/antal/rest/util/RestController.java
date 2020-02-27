package io.antal.rest.util;

import io.antal.rest.PagedResponse;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * RestController provides a JAX-RS annotated class implementation for easily creating Nike standard rest apis
 *
 * URL patterns:
 *      GET - "/{id}"
 *      GET - "/"
 *      POST - "/"
 *      PUT - "/{id}"
 *      DELETE - "/{id}"
 *
 * @param <MODEL>
 */
public class RestController<MODEL extends DataModel> {

    private final String basePath;
    private final RestDao<MODEL> restDao;

    public RestController(String basePath, RestDao<MODEL> restDao) {
        this.basePath = basePath;
        this.restDao = restDao;
    }

    /**
     * Retreives a single object as indicated by the id
     *
     * @param id Id of the object to be retreived
     *
     * @return MODEL represented by the given id
     * @throws IOException
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public MODEL get(@BeanParam HttpServletRequest request, @PathParam("id") final String id) throws IOException {
        Locale locale = request.getLocale();
        return this.restDao.getById(parseAndValidateUUID(id));
    }


    /**
     * Get a single page of objects filtered by the given params
     *
     * @param filters
     * @param anchor
     * @param count
     * @return A single of of MODEL types represented by the parameters
     * @throws IOException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PagedResponse<MODEL> get(@QueryParam("filter") final List<String> filters,
                                    @QueryParam("anchor") String anchor,
                                    @QueryParam("count") @DefaultValue("100") int count) throws IOException {
        return this.restDao.get(filters, anchor, count);
    }

    /**
     * Creates a new item using the given data
     *
     * @param data
     * @return The created MODEL
     * @throws IOException
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public MODEL create(MODEL data) throws IOException {
        return this.restDao.create(data);
    }

    /**
     * Sets the value of the given id to the given data
     *
     * @param id
     * @param data
     * @return The set MODEL
     * @throws IOException
     */
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public MODEL replace(@PathParam("id") final String id,
                         MODEL data) throws IOException  {
        return this.restDao.replace(parseAndValidateUUID(id), data);
    }

    /**
     * Deletes the object referenced by id
     * @param id
     * @return Response of Accepted - 200
     * @throws IOException
     */
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") final String id) throws IOException {
        this.restDao.delete(parseAndValidateUUID(id));
        return Response.accepted().build();
    }

    private static UUID parseAndValidateUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to parse given id into UUID. " + e.getMessage(), e);
        }
    }
}
