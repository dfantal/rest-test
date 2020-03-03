package io.antal.rest.util;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebApplicationExceptionHandler implements ExceptionMapper<WebApplicationException> {

    private static final Logger LOGGER = Logger.getLogger(WebApplicationExceptionHandler.class.getName());

    @Override
    public Response toResponse(WebApplicationException exception) {
        LOGGER.log(Level.SEVERE, exception.getResponse().getStatus() + " - " + exception.getMessage(), exception);
        return Response.status(exception.getResponse().getStatus())
                .entity(Entity.text("{\"status\":\"" + exception.getResponse().getStatus() + "\"}"))
                .header("Content-Type", "application/json")
                .build();
    }
}
