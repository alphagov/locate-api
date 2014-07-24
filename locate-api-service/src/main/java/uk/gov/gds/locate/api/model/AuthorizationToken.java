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

    @JsonProperty("identifier")
    private String identifier;

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

    public AuthorizationToken(String id, String identifier, String token, QueryType queryType, DataType dataType) {
        this.id = id;
        this.identifier = identifier;
        this.token = token;
        this.queryType = queryType;
        this.dataType = dataType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getToken() {
        return token;
    }

    public QueryType getQueryType() {
        return this.queryType;
    }

    public DataType getDataType() {
        return dataType;
    }

    @Override
    public String toString() {
        return "AuthorizationToken{" +
                "id='" + id + '\'' +
                ", identifier='" + identifier + '\'' +
                ", token='" + token + '\'' +
                ", queryType=" + queryType +
                '}';
    }
}