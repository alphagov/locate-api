package uk.gov.gds.locate.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Usage {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("identifier")
    private String identifier;

    @JsonProperty("count")
    private Integer count;

    public Usage() {
    }

    public Usage(String id, String identifier, Integer count) {
        this.id = id;
        this.identifier = identifier;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Integer getCount() {
        return count;
    }
}
