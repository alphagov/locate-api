package uk.gov.gds.locate.api.services;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;

import java.util.Random;

public class BearerTokenGenerationService {
    int tokenBinaryDataLengthInBytes = 20;

    public String newToken() {
        byte[] bytes = new byte[tokenBinaryDataLengthInBytes];
        new Random(new DateTime().getMillis()).nextBytes(bytes);
        return Base64.encodeBase64URLSafeString(bytes);
    }
}
