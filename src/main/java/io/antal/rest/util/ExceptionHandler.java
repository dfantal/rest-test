package io.antal.rest.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionHandler implements ExceptionMapper<Throwable> {

    private static final Logger logger = Logger.getLogger(ExceptionHandler.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        logger.log(Level.SEVERE, exception.getMessage(), exception);

        return Response.serverError().build();
    }
}
