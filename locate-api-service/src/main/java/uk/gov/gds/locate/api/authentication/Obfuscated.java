package uk.gov.gds.locate.api.authentication;

public abstract class Obfuscated {
    public static String getObfuscatedToken(String token) {
        return String.format("%s...%s", token.substring(0, 2), token.substring(token.length() - 2));
    }
}
