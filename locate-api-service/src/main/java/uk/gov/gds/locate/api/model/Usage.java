package uk.gov.gds.locate.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.undercouch.bson4jackson.serializers.BsonDateSerializer;

import java.util.Date;

public class Usage {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("identifier")
    private String identifier;

    @JsonProperty("count")
    private Integer count;

    @JsonProperty("createdAt")
    private Date createdAt;

    @JsonProperty("expireAt")
    private Date expireAt;

    public Usage() {
    }

    public Usage(String id, String identifier, Integer count, Date expireAt) {
        this.id = id;
        this.identifier = identifier;
        this.count = count;
        this.expireAt = expireAt;
        this.createdAt = new Date();
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
