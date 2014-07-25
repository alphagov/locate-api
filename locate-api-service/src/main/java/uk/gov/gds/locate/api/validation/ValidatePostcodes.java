package uk.gov.gds.locate.api.validation;

import org.fest.util.Strings;

public abstract class ValidatePostcodes {

    public static Boolean isValid(String postcode) {
        return !Strings.isNullOrEmpty(postcode) && postcode.length() < 10; // && regexForOnlyLettersAndNumbers;
    }
}
