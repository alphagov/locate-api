package uk.gov.gds.locate.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.mongojack.ObjectId;
import uk.gov.gds.locate.api.json.DataTypeJsonDeserializer;
import uk.gov.gds.locate.api.json.DataTypeJsonSerializer;
import uk.gov.gds.locate.api.json.QueryTypeJsonDeserializer;
import uk.gov.gds.locate.api.json.QueryTypeJsonSerializer;

public class AuthorizationToken {

    @JsonProperty("_id")
    @ObjectId
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("identifier")
    private String identifier;

    @JsonProperty("organisation")
    private String organisation;

    @JsonProperty("token")
    private String token;

    @JsonSerialize(using = QueryTypeJsonSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    @JsonDeserialize(using = QueryTypeJsonDeserializer.class)
    private QueryType queryType;

    @JsonSerialize(using = DataTypeJsonSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    @JsonDeserialize(using = DataTypeJsonDeserializer.class)
    private DataType dataType;

    public AuthorizationToken() {
    }

    public AuthorizationToken(String id, String name, String identifier, String organisation, String token, QueryType queryType, DataType dataType) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.organisation = organisation;
        this.token = token;
        this.queryType = queryType;
        this.dataType = dataType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getToken() {
        return token;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public DataType getDataType() {
        return dataType;
    }

    @Override
    public String toString() {
        return "AuthorizationToken{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", identifier='" + identifier + '\'' +
                ", organisation='" + organisation + '\'' +
                ", token='" + token + '\'' +
                ", queryType=" + queryType +
                ", dataType=" + dataType +
                '}';
    }
}