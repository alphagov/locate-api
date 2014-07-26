package uk.gov.gds.locate.api.exceptions;

import javax.ws.rs.core.Response;

public class LocateWebException extends Exception {

    protected static final String CONTENT_TYPE = "application/json; charset=utf-8";
    protected static final String CONTENT_TYPE_HEADER = "Content-Type";

    private final int statusCode;
    private final Object body;

    public LocateWebException(int statusCode, Object body) {
        this(statusCode, body, null);
    }

    public LocateWebException(int statusCode, Object body, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Object getBody() {
        return body;
    }

    public Response getResponse() {
        return Response
                .status(statusCode)
                .header(CONTENT_TYPE_HEADER, CONTENT_TYPE)
                .entity(body)
                .build();
    }


    @Override
    public String toString() {
        return "LocateWebException{" +
                "statusCode=" + statusCode +
                ", body=" + body +
                '}';
    }
}