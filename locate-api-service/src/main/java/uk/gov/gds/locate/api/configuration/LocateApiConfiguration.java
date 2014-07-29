package uk.gov.gds.locate.api.configuration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocateApiConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private MongoConfiguration mongoConfiguration = new MongoConfiguration();

    @Valid
    @NotNull
    @JsonProperty
    private Integer maxRequestsPerDay;

    @Valid
    @NotNull
    @JsonProperty
    private String encryptionKey;

    @Valid
    @NotNull
    @JsonProperty
    private Boolean encrypted;

    @Valid
    @NotNull
    @JsonProperty
    private String allowedOrigins;

    public MongoConfiguration getMongoConfiguration() {
        return mongoConfiguration;
    }

    public Integer getMaxRequestsPerDay() {
        return maxRequestsPerDay;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public Boolean getEncrypted() {
        return encrypted;
    }

    public String getAllowedOrigins() {
        return allowedOrigins;
    }

    @Override
    public String toString() {
        return "LocateApiConfiguration{" +
                "mongoConfiguration=" + mongoConfiguration +
                ", maxRequestsPerDay=" + maxRequestsPerDay +
                ", encryptionKey='" + encryptionKey + '\'' +
                ", encrypted=" + encrypted +
                ", allowedOrigins='" + allowedOrigins + '\'' +
                '}';
    }
}
