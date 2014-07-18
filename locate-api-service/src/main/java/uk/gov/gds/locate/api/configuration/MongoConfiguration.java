package uk.gov.gds.locate.api.configuration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import com.yammer.dropwizard.config.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private String hosts;

    @Valid
    @NotNull
    @JsonProperty
    private Integer port;

    @Valid
    @NotNull
    @JsonProperty
    private String locateDatabase;

    @Valid
    @NotNull
    @JsonProperty
    private String credentialsDatabase;

    @Valid
    @JsonProperty
    private String username;

    @Valid
    @JsonProperty
    private String password;

    public String getHosts() {
        return hosts;
    }

    public Integer getPort() {
        return port;
    }

    public String getLocateDatabase() {
        return locateDatabase;
    }

    public String getCredentialsDatabase() {
        return credentialsDatabase;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean requiresAuth() {
        return !Strings.isNullOrEmpty(this.username) && !Strings.isNullOrEmpty(this.password);
    }
}
