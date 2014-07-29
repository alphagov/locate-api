package uk.gov.gds.locate.api.exceptions;


import com.google.common.collect.ImmutableMap;

import javax.annotation.concurrent.Immutable;
import javax.ws.rs.core.Response;

public class ResourceNotFoundException extends LocateWebException {

    public ResourceNotFoundException() {
        super(Response.Status.NOT_FOUND.getStatusCode(), ImmutableMap.of("error", "not found"));
    }
}
