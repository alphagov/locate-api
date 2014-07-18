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
    private String databaseName;

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

    public String getDatabaseName() {
        return databaseName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean hasAuth() {
        return !Strings.isNullOrEmpty(this.username) && !Strings.isNullOrEmpty(this.password);
    }

    @Override
    public String toString() {
        return "MongoConfiguration{" +
                "hosts='" + hosts + '\'' +
                ", port=" + port +
                ", databaseName='" + databaseName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
