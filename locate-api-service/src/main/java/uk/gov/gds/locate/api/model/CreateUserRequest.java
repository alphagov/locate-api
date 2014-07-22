package uk.gov.gds.locate.api.model;

public class CreateUserRequest {

    private String name;
    private String email;
    private String organisation;
    private String queryType;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
