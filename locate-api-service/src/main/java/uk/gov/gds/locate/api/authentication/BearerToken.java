package uk.gov.gds.locate.api.authentication;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class BearerToken {

    private final String bearerToken;

    public BearerToken(String bearerToken) {
        this.bearerToken = checkNotNull(bearerToken);
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public String getObfuscatedToken() {
        return String.format("%s...%s", bearerToken.substring(0, 2), bearerToken.substring(bearerToken.length() - 2));
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("bearerToken", bearerToken)
                .toString();
    }
}
