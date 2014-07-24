package uk.gov.gds.locate.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gds.locate.api.exceptions.LocateWebException;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class LocateExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Throwable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocateExceptionMapper.class);

    protected static final String CONTENT_TYPE = "application/json; charset=utf-8";
    protected static final String CONTENT_TYPE_HEADER = "Content-Type";

    @Override
    public Response toResponse(Throwable exception) {

        if (exception instanceof LocateWebException) {
            LOGGER.error("WebApplicationException", exception);
            return ((LocateWebException) exception).getResponse();
        }

        LOGGER.error("Server Error", exception);

        return Response.status(
                Response.Status.fromStatusCode(500))
                .header(CONTENT_TYPE_HEADER, CONTENT_TYPE)
                .entity("{ \"error\" : \"Internal Server Error\" }")
                .build();
    }
}