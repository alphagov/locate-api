package uk.gov.gds.locate.api.configuration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    public String getHosts() {
        return hosts;
    }

    public Integer getPort() {
        return port;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
