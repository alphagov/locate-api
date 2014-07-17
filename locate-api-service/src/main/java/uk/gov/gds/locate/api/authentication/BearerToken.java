package uk.gov.gds.locate.api.authentication;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static uk.gov.gds.locate.api.authentication.Obfuscated.getObfuscatedToken;

public class BearerToken {

    private final String bearerToken;

    public BearerToken(String bearerToken) {
        this.bearerToken = checkNotNull(bearerToken);
    }

    public String getBearerToken() {
        return bearerToken;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("bearerToken", getObfuscatedToken(bearerToken))
                .toString();
    }
}
