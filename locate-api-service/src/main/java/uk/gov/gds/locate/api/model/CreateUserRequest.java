package uk.gov.gds.locate.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserRequest {

    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("organisation")
    private String organisation;
    @JsonProperty("queryType")
    private String queryType;
    @JsonProperty("dataType")
    private String dataType;

    public CreateUserRequest() {
    }

    public CreateUserRequest(String name, String email, String organisation, String queryType, String dataType) {
        this.name = name;
        this.email = email;
        this.organisation = organisation;
        this.queryType = queryType;
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }


    public String getOrganisation() {
        return organisation;
    }


    public String getQueryType() {
        return queryType;
    }


    public String getDataType() {
        return dataType;
    }

}
